package cn.felix.rocketmq.controller;

import cn.felix.rocketmq.producer.ProducerOne;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @program: mq-demo
 * @description:
 * @author: WangJie
 * @create: 2018-07-25 11:58
 **/
@RestController
@Slf4j
public class RabbitMqController {
    @Autowired
    private ProducerOne producerOne;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("/send-mq/{message}")
    public String sendMq(@PathVariable("message") String message){
        String content="Date:"+new Date()+":"+message;
        producerOne.sendMsg(content);
        return content;
    }

    @GetMapping("/send-mq/orderly/{message}/{key}")
    public String sendMqOrderly(@PathVariable("message") String message,@PathVariable("key") String key){
        String content="Date:"+new Date()+":"+message;
        SendResult sendResult = rocketMQTemplate.syncSendOrderly("springboot-rocketmq:test", "hello springboot mq " + content, key);
        System.out.println(JSONObject.toJSONString(sendResult));
        return content;
    }

    @GetMapping("/send-mq/transaction")
    public String sendMqOrderly(){
        for (int i = 0; i < 10; i++) {
            try {
                Message msg = MessageBuilder.withPayload("rocketMQTemplate transactional message " + i).
                        setHeader(RocketMQHeaders.TRANSACTION_ID, "KEY_" + i).build();
                SendResult sendResult = rocketMQTemplate.sendMessageInTransaction("tx-producer-group","springboot-rocketmq:test", msg, null);
                System.out.printf("------rocketMQTemplate send Transactional msg body = %s , sendResult=%s %n",
                        msg.getPayload(), sendResult.getSendStatus());

                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "success";
    }


}
