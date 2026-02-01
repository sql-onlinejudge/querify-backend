package me.suhyun.soj.global.infrastructure.cache

import com.fasterxml.jackson.databind.ObjectMapper
import me.suhyun.soj.global.infrastructure.cache.config.CacheProperties
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

@Service
class CacheService(
    private val redisTemplate: RedisTemplate<String, String>?,
    private val objectMapper: ObjectMapper,
    private val properties: CacheProperties
) {

    private data class CacheEntry(
        val value: Any,
        val expireAt: Instant?
    )

    private val localCache = ConcurrentHashMap<String, CacheEntry>()

    private val useRedis: Boolean
        get() = properties.provider == "redis" && redisTemplate != null

    fun <T : Any> get(key: String, type: Class<T>): T? {
        return if (useRedis) {
            getFromRedis(key, type)
        } else {
            getFromLocal(key, type)
        }
    }

    fun <T : Any> put(key: String, value: T, ttl: Duration = properties.ttl.default) {
        if (useRedis) {
            putToRedis(key, value, ttl)
        } else {
            putToLocal(key, value, ttl)
        }
    }

    fun evict(key: String) {
        if (useRedis) {
            redisTemplate?.delete(key)
        } else {
            localCache.remove(key)
        }
    }

    fun evictByPattern(pattern: String) {
        if (useRedis) {
            redisTemplate?.keys(pattern)?.let { keys ->
                if (keys.isNotEmpty()) {
                    redisTemplate.delete(keys)
                }
            }
        } else {
            val regex = pattern.replace("*", ".*").toRegex()
            localCache.keys.filter { regex.matches(it) }.forEach { localCache.remove(it) }
        }
    }

    private fun <T : Any> getFromRedis(key: String, type: Class<T>): T? {
        val value = redisTemplate?.opsForValue()?.get(key) ?: return null
        return objectMapper.readValue(value, type)
    }

    private fun <T : Any> putToRedis(key: String, value: T, ttl: Duration) {
        val json = objectMapper.writeValueAsString(value)
        redisTemplate?.opsForValue()?.set(key, json, ttl)
    }

    private fun <T : Any> getFromLocal(key: String, type: Class<T>): T? {
        val entry = localCache[key] ?: return null
        if (entry.expireAt != null && Instant.now().isAfter(entry.expireAt)) {
            localCache.remove(key)
            return null
        }
        @Suppress("UNCHECKED_CAST")
        return entry.value as? T
    }

    private fun <T : Any> putToLocal(key: String, value: T, ttl: Duration) {
        val expireAt = Instant.now().plusMillis(ttl.toMillis())
        localCache[key] = CacheEntry(value, expireAt)
    }
}

inline fun <reified T : Any> CacheService.get(key: String): T? = get(key, T::class.java)
