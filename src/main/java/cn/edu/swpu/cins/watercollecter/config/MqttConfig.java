package cn.edu.swpu.cins.watercollecter.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.transformer.HeaderEnricher;
import org.springframework.integration.transformer.support.ExpressionEvaluatingHeaderValueMessageProcessor;
import org.springframework.integration.transformer.support.HeaderValueMessageProcessor;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(MqttProperties.class)
public class MqttConfig {

    @Autowired
    private MqttProperties properties;

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel mqttMessageEnrichChannel() {
        return new DirectChannel();
    }

    /**
     * 配置MQTT的消息转换和是否采用异步执行
     * @return
     */
    @Bean
    public DefaultPahoMessageConverter messageConverter() {
        DefaultPahoMessageConverter messageConverter = new DefaultPahoMessageConverter();
        messageConverter.setPayloadAsBytes(true);
        return messageConverter;
    }

    /**
     * 配置Mqtt的连接,此处是通过工厂方法模式获取一个MqttPahoClientFactory的实例
     * @return
     */
    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory() {
        DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
        if (StringUtils.isNotBlank(properties.getUsername())) {
            clientFactory.setUserName(properties.getUsername());
        }
        if (StringUtils.isNotBlank(properties.getPassword())) {
            clientFactory.setPassword(properties.getPassword());
        }
        return clientFactory;
    }

    /**
     * MQTT producer 配置
     * @return
     */
    @Bean
    public MessageProducerSupport mqttInbound() {
        MqttPahoMessageDrivenChannelAdapter channelAdapter = new MqttPahoMessageDrivenChannelAdapter(properties.getBrokerURL(),
                properties.getClientId(), mqttPahoClientFactory(), properties.getTopicName());
        channelAdapter.setCompletionTimeout(5000);
        channelAdapter.setConverter(messageConverter());
        channelAdapter.setQos(2);
        channelAdapter.setOutputChannel(mqttInputChannel());
        return channelAdapter;
    }

    /**
     * MQTT中的信息转换成Kafka中的消息格式
     * @return
     */
    @Bean
    @Transformer(inputChannel = "mqttMessageEnrichChannel", outputChannel = "mqttInputChannel")
    public HeaderEnricher enrichHeaders() {
        Map<String, HeaderValueMessageProcessor<?>> headersToAdd = new HashMap<>();
        Expression expression = new SpelExpressionParser().parseExpression("headers." + MqttHeaders.TOPIC + ".toString().replaceAll(\'/\', \'.\')");
        headersToAdd.put(KafkaHeaders.TOPIC,
                new ExpressionEvaluatingHeaderValueMessageProcessor<>(expression, String.class));
        HeaderEnricher enricher = new HeaderEnricher(headersToAdd);
        return enricher;
        //TODO
    }

    @Transformer(inputChannel = "mqttInputChannel", outputChannel = "mqttMessageEnrichChannel")
    public Message<String> enrichMessage(Message<String> message) throws JsonProcessingException {
        Map<String, String> jsonMap = new LinkedHashMap<>();
        jsonMap.put("payload", message.getPayload());
        jsonMap.put("timestamp", String.valueOf(new Date().getTime()));
        return MessageBuilder.createMessage(objectMapper().writeValueAsString(jsonMap), message.getHeaders());
    }

    //message handle,消息处理的具体函数,从MQTT写入Kafka的具体实现逻辑
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MqttMessageHandler();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
