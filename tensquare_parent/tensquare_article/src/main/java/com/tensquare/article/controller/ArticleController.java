package com.tensquare.article.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.article.pojo.Article;
import com.tensquare.article.service.ArticleService;
import com.tensquare.entity.PageResult;
import com.tensquare.entity.Result;
import com.tensquare.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/article")
@CrossOrigin
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 文章新增
     * @param article
     * @return
     */
    @PostMapping(value = "/add")
    public Result add(@RequestBody Article article) {
        articleService.add(article);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /**
     * 根据id删除文章
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}")
    public Result delete(@PathVariable String id) {
        articleService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功!");
    }

    /**
     * 查询所有文章
     * @return
     */
    @RequestMapping(value = "/findAll")
    public Result findAll() {
        int a = 10/0;
        List<Article> articleList = articleService.findAll();
        return new Result(true, StatusCode.OK, "查询所有文章成功!", articleList);
    }

    /**
     * 根据id查询文章
     * @return
     */
    @RequestMapping(value = "/findById/{id}")
    public Result findById(@PathVariable String id) {
        Article article = articleService.findById(id);
        return new Result(true, StatusCode.OK, "查询指定文章成功!", article);
    }

    /**
     * 修改文章
     * @return
     */
    @PostMapping(value = "/update/{id}")
    public Result findById(@PathVariable String id, @RequestBody Article article) {
        article.setId(id);
        articleService.update(article);
        return new Result(true, StatusCode.OK, "修改文章成功!");
    }


    /**
     * 条件查询和分页
     * @param map
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}")
    public Result search(@RequestBody Map map, @PathVariable int page, @PathVariable int size) {
        Page<Article> pageList = articleService.search(map, page, size);
        PageResult pageResult = new PageResult(pageList.getTotal(), pageList.getRecords());
        return new Result(true, StatusCode.OK, "分页查询成功!", pageResult);

    }


}