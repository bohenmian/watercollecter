package cn.edu.swpu.cins.watercollecter.entity;


import java.io.Serializable;

public class Message implements Serializable {

    private Long id;    //设备号

    private Long time;      //测试时间

    private Double temperature;     //温度

    private Double oldPh;       //原水PH

    private Double oldTurbid;   //原水浊度

    private Double newTurbid;   //出水浊度

    private Double water;   //取水量

    private Double consume; //PAC消耗


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getOldPh() {
        return oldPh;
    }

    public void setOldPh(Double oldPh) {
        this.oldPh = oldPh;
    }

    public Double getOldTurbid() {
        return oldTurbid;
    }

    public void setOldTurbid(Double oldTurbid) {
        this.oldTurbid = oldTurbid;
    }

    public Double getNewTurbid() {
        return newTurbid;
    }

    public void setNewTurbid(Double newTurbid) {
        this.newTurbid = newTurbid;
    }

    public Double getWater() {
        return water;
    }

    public void setWater(Double water) {
        this.water = water;
    }

    public Double getConsume() {
        return consume;
    }

    public void setConsume(Double consume) {
        this.consume = consume;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", time=" + time +
                ", temperature=" + temperature +
                ", oldPh=" + oldPh +
                ", oldTurbid=" + oldTurbid +
                ", newTurbid=" + newTurbid +
                ", water=" + water +
                ", consume=" + consume +
                '}';
    }
}
