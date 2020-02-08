package com.tensquare.article.service;

import com.tensquare.article.pojo.Comment;

import java.awt.*;
import java.util.List;


/**
 * 文章评论接口
 */
public interface CommentService {

    //新增评论
    void add(Comment comment);

    //删除评论
    void deleteById(String id);

    //修改评论
    void update(Comment comment);

    //查询所有评论
    List<Comment> findAll();

    //根据id查询评论
    Comment findById(String id);

    //根据文章id查询评论
    List<Comment> findByArticleId(String articleId);

    //点赞
    void thumbup(String id);
}
