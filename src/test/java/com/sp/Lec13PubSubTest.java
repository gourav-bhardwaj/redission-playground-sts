package com.sp;

import org.junit.jupiter.api.Test;
import org.redisson.api.RTopicReactive;
import org.redisson.client.codec.StringCodec;

public class Lec13PubSubTest extends BaseTest {
	
	@Test
	void subscribeOne() {
		RTopicReactive topic = this.rxclient.getTopic("topic-1", StringCodec.INSTANCE);
		topic.getMessages(String.class)
		.doOnNext(m -> System.out.println("Subscribe : 1 --> " + m))
		.subscribe();
		sleep(300);
	}
	
	@Test
	void subscribeSecond() {
		RTopicReactive topic = this.rxclient.getTopic("topic-1", StringCodec.INSTANCE);
		topic.getMessages(String.class)
		.doOnNext(m -> System.out.println("Subscribe : 2 --> " + m))
		.subscribe();
		sleep(300);
	}

}
