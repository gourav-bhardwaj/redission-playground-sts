package com.sp;

import org.junit.jupiter.api.Test;
import org.redisson.api.BatchOptions;
import org.redisson.api.RBatchReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RSetReactive;
import org.redisson.client.codec.LongCodec;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec15BatchTest extends BaseTest {

	@Test
	public void batchRedisTest() {
		RBatchReactive batch = this.rxclient.createBatch(BatchOptions.defaults());
		RListReactive<Long> list = batch.getList("numbers-list", LongCodec.INSTANCE);
		RSetReactive<Long> set = batch.getSet("numbers-set", LongCodec.INSTANCE);
		for (long i = 0; i < 200_000; i++) {
			list.add(i);
			set.add(i);
		}
		StepVerifier.create(batch.execute().then()).verifyComplete();
	}
	
	@Test
	public void regularRedisTest() {
		RListReactive<Long> list = this.rxclient.getList("numbers-list", LongCodec.INSTANCE);
		RSetReactive<Long> set = this.rxclient.getSet("numbers-set", LongCodec.INSTANCE);
		Mono<Void> mono = Flux.range(1, 200_000).map(Long::valueOf).flatMap(i -> {
			return list.add(i).then(set.add(i));
		}).then();
		StepVerifier.create(mono).verifyComplete();
	}
	
}
