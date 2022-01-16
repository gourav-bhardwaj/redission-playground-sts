package com.sp;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;
import org.redisson.api.RHyperLogLogReactive;
import org.redisson.client.codec.LongCodec;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec12HyperLogLogTest extends BaseTest {
	
	@Test
	public void hyperLogLogTest() {
		 RHyperLogLogReactive<Long> views = this.rxclient.getHyperLogLog("views-count", LongCodec.INSTANCE);
		
		 List<Long> list1 = LongStream.rangeClosed(1, 4000).map(Long::valueOf).boxed().collect(Collectors.toList());
		 
		 List<Long> list2 = LongStream.rangeClosed(10000, 56000).map(Long::valueOf).boxed().collect(Collectors.toList());
		 
		 List<Long> list3 = LongStream.rangeClosed(6700, 89000).map(Long::valueOf).boxed().collect(Collectors.toList());
		 
		 List<Long> list4 = LongStream.rangeClosed(23000, 100_000).boxed().collect(Collectors.toList());
		 
		 Mono<Void> mono = Flux.just(list1, list2, list3, list4)
				 .flatMap(views::addAll)
				 .then();
		 
		 StepVerifier.create(mono).verifyComplete();
		 
		 views.count().doOnNext(c -> {
			 System.out.println("-------------------------------------" + c + "---------------------------------------------");
		 }).subscribe();
		 
	}

}
