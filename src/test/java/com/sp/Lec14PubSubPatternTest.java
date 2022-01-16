package com.sp;

import org.junit.jupiter.api.Test;
import org.redisson.api.RPatternTopicReactive;
import org.redisson.api.RTopicReactive;
import org.redisson.api.listener.PatternMessageListener;
import org.redisson.client.codec.StringCodec;

public class Lec14PubSubPatternTest extends BaseTest {

	@Test
	public void subscriberOne() {
		RTopicReactive topic = this.rxclient.getTopic("topic-1", StringCodec.INSTANCE);
		topic.getMessages(String.class)
		.doOnNext(m -> System.out.println("Subscriber 1 : --> " + m)).subscribe();
		sleep(300);
	}
	
	@Test
	public void patternSubscriber() {
		RPatternTopicReactive pattern_topic = this.rxclient.getPatternTopic("topic-*", StringCodec.INSTANCE);
		pattern_topic.addListener(String.class, new PatternMessageListener<String>() {
			@Override
			public void onMessage(CharSequence pattern, CharSequence channel, String msg) {
				System.out.println(pattern + " : " + channel + " : " + msg);
			}
		}).subscribe();
		sleep(300);
	}
	
}
