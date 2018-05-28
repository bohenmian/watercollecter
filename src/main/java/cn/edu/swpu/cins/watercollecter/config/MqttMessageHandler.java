package cn.edu.swpu.cins.watercollecter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class MqttMessageHandler implements MessageHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private KafkaTemplate<String, String> template;


    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String kafkaTopic = message.getHeaders().get(KafkaHeaders.TOPIC, String.class);
        //将接收到的消息发送给Kafka
        ListenableFuture future = template.send(kafkaTopic, (String) message.getPayload());
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        logger.info(String.valueOf(message.getPayload()));
    }
}
