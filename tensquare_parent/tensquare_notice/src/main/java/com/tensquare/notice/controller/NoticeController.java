package com.tensquare.notice.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.entity.PageResult;
import com.tensquare.entity.Result;
import com.tensquare.entity.StatusCode;
import com.tensquare.notice.pojo.Notice;
import com.tensquare.notice.pojo.NoticeFresh;
import com.tensquare.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 消息通知控制器
 */
@RestController
@RequestMapping(value = "/notice")
@CrossOrigin
public class NoticeController {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private NoticeService noticeService;

    /**
     * 根据id查询消息通知
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id){

       Notice notice =  noticeService.findById(id);

       return new Result(true, StatusCode.OK,"查询成功!",notice);

    }

    /**
     * 新增消息通知
     * @param notice
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Notice notice){

        noticeService.add(notice);

        return new Result(true, StatusCode.OK,"新增成功!");

    }


    /**
     * 分页查询消息通知
     * @param page size
     * @return
     */
    @RequestMapping(value = "findPage/{page}/{size}",method = RequestMethod.GET)
    public Result findPage(@PathVariable int page,@PathVariable int size){

        Page<Notice> noticePage = noticeService.findPage(page,size);

        PageResult<Notice> pageResult = new PageResult<>(noticePage.getTotal(),noticePage.getRecords());

        return new Result(true, StatusCode.OK,"分页查询成功!",pageResult);

    }
    /**
     * 修改通知
     * @param notice
     * @return
     */
    @RequestMapping(value = "{id}",method = RequestMethod.PUT)
    public Result update(@RequestBody Notice notice, @PathVariable String id){
        notice.setId(id);
        noticeService.updateById(notice);
        return new Result(true, StatusCode.OK,"修改成功!");

    }

    /**
     * 根据用户id查询该用户的待推送消息（新消息）
     */

    @RequestMapping(value = "/freshPage/{userId}/{page}/{size}",method = RequestMethod.GET)
    public Result freshPage(@PathVariable String userId,@PathVariable int page,@PathVariable int size){

        Page<NoticeFresh> noticeFreshPage = noticeService.freshPage(userId,page,size);

        PageResult<NoticeFresh> pageResult = new PageResult<NoticeFresh>(noticeFreshPage.getTotal(),noticeFreshPage.getRecords());

        return new Result(true, StatusCode.OK, "查询成功",pageResult);
    }

    /**
     * 删除待推送数据
     * @param noticeFresh
     * @return
     */
    @RequestMapping(value = "/fresh",method = RequestMethod.DELETE)
    public Result delete(@RequestBody NoticeFresh noticeFresh){

         noticeService.delete(noticeFresh);

        return new Result(true, StatusCode.OK,"删除成功!");

    }


}
