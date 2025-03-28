package me.saechimdaeki.testfeed.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.GenericContainer;

@TestConfiguration
public class RedisTestContainerConfig {

    private static final GenericContainer<?> redisContainer;

    static {
        redisContainer = new GenericContainer<>("redis:7.0.5")
                .withExposedPorts(6379);
        redisContainer.start();
    }
}