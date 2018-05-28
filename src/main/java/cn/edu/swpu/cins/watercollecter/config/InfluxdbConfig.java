package cn.edu.swpu.cins.watercollecter.config;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(InfluxdbProperties.class)
public class InfluxdbConfig {

    @Autowired
    private InfluxdbProperties properties;

    @Bean(name = "influxBean")
    public InfluxDB influxDB() {
        return InfluxDBFactory.connect(properties.getUrl(), properties.getUsername(), properties.getPassword());
    }

}
