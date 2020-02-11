package com.tensquare.article.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.article.pojo.Article;

import java.util.List;
import java.util.Map;

/**
 * 业务层接口
 */
public interface ArticleService {

    //新增文章
    void add(Article article);

    //根据id删除文章
    void delete(String id);

    //查询所有
    List<Article> findAll();

    //根据id查询文章
    Article findById(String id);

    //修改文章
    void update(Article article);

    //条件查询和分页
    Page<Article> search(Map map, int page, int size);

    //订阅
    boolean subscribe(String userId, String articleId);

    //点赞
    boolean thumbup(String userId, String articleId);
}
