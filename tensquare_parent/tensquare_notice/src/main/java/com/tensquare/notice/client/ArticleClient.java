package com.tensquare.notice.client;

import com.tensquare.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 消息通知微服务调用文章微服务获取文章标题
 */
@FeignClient("tensquare-article")
public interface ArticleClient {
    @RequestMapping(value = "/article/findById/{id}")
    public Result findById(@PathVariable String id);
}
