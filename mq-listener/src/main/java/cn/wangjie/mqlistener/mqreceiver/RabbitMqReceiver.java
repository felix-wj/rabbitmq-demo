package cn.wangjie.mqlistener.mqreceiver;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @program: mq-demo
 * @description:
 * @author: WangJie
 * @create: 2018-07-25 12:02
 **/


@Component

public class RabbitMqReceiver {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "demo-queue", durable = "true"),
            exchange = @Exchange(name = "demo-exchange", durable = "true", type = "topic"),
            key = "demo-queue"
        )
    )
    @RabbitHandler
    public void receiver(@Payload String msg , @Headers Map<String , Object> headers , Channel channel, CorrelationData correlationData) throws IOException {
        System.out.println("消费queueTest队列消息:" + msg);
        Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        System.out.println(correlationData.toString());
        System.out.println(headers.get("id").toString());
        channel.basicAck(deliveryTag,false);
    }
}
