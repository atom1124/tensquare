package com.tensquare.notice.linstener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.tensquare.entity.Result;
import com.tensquare.entity.StatusCode;
import com.tensquare.notice.netty.MyWebSocketHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import java.util.HashMap;

/**
 * 用户在线状态下 主动推送消息
 * @author wangxin
 * @version 1.0
 */
public class UserNoticeListener implements ChannelAwareMessageListener{
    public static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        //1.获取消息队列名称
        String queueName = message.getMessageProperties().getConsumerQueue();
        //2.获取当前用户的通道
        String userId = queueName.substring(queueName.lastIndexOf("_")+1);
        io.netty.channel.Channel channel1 = MyWebSocketHandler.map.get(userId);

        //3.判断用户是否在线
        if(channel1 != null){
            HashMap countMap = new HashMap();
            countMap.put("userNoticeCount",1);
            Result result = new Result(true, StatusCode.OK,"推送数据成功",countMap);
            //4.发送消息 数据通过websocket连接主动推送页面
            channel1.writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString(result)));
        }
    }
}
