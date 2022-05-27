package cn.wangjie.mqproducer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMqController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;

    }

    @GetMapping("/send-rabbitmq/{message}")
    public String sendRabbitMq(@PathVariable("message") String message){
        String content="Date:"+new Date()+":"+message;
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        //消息唯一ID
        CorrelationData correlationData = new CorrelationData("消息id");
        rabbitTemplate.convertAndSend("demo-exchange","demo-routing-key", content, correlationData);
        return content;
    }
    final RabbitTemplate.ConfirmCallback confirmCallback = (correlationData, ack, cause) -> {
        System.out.println("correlationData: " + correlationData);
        String messageId = correlationData.getId();
        if(ack){
            //如果confirm返回成功 则进行更新
            System.out.println(messageId);
        } else {
            System.err.println("异常处理...");
        }
    };
    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
            System.out.println("returnCallback: ");
            System.out.println(message.toString());
            System.out.println(replyCode);
            System.out.println(replyText);
        }
    };

}
