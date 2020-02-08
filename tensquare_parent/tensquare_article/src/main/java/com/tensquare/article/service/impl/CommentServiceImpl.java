package com.tensquare.article.service.impl;

import com.tensquare.article.pojo.Comment;
import com.tensquare.article.repository.CommentRepository;
import com.tensquare.article.service.CommentService;
import com.tensquare.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 新增评论
     * @param comment
     */
    @Override
    public void add(Comment comment) {
        String userid = "1122";
        comment.setUserid(userid);
        String id = idWorker.nextId() + "";
        comment.set_id(id);
        //初始化数据
        comment.setPublishdate(new Date());
        comment.setThumbup(0);
        commentRepository.save(comment);

    }


    /**
     * 根据id删除指定评论
     * @param id
     */
    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);


    }

    /**
     * 修改评论
     * @param comment
     */
    @Override
    public void update(Comment comment) {
        commentRepository.save(comment);


    }

    /**
     * 查询所有评论
     * @return
     */
    @Override
    public List<Comment> findAll() {
        List<Comment> commentList = commentRepository.findAll();
        return commentList;
    }

    /**
     * 根据id查询评论
     * @return
     */
    @Override
    public Comment findById(String id) {
        Comment comment = commentRepository.findById(id).get();
        return comment;
    }

    /**
     * 根据文章id查询评论
     * @param articleId
     * @return
     */
    @Override
    public List<Comment> findByArticleId(String articleId) {
        List<Comment> commentList = commentRepository.findByArticleid(articleId);
        return commentList;
    }

    /**
     * 点赞
     * @param id
     */
    @Override
    public void thumbup(String id) {
        //方式一
        //Comment comment = commentRepository.findById(id).get();
        //comment.setThumbup(comment.getThumbup() + 1);
        //commentRepository.save(comment);

        //方式二

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));

        Update update = new Update();
        update.inc("thumbup",1);

        mongoTemplate.updateFirst(query,update,"comment");


    }
}
