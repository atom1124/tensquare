package com.tensquare.notice.client;


import com.tensquare.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 消息通知微服务调用用户微服务获取用户昵称
 */
@FeignClient("tensquare-user")
public interface UserClient {
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public Result findById(@PathVariable String userId);



}
