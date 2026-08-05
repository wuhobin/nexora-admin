package com.nexora.config;

import com.nexora.security.session.OnlineSessionRecord;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class RedisConfigTest {

    @Test
    void redisTemplateRoundTripsOnlineSessionWithJavaTime() {
        RedisTemplate<String, Object> redisTemplate = new RedisConfig()
                .redisTemplate(mock(RedisConnectionFactory.class));
        OnlineSessionRecord record = new OnlineSessionRecord(
                "550e8400-e29b-41d4-a716-446655440000",
                7,
                "user@example.com",
                "User",
                "127.0.0.1",
                "Chrome",
                "Windows",
                LocalDateTime.of(2026, 8, 5, 13, 30, 45));

        @SuppressWarnings("unchecked")
        RedisSerializer<Object> serializer =
                (RedisSerializer<Object>) redisTemplate.getValueSerializer();
        Object restored = serializer.deserialize(serializer.serialize(record));

        assertThat(restored).isEqualTo(record);
        assertThat(redisTemplate.getKeySerializer())
                .isInstanceOf(StringRedisSerializer.class);
        assertThat(redisTemplate.getHashValueSerializer()).isSameAs(serializer);
    }
}
