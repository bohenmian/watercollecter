package cn.edu.swpu.cins.watercollecter.service.impl;

import cn.edu.swpu.cins.watercollecter.entity.Message;
import cn.edu.swpu.cins.watercollecter.service.ProducerService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class ProducerServiceImpl implements ProducerService {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private KafkaTemplate<String, String> template;

    private Gson gson = new GsonBuilder().create();

    @Override
    public void send(Message message) {
        log.info("+++++++++++++++++++++  message = {}", gson.toJson(message));
        template.send("test", gson.toJson(message));
    }
}
