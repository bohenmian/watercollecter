package cn.edu.swpu.cins.watercollecter.controller;

import cn.edu.swpu.cins.watercollecter.entity.Message;
import cn.edu.swpu.cins.watercollecter.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("event")
public class KafkaController {

    @Autowired
    private ProducerService producerService;

    @PostMapping("/send")
    public String send() {
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            Message message = new Message();
            message.setId((long) random.nextInt(1000000));
            message.setTime(System.currentTimeMillis());
            message.setTemperature(random.nextDouble() * 100000);
            message.setOldPh(random.nextDouble() * 10);
            message.setNewTurbid(random.nextDouble() * 100);
            message.setOldTurbid(random.nextDouble() * 10);
            message.setWater(random.nextDouble() * 100000);
            message.setConsume(random.nextDouble() * 1000);
            producerService.send(message);
        }
        return "send message success";
    }


}
