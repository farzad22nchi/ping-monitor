package com.fto.monitor_tool.ping.service;

import com.fto.monitor_tool.ping.model.Ping;
import com.fto.monitor_tool.ping.repository.PingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.time.LocalDateTime;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class WebPingService {

    private static Logger log = Logger.getLogger(WebPingService.class.getName());
    static final long MAX_DURATION = 1000l;
    static final long MIN_INTERVAL = 1l;

    @Autowired
    PingRepo pingRepo;

    PingThread ping;

    public void startPing(String address, long durationMin, long intervalSec) {
        if (address == null || address.isBlank()) {
            log.log(Level.WARNING, "the url address is not valid");
            return;
        }
        if (durationMin > MAX_DURATION) {
            durationMin = MAX_DURATION;
            log.log(Level.WARNING, "the Duration time exceed set max default value");
        }
        if (intervalSec < MIN_INTERVAL){
            intervalSec = MIN_INTERVAL;
            log.log(Level.WARNING, "the Interval time is too less set min default value");
        }
        ping = new PingThread(address, durationMin, intervalSec);
        ping.start();
    }

    public void stopPing() {
        if(ping != null && !ping.isInterrupted()) ping.interrupt();
    }

    public Long getAverageTime(LocalDateTime start, LocalDateTime end){
        return pingRepo.getAverageTime(start, end).orElse(0l);
    }

    public List<Ping> getPings(LocalDateTime start, LocalDateTime end){
        return pingRepo.getResponseTime(start, end);
    }





    private class PingThread extends Thread {
        private Thread worker;
        private AtomicBoolean running = new AtomicBoolean(false);
        private final String address;
        private final Long duration;
        private final Long interval;

        public PingThread(String address, Long duration, Long interval) {
            this.address = address;
            this.duration = duration;
            this.interval = interval;
        }

        public void start() {
            running.set(true);
            worker = new Thread(this);
            worker.start();
        }

        public void interrupt() {
            running.set(false);
        }

        @Override
        public void run() {
            {
                int i = 0;
                final long start = System.currentTimeMillis();
                final long end = start+duration*60l*1000l;
                while (running.get() &&  System.currentTimeMillis()< end) {
                    runPing(address);
                    try {
                        Thread.sleep(interval * 1000l);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log.log(Level.WARNING,"Thread was interrupted, Failed to complete operation" + e);
                    }
                }
            }
        }

        private void runPing(String address) {
            StopWatch stopwatch = new StopWatch();
            RestTemplate restTemplate = new RestTemplate();
            stopwatch.start();
            final ResponseEntity<String> entity = restTemplate.getForEntity(address, String.class);
            stopwatch.stop();
            long timeMillis = stopwatch.getLastTaskTimeMillis();
            log.log(Level.INFO, "ping time: " + timeMillis);
            pingRepo.save(new Ping(address, LocalDateTime.now(), timeMillis, entity.getStatusCodeValue()));
        }
    }
}
