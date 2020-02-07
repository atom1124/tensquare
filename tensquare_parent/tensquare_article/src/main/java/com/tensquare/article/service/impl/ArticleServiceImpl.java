package com.tensquare.article.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.article.dao.ArticleDao;
import com.tensquare.article.pojo.Article;
import com.tensquare.article.service.ArticleService;
import com.tensquare.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.smartcardio.ATR;
import java.util.List;
import java.util.Map;

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

        articleDao.insert(article);
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
            entityWrapper.eq("title", map.get("title"));
        }
        if (!StringUtils.isEmpty(map.get("content"))) {
            entityWrapper.eq("content", map.get("content"));
        }
        //调用dao
        List<Article> articleListPage = articleDao.selectPage(pageList, entityWrapper);
        pageList.setRecords(articleListPage);
        return pageList;
    }


}
