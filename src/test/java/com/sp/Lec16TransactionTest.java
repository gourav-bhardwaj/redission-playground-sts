package com.sp;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.api.RTransactionReactive;
import org.redisson.api.TransactionOptions;
import org.redisson.client.codec.LongCodec;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
public class Lec16TransactionTest extends BaseTest {
	
	public RBucketReactive<Long> user1;
	public RBucketReactive<Long> user2;
	
	@AfterAll
	public void end() {
		Mono.zip(this.user1.get(), this.user2.get())
				.doOnNext(System.out::println)
				.subscribe();
	}

	@Test
	void transactionTest() {
		RTransactionReactive rtransaction = this.rxclient.createTransaction(TransactionOptions.defaults());
		this.user1 = rtransaction.getBucket("user:1:balance", LongCodec.INSTANCE);
		this.user2 = rtransaction.getBucket("user:2:balance", LongCodec.INSTANCE);
		Mono<Void> mono = this.user1.set(100l).then(this.user2.set(0l)).then();
		StepVerifier.create(mono).verifyComplete();
		transfer(user1, user2, 20)
		.thenReturn(0).map(i -> 5/i)
		.doOnNext(val -> {
			log.info("Successfully completed");
			rtransaction.commit();
		}).doOnError(ex -> {
			log.error("Error : {}", ex.getMessage());
			rtransaction.rollback();
		})
		.subscribe();
		sleep(10);
	}
	
	private Mono<Void> transfer(RBucketReactive<Long> user1, RBucketReactive<Long> user2, int amount) {
		return Mono.zip(user1.get(), user2.get())
		.filter(t -> t.getT1() >= amount)
		.flatMap(t -> user1.set(t.getT1() - amount).thenReturn(t))
		.flatMap(t -> user2.set(t.getT2() + amount))
		.then();
	}
	
}
