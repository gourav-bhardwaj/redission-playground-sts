package com.sp;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;
import org.redisson.api.RListReactive;
import org.redisson.client.codec.LongCodec;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec09ListTest extends BaseTest {

	@Test
	void redisListNotInSequence() {
		RListReactive<Long> numbers = this.rxclient.getList("numbers-1", LongCodec.INSTANCE);
		Mono<Void> mono = Flux.range(1, 10).map(Long::valueOf)
		.flatMap(numbers::add)
		.then();
		StepVerifier.create(mono).verifyComplete();
		StepVerifier.create(numbers.size()).expectNext(10).verifyComplete();
	}
	
	@Test
	void redisListSequence() {
		RListReactive<Long> numbers = this.rxclient.getList("numbers-2", LongCodec.INSTANCE);
		List<Long> list = LongStream.rangeClosed(1, 10).map(Long::valueOf).boxed().collect(Collectors.toList());
		StepVerifier.create(numbers.addAll(list).then()).verifyComplete();
		StepVerifier.create(numbers.size()).expectNext(10).verifyComplete();
	}
	
}
