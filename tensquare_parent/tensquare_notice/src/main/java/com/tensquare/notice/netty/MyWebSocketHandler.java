package com.tensquare.notice.netty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tensquare.entity.Result;
import com.tensquare.entity.StatusCode;
import com.tensquare.notice.config.ApplicationContextProvider;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息推送-首次登录会进入此类 服务端主动推送数据到页面
 * @author wangxin
 * @version 1.0
 */
public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    
    private ObjectMapper objectMapper = new ObjectMapper();

    public  static ConcurrentHashMap<String,Channel> map = new ConcurrentHashMap<>();//将连接存入map中
    ///消息队列模板
    RabbitTemplate rabbitTemplate = ApplicationContextProvider.getApplicationContext().getBean(RabbitTemplate.class);

    private static SimpleMessageListenerContainer simpleMessageListenerContainer = ApplicationContextProvider.getApplicationContext().getBean("sysNoticeContainer",SimpleMessageListenerContainer.class);

    private static SimpleMessageListenerContainer userMessageListenerContainer = ApplicationContextProvider.getApplicationContext().getBean("userNoticeContainer",SimpleMessageListenerContainer.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        //1.获取消息队列模板对象
        //2.通过数据通道读取数据  { "userId":"1" }
        String text = textWebSocketFrame.text();
        String userId = objectMapper.readTree(text).get("userId").asText();
        //3.首次建立连接
        Channel channel = map.get(userId);
        if(channel == null){
            channel = channelHandlerContext.channel();
            map.put(userId,channel);
        }
        //4.从消息队列中获取当前用户的消息
        //RabbitAdmin rabbitAdmin = ApplicationContextProvider.getApplicationContext().getBean("rabbitAdmin", RabbitAdmin.class);
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate);
        String queueName = "article_subscribe_"+userId;
        String userQueueName = "article_thumbup_"+userId;
        Properties queueProperties = rabbitAdmin.getQueueProperties(queueName);
        Properties userQueueProperties = rabbitAdmin.getQueueProperties(userQueueName);
        int noticeCount = 0;
        if(queueProperties != null){
            noticeCount  =(int)queueProperties.get(RabbitAdmin.QUEUE_MESSAGE_COUNT);//消息队列数量
        }

        int userNoticeCount = 0;
        if(userQueueProperties != null){
            userNoticeCount  =(int)userQueueProperties.get(RabbitAdmin.QUEUE_MESSAGE_COUNT);//消息队列数量
        }
        //5.把消息队列中消息数量推送用户
        HashMap countMap = new HashMap();
        countMap.put("sysNoticeCount",noticeCount);
        countMap.put("userNoticeCount",userNoticeCount);
        Result result = new Result(true, StatusCode.OK,"推送数据成功",countMap);
        channel.writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString(result)));
        //6.消息从消息队列清空
        if(noticeCount>0){
            rabbitAdmin.purgeQueue(queueName,true);
        }
        if(userNoticeCount>0){
            rabbitAdmin.purgeQueue(userQueueName,true);
        }
        //7.设置当前用户对应的消息队列监听（后续新消息 ，通过监听推送给用户）
        simpleMessageListenerContainer.addQueueNames(queueName);
        userMessageListenerContainer.addQueueNames(userQueueName);
    }
}
