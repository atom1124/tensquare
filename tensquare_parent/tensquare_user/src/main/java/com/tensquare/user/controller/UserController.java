package com.tensquare.user.controller;


import com.tensquare.entity.Result;
import com.tensquare.entity.StatusCode;
import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 控制器
 */
@RestController
@RequestMapping(value = "/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public Result findById(@PathVariable String userId) {
        User user = userService.findById(userId);
        return new Result(true, StatusCode.OK, "查询成功", user);
    }

}
