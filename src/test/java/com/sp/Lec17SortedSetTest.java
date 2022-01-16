package com.sp;

import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.client.codec.StringCodec;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec17SortedSetTest extends BaseTest {
	
	@Test
	public void sortedSet() {
		RScoredSortedSetReactive<String> sortedSet = this.rxclient.getScoredSortedSet("product-rated", StringCodec.INSTANCE);
		Mono<Void> mono = sortedSet.addScore("Lenovo i5 laptop", 12.90)
		.then(sortedSet.add(23.67, "HP laptop i5"))
		.then(sortedSet.addScore("OnePlus", 7)).then();
		StepVerifier.create(mono).verifyComplete();
		
		sortedSet.entryRange(0, 1)
		.flatMapIterable(t -> t)
		.doOnNext(t -> System.out.println("Score : " + t.getScore() + ", Value: " + t.getValue()))
		.subscribe();
	}

}
