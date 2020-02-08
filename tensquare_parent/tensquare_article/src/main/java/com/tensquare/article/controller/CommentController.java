package com.tensquare.article.controller;


import com.tensquare.article.pojo.Comment;
import com.tensquare.article.service.CommentService;
import com.tensquare.entity.Result;
import com.tensquare.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/comment")
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 新增评论
     * @param comment
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Comment comment) {
        commentService.add(comment);
        return new Result(true, StatusCode.OK, "评论新增成功!");

    }


    /**
     * 删除评论
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        commentService.deleteById(id);
        return new Result(true, StatusCode.OK, "评论删除成功!");

    }

    /**
     * 修改评论
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable String id, @RequestBody Comment comment) {
        comment.set_id(id);
        commentService.update(comment);
        return new Result(true, StatusCode.OK, "评论修改成功!");

    }

    /**
     * 查询所有评论
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        List<Comment> commentList = commentService.findAll();
        return new Result(true, StatusCode.OK, "成功查询所有评论!", commentList);

    }

    /**
     * 根据评论id查询评论
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        Comment comment = commentService.findById(id);
        return new Result(true, StatusCode.OK, "查询成功!", comment);

    }

    /**
     * 根据文章id查询评论
     * @param articleId
     * @return
     */
    @RequestMapping(value = "/article/{articleId}", method = RequestMethod.GET)
    public Result findByArticleId(@PathVariable String articleId) {
        List<Comment> commentList = commentService.findByArticleId(articleId);
        return new Result(true, StatusCode.OK, "查询成功!", commentList);

    }

    /**
     * 评论点赞
     * @param id
     * @return
     */
    @RequestMapping(value = "/thumbup/{id}", method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String id) {
        String userId = "123";
        Object value = redisTemplate.opsForValue().get(userId + "_" + id);
        if (value != null) {
            return new Result(false, StatusCode.ERROR, "不能重复点赞");
        }
        commentService.thumbup(id);
        redisTemplate.opsForValue().set(userId +"_"+id,"ok");
        return new Result(true, StatusCode.OK, "点赞成功!");

    }

}
