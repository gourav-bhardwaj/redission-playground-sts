package com.sp;

import org.junit.jupiter.api.Test;
import org.redisson.api.RDequeReactive;
import org.redisson.api.RQueueReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec10QueueAndStackTest extends BaseTest {
	
	@Test
	void redisQueue() {
		RQueueReactive<Long> numbers = this.rxclient.getQueue("numbers-2", LongCodec.INSTANCE);
		Mono<Void> mono = numbers.poll().repeat(3).then();
		StepVerifier.create(mono).verifyComplete();
		StepVerifier.create(numbers.size()).expectNext(6).verifyComplete();
	}
	
	@Test
	void redisStack() {
		RDequeReactive<Long> numbers = this.rxclient.getDeque("numbers-2", LongCodec.INSTANCE);
		Mono<Void> mono = numbers.pollLast().repeat(3).doOnNext(System.out::println).then();
		StepVerifier.create(mono).verifyComplete();
		StepVerifier.create(numbers.size()).expectNext(2).verifyComplete();
	}

}
