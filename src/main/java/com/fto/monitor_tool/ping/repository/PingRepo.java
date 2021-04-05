package com.fto.monitor_tool.ping.repository;

import com.fto.monitor_tool.ping.model.Ping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.time.LocalDateTime;

@Repository
public interface PingRepo extends JpaRepository<Ping, Long> {

    @Query(value = "SELECT AVG(response_time) FROM pings p WHERE p.time > ?1 AND p.time < ?2", nativeQuery = true)
    Optional<Long> getAverageTime(LocalDateTime start, LocalDateTime end);

    @Query("SELECT p FROM Ping p WHERE p.time > ?1 AND p.time < ?2")
    List<Ping> getResponseTime(LocalDateTime start, LocalDateTime end);
}
