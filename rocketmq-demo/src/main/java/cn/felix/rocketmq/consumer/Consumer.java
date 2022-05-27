package cn.felix.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class Consumer {



	@Component
	@RocketMQMessageListener(consumerGroup = "group1", topic = "springboot-rocketmq" ,selectorExpression = "test")
	class ReceiverOne implements RocketMQListener<String> , RocketMQPushConsumerLifecycleListener {
		@Override
		public void onMessage(String msg) {
			System.out.println("group1-1 消费消息:"+msg);
		}

		@Override
		public void prepareStart(DefaultMQPushConsumer consumer) {
			//设置消费次数
			consumer.setMaxReconsumeTimes(3);
		}
	}
	@Component
	@RocketMQMessageListener(consumerGroup = "group1", topic = "springboot-rocketmq",selectorExpression = "test")
	class ReceiverOne1 implements RocketMQListener<String>{
		@Override
		public void onMessage(String msg) {
			System.out.println("group1-2 消费消息:"+msg);
		}
	}

	@Component
	@RocketMQMessageListener(consumerGroup = "group2", topic = "springboot-rocketmq",selectorExpression = "test")
	class ReceiverTwo implements RocketMQListener<String>{
		@Override
		public void onMessage(String msg) {
			System.out.println("group2 消费消息:"+msg);
		}
	}

	@Component
	@RocketMQMessageListener(consumerGroup = "group3", topic = "springboot-rocketmq",selectorExpression = "test3")
	class ReceiverThree implements RocketMQListener<String>{
		@Override
		public void onMessage(String msg) {
			System.out.println("group3 消费消息:"+msg);
		}
	}
}
