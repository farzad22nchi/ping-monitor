package com.fto.monitor_tool.ping;

import static org.assertj.core.api.Assertions.assertThat;
import com.fto.monitor_tool.ping.model.Ping;
import com.fto.monitor_tool.ping.repository.PingRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class PingRepoTest {
    @Autowired
    private PingRepo repo;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testSavePing(){
        Ping ping = new Ping("www.test.com", LocalDateTime.now(), 123l, 200);
        final Ping actualPing = repo.save(ping);

        final Ping expectedPing = entityManager.find(Ping.class, actualPing.getId());
        assertThat(expectedPing.getTime()).isEqualTo(expectedPing.getTime());
    }

    @Test
    public void testCustomFinder(){
        final LocalDateTime timeFrom = LocalDateTime.now();
        Ping pCom = new Ping("www.test.com", LocalDateTime.now(), 123l, 200);
        Ping pOrg = new Ping("www.test.org", LocalDateTime.now(), 123l, 200);
        Ping pNet = new Ping("www.test.net", LocalDateTime.now(), 123l, 200);
        final List<Ping> actualPings = repo.saveAll(Arrays.asList(pCom, pOrg, pNet));
        final LocalDateTime timeTo= LocalDateTime.now();
        final List<Ping> expectedPings = repo.getResponseTime(timeFrom, timeTo);
        assertThat(expectedPings.size()==3);
        assertThat(actualPings).isEqualTo(expectedPings);
    }
}
