package cn.edu.swpu.cins.watercollecter.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerService {

    void listen(ConsumerRecord<?, ?> record);
}
