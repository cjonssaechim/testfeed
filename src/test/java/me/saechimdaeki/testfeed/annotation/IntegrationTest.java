package me.saechimdaeki.testfeed.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import me.saechimdaeki.testfeed.config.KafkaTestContainerConfig;
import me.saechimdaeki.testfeed.config.RedisTestContainerConfig;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest
@Import({KafkaTestContainerConfig.class, RedisTestContainerConfig.class})
public @interface IntegrationTest {

}