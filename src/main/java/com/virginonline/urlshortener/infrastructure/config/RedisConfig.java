package com.virginonline.urlshortener.infrastructure.config;

import com.virginonline.urlshortener.domain.model.LinkInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class RedisConfig {
  @Bean
  public ReactiveRedisTemplate<String, LinkInfo> reactiveRedisTemplate(
      ReactiveRedisConnectionFactory factory) {
    Jackson2JsonRedisSerializer<LinkInfo> serializer =
        new Jackson2JsonRedisSerializer<>(LinkInfo.class);
    RedisSerializationContext.RedisSerializationContextBuilder<String, LinkInfo> builder =
        RedisSerializationContext.newSerializationContext(
            new Jackson2JsonRedisSerializer<>(String.class));
    RedisSerializationContext<String, LinkInfo> context = builder.value(serializer).build();
    return new ReactiveRedisTemplate<>(factory, context);
  }
}
