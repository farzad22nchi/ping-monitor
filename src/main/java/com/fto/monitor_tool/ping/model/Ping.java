package com.fto.monitor_tool.ping.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@XmlRootElement(name = "pings")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "pings")
public class Ping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "web_address", nullable = false, length = 1000)
    private String webAddress;
    private LocalDateTime time;
    @Column(name= "response_time")
    private Long responseTime;
    private int code;

    public Ping(String webAddress, LocalDateTime time, Long responseTime, int code) {
        this.webAddress = webAddress;
        this.time = time;
        this.responseTime = responseTime;
        this.code = code;
    }

    public Ping() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Ping{" +
                "id=" + id +
                ", webAddress='" + webAddress + '\'' +
                ", time=" + time +
                ", responseTime=" + responseTime +
                ", code=" + code +
                '}';
    }
    public String[] toListString(){
        return new String[]{id.toString(), webAddress.toString(), time.toString(), responseTime.toString(), Integer.toString(code)};
    }
}
