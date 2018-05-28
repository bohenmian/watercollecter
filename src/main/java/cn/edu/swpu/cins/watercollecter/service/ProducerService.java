package cn.edu.swpu.cins.watercollecter.service;

import cn.edu.swpu.cins.watercollecter.entity.Message;

public interface ProducerService {

    void send(Message message);

}
