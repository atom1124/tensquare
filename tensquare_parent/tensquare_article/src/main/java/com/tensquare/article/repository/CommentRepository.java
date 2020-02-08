package com.tensquare.article.repository;

import com.tensquare.article.pojo.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * 操作Mongodb接口
 */
public interface CommentRepository extends MongoRepository<Comment,String> {

    //根据文章id查询评论
    //规则：findBy+条件属性Articleid
    List<Comment> findByArticleid(String articleId);
}
