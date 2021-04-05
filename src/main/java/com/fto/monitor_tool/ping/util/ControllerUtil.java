package com.fto.monitor_tool.ping.util;

import com.fto.monitor_tool.ping.service.WebPingService;
import org.apache.commons.validator.routines.TimeValidator;
import org.apache.commons.validator.routines.UrlValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ControllerUtil {
    private static final Logger log = Logger.getLogger(ControllerUtil.class.getName());
    private static final String TIME_PATTERN = "dd.MM.yyyy HH:mm:ss";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN);

    static final int MIN_INTERVAL = 1;
    static final int MAX_DURATION = 60;

    public LocalDateTime getDateTime(String time) {
        //TODO NEED TO USE A BETTER WAY TO CHECK TIME VALIDATION
        if(!isTimeValid(time)){
            log.log(Level.WARNING, "wrong time format set it to default");
            return LocalDateTime.now();
        }
        return LocalDateTime.parse(time, dateTimeFormatter);
    }

    private boolean isTimeValid(String timeFrom) {
        return timeFrom.matches("\\d{1,2}.\\d{1,2}.\\d{4}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}");
    }


    public String getAddress(String webUrl) {
        if(!isUrlValid(webUrl)) {
            log.log(Level.WARNING, "wrong webUrl link format, it sets to default http://google.com");
            webUrl = "http://google.com";
        }
        return webUrl;
    }

    private boolean isUrlValid(String address) {
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(address);
    }
    public Integer getTimeDuration(String duration){
        return duration == null
                || duration.isBlank()
                ? MAX_DURATION : Integer.parseInt(duration);
    }
    public Integer getTimeInterval(String interval){
        return interval == null
                || interval.isBlank()
                ? MIN_INTERVAL : Integer.parseInt(interval);
    }
}
