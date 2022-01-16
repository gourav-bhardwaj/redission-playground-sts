package com.sp;

import org.junit.jupiter.api.RepeatedTest;
import org.redisson.api.RAtomicLongReactive;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class RedissionPlaygroundStsApplicationTests {
	
	@Autowired
	private ReactiveStringRedisTemplate template;
	
	@Autowired
	private RedissonReactiveClient client;

	@RepeatedTest(3)
	void contextLoads() {
		ReactiveValueOperations<String, String> oprVal = template.opsForValue();
		long st = System.currentTimeMillis();
		Mono<Void> mono = Flux.range(1, 300_000).flatMap(i -> {
			return oprVal.increment("num-incr");
		}).then();
		StepVerifier.create(mono).verifyComplete();
		long ed = System.currentTimeMillis();
		System.out.println("Computing time : %d".formatted(ed - st));
	}
	
	@RepeatedTest(3)
	void redissonClient() {
		RAtomicLongReactive numIncr = client.getAtomicLong("num-incr-1");
		long st = System.currentTimeMillis();
		Mono<Void> mono = Flux.range(1, 300_000).flatMap(i -> {
			return numIncr.incrementAndGet();
		}).then();
		StepVerifier.create(mono).verifyComplete();
		long ed = System.currentTimeMillis();
		System.out.println("Computing time : %d".formatted(ed - st));
	}

}
