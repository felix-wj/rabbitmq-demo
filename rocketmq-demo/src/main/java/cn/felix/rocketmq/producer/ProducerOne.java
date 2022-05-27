package cn.felix.rocketmq.producer;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProducerOne {
	@Autowired
	private RocketMQTemplate rocketMQTemplate;

	public void sendMsg(String msg){
		rocketMQTemplate.convertAndSend("springboot-rocketmq:test", "hello springboot mq "+msg);
	}
}
