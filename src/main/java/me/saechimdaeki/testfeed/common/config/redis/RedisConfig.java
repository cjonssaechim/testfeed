package me.saechimdaeki.testfeed.common.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import me.saechimdaeki.testfeed.feed.dto.FeedEvent;
import me.saechimdaeki.testfeed.feed.service.response.FeedVo;

@Configuration
public class RedisConfig {

	@Value("${spring.data.redis.host:localhost}")
	private String host;

	@Value("${spring.data.redis.port:6379}")
	private int port;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}

	@Bean
	public RedisTemplate<String, Long> redisTemplateForLong(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Long> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		template.setKeySerializer(new StringRedisSerializer());

		template.setValueSerializer(new GenericToStringSerializer<>(Long.class));

		template.afterPropertiesSet();
		return template;
	}

	@Bean
	public RedisTemplate<String, FeedVo> feedEventRedisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, FeedVo> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		StringRedisSerializer stringSerializer = new StringRedisSerializer();
		template.setKeySerializer(stringSerializer);
		template.setHashKeySerializer(stringSerializer);

		Jackson2JsonRedisSerializer<FeedEvent> jacksonSerializer = new Jackson2JsonRedisSerializer<>(new ObjectMapper().registerModule(new JavaTimeModule()),
			FeedEvent.class);
		template.setValueSerializer(jacksonSerializer);
		template.setHashValueSerializer(jacksonSerializer);

		template.afterPropertiesSet();
		return template;
	}
}
