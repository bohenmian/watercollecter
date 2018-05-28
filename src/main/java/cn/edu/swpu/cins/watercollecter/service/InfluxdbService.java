package cn.edu.swpu.cins.watercollecter.service;

import cn.edu.swpu.cins.watercollecter.entity.Message;

public interface InfluxdbService {

    int writeToInfluxdb(Message message, String measurement);

}
