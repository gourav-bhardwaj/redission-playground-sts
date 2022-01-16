package com.sp;

import java.time.Duration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBlockingDequeReactive;
import org.redisson.client.codec.LongCodec;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec11MessageQueue extends BaseTest {
	
	private RBlockingDequeReactive<Long> msgQueue;
	
	@BeforeAll
	void setup() {
		msgQueue = this.rxclient.getBlockingDeque("msgQueue-1", LongCodec.INSTANCE);
	}
	
	@Test
	void consumer1() {
		Mono<Void> mono = msgQueue.takeElements()
				.doOnNext(i -> System.out.println("Conumer : 1 -> " + i))
				.doOnError(System.out::println).then();
		StepVerifier.create(mono).verifyComplete();
		sleep(300);
	}
	
	@Test
	void consumer2() {
		Mono<Void> mono = msgQueue.takeElements()
				.doOnNext(i -> System.out.println("Conumer : 2 -> " + i))
				.doOnError(System.out::println).then();
		StepVerifier.create(mono).verifyComplete();
		sleep(300);
	}

	@Test
	void producer() {
		Mono<Void> mono = Flux.range(1, 100)
				.delayElements(Duration.ofSeconds(1))
				.doOnNext(System.out::println)
				.flatMap(i -> msgQueue.add(Long.valueOf(i)))
				.then();
		StepVerifier.create(mono).verifyComplete();
	}

}
