package cn.edu.swpu.cins.watercollecter.service.impl;

import cn.edu.swpu.cins.watercollecter.config.InfluxdbProperties;
import cn.edu.swpu.cins.watercollecter.entity.Message;
import cn.edu.swpu.cins.watercollecter.service.InfluxdbService;
import org.influxdb.InfluxDB;

import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class InfluxdbServiceImpl implements InfluxdbService {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private InfluxdbProperties properties;

    //指定名字的bean来加载
    @Autowired
    @Qualifier(value = "influxBean")
    private InfluxDB influxDB;

    private BatchPoints batchPoints;


    @Override
    public int writeToInfluxdb(Message message, String measurement) {
        Point point = Point.measurement(measurement).time(message.getTime(), TimeUnit.MILLISECONDS)
                .addField("id", message.getId())
                .addField("temperature", message.getTemperature())
                .addField("oldPh", message.getOldPh())
                .addField("oldTurbid", message.getOldTurbid())
                .addField("newTurbid", message.getNewTurbid())
                .addField("water", message.getWater())
                .addField("consume", message.getConsume())
                .build();
        batchPoints = BatchPoints.database(properties.getDbName())
                .retentionPolicy(properties.getRetention())
                .consistency(InfluxDB.ConsistencyLevel.ALL)
                .build();
        batchPoints.point(point);
        influxDB.write(batchPoints);
        return 0;
    }

}
