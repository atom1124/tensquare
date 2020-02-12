package com.tensquare.notice.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.entity.Result;
import com.tensquare.notice.client.ArticleClient;
import com.tensquare.notice.client.UserClient;
import com.tensquare.notice.dao.NoticeDao;
import com.tensquare.notice.dao.NoticeFreshDao;
import com.tensquare.notice.pojo.Notice;
import com.tensquare.notice.pojo.NoticeFresh;
import com.tensquare.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 消息通知业务层
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class NoticeService {

    @Autowired
    private NoticeDao noticeDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private NoticeFreshDao noticeFreshDao;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ArticleClient articleClient;


    /**
     * 根据id查询消息通知
     * @param id
     * @return
     */
    public Notice findById(String id) {
        Notice notice = noticeDao.selectById(id);
        return notice;

    }

    /**
     * 新增消息通知
     * @param notice
     */
    @Transactional
    public void add(Notice notice) {
        //1.设置初始化数据
        String id = idWorker.nextId() + "";
        notice.setId(id);
        notice.setCreatetime(new Date());
        notice.setState("0");
        noticeDao.insert(notice);

        //2.待推送信入库,新消息提醒
        //NoticeFresh noticeFresh = new NoticeFresh();
        //noticeFresh.setUserId(notice.getReceiverId());//待通知用户的id
        //noticeFresh.setNoticeId(id);
        //noticeFreshDao.insert(noticeFresh);//保存进数据库

    }


    /**
     * 设置消息通知信息/提取公共方法
     * @param notice
     */
    public void setNoticeInfo(Notice notice) {
        //查询用户昵称
        Result resultUser = userClient.findById(notice.getReceiverId());
        Map userMap = (Map) resultUser.getData();
        String nickname = (String) userMap.get("nickname");
        notice.setOperatorName(nickname);

        //查询文章标题
        //判断消息类型
        if (notice.getTargetType().equals("article")) {
            Result resultArticle = articleClient.findById(notice.getTargetId());
            Map articleMap = (Map) resultArticle.getData();
            String title = (String) articleMap.get("title");
            notice.setTargetName(title);
        }


    }


    /**
     * 分页查询消息通知
     * @param page
     * @param size
     * @return
     */
    public Page<Notice> findPage(int page, int size) {

        Page<Notice> noticePage = new Page<>(page, size);
        List<Notice> notices = noticeDao.selectPage(noticePage, null);
        for (Notice notice : notices) {
            setNoticeInfo(notice);
        }
        noticePage.setRecords(notices);
        return noticePage;
    }


    /**
     * 修改通知
     * @param notice
     */
    public void updateById(Notice notice) {
        noticeDao.updateById(notice);
    }

    /**
     * 根据用户id查询该用户的待推送消息（新消息）
     * @param userId
     * @param page
     * @param size
     * @return
     */
    public Page<NoticeFresh> freshPage(String userId, int page, int size) {

        Page<NoticeFresh> noticeFreshPage = new Page<>(page, size);
        EntityWrapper<NoticeFresh> noticeFreshEntityWrapper = new EntityWrapper<>();
        noticeFreshEntityWrapper.eq("userId", userId);
        List<NoticeFresh> noticeFreshes = noticeFreshDao.selectPage(noticeFreshPage, noticeFreshEntityWrapper);
        return noticeFreshPage.setRecords(noticeFreshes);


    }

    /**
     * 删除待推送数据
     * @param noticeFresh
     */
    public void delete(NoticeFresh noticeFresh) {
        noticeFreshDao.delete(new EntityWrapper<>(noticeFresh));
    }
}
