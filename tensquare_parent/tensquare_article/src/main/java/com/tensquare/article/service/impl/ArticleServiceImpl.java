package com.tensquare.article.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.article.client.NoticeClient;
import com.tensquare.article.config.RabbitmqConfig;
import com.tensquare.article.dao.ArticleDao;
import com.tensquare.article.pojo.Article;
import com.tensquare.article.pojo.Notice;
import com.tensquare.article.service.ArticleService;
import com.tensquare.util.IdWorker;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 业务实现类
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private NoticeClient noticeClient;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DirectExchange exchange;


    /**
     * 新增文章
     * @param article
     */
    @Override
    public void add(Article article) {
        //1.新增文章
        String authorId = "1";
        article.setId(idWorker.nextId() + "");
        article.setVisits(0);   //浏览量
        article.setThumbup(0);  //点赞数
        article.setComment(0);  //评论数
        article.setUserid(authorId);

        //2.通知
        Set<String> members = redisTemplate.opsForSet().members("article_author_" + authorId);

        for (String uid : members) {
            //消息通知
            Notice notice = new Notice();
            notice.setReceiverId(uid);
            notice.setOperatorId(authorId);
            notice.setAction("publish");
            notice.setTargetType("article");
            notice.setTargetId(article.getId());
            notice.setCreatetime(new Date());
            notice.setType("sys");
            notice.setState("0");
            noticeClient.add(notice);
        }
        articleDao.insert(article);
        //入库成功后,发送mq消息,内容是消息通知id
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ARTICLE,authorId,article.getId());
    }

    /**
     * 删除文章
     * @param id
     */
    @Override
    public void delete(String id) {
        articleDao.deleteById(id);
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<Article> findAll() {
        List<Article> articleList = articleDao.selectList(null);
        return articleList;
    }

    /**
     * 根据id查询文章
     * @param id
     * @return
     */
    @Override
    public Article findById(String id) {
        Article article = articleDao.selectById(id);
        return article;
    }

    /**
     * 修改文章
     * @param article
     */
    @Override
    public void update(Article article) {
        articleDao.updateById(article);
    }

    /**
     * 条件查询和分页
     * @param map
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Article> search(Map map, int page, int size) {

        //1.分装分页对象
        Page<Article> pageList = new Page<>(page, size);
        //2.封装分页对象
        EntityWrapper<Article> entityWrapper = new EntityWrapper<>();

        if (!StringUtils.isEmpty(map.get("columnid"))) {
            entityWrapper.eq("columnid", map.get("columnid"));
        }
        if (!StringUtils.isEmpty(map.get("userid"))) {
            entityWrapper.eq("userid", map.get("userid"));
        }
        if (!StringUtils.isEmpty(map.get("title"))) {
            entityWrapper.eq("title", "%" + map.get("title") + "%");
        }
        if (!StringUtils.isEmpty(map.get("content"))) {
            entityWrapper.eq("content", "%" + map.get("content") + "%");
        }
        //调用dao
        List<Article> articleListPage = articleDao.selectPage(pageList, entityWrapper);
        pageList.setRecords(articleListPage);
        return pageList;
    }

    /**
     * 订阅
     * @param userId
     * @param articleId
     * @return
     */
    @Override
    public boolean subscribe(String userId, String articleId) {
        //根据文章id查询文章作者authorId
        String authorId = articleDao.selectById(articleId).getUserid();
        //2.存到Redis
        //用户key=userKey value =作者集合
        String userKey = "article_user_" + userId;
        //作者key=authorKey  value=用户集合
        String authorKey = "article_author_" + authorId;
        //判断用户是否已经关注作者
        Boolean isMember = redisTemplate.opsForSet().isMember(userKey, authorId);
        //创建queue
        Queue queue = new Queue("article_subscribe_"+userId,true);
        //声明exchange和queue的绑定关系，设置路由键为作者id
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(authorId);


        //3.判断是否已经订阅
        if (isMember) {
            //取消关注
            redisTemplate.opsForSet().remove(userKey, authorId);
            redisTemplate.opsForSet().remove(authorKey, userId);
            //进行解绑
            rabbitAdmin.removeBinding(binding);
            return false;
        } else {
            //产生订阅关系
            redisTemplate.opsForSet().add(userKey, authorId);
            redisTemplate.opsForSet().add(authorKey, userId);
            //进行绑定
            rabbitAdmin.declareQueue(queue);
            rabbitAdmin.declareBinding(binding);
            return true;
        }

    }

    /**
     * 实现点赞功能
     * @param userId
     * @param articleId
     * @return
     */
    @Override
    public boolean thumbup(String articleId, String userId) {
        //1.判断是否已经点赞
        Object o = redisTemplate.opsForValue().get("article_thumbup_" + userId + "_" + articleId);
        //2.如果没有点赞，则点赞
        if (!StringUtils.isEmpty(o)) {
            return false;//重复点赞
        }
        Article article = articleDao.selectById(articleId);
        article.setThumbup(article.getThumbup() + 1);
        articleDao.updateById(article);
        //将点赞记录存入
        redisTemplate.opsForValue().set("article_thumbup_" + userId + "_" + articleId, "1");

        //点赞成功后 调用消息通知微服务写入消息表
        Notice notice = new Notice();
        notice.setReceiverId(article.getUserid());//接收消息的作者id
        notice.setOperatorId(userId);//当前登录的用户
        notice.setAction("publish");
        notice.setTargetType("thumbup");//点赞操作
        notice.setTargetId(article.getId());//文章id
        notice.setType("user");//消息通知系统群发的
        noticeClient.add(notice);

        return true;
    }


}
