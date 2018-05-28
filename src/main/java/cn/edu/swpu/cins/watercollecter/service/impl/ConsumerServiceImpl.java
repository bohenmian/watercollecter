package cn.edu.swpu.cins.watercollecter.service.impl;

import cn.edu.swpu.cins.watercollecter.entity.Message;
import cn.edu.swpu.cins.watercollecter.service.ConsumerService;
import cn.edu.swpu.cins.watercollecter.service.InfluxdbService;
import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private InfluxdbService influxdbService;


    @Override
    @KafkaListener(topics = {"test"})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            //获取producer发送的message
            Message message = new Gson().fromJson(String.valueOf(kafkaMessage.get()), Message.class);
            log.info(message.toString());
            //将数据写入influxdb,measurement代表的是数据源中的数据表
            influxdbService.writeToInfluxdb(message, "measurement");
            //TODO convert message to Object
        }
    }
}
