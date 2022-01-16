package com.sp;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.LocalCachedMapOptions.ReconnectionStrategy;
import org.redisson.api.LocalCachedMapOptions.SyncStrategy;
import org.redisson.api.RLocalCachedMap;
import org.redisson.codec.TypedJsonJacksonCodec;

import com.sp.dto.Student;

import reactor.core.publisher.Flux;

public class Loc08LocalMapCachedTest extends BaseTest {

	
	private RLocalCachedMap<Integer, Student> cacheMap;
	
	@BeforeAll
	public void mapInit() {
		TypedJsonJacksonCodec codec = new TypedJsonJacksonCodec(Integer.class, Student.class);
		LocalCachedMapOptions<Integer, Student> options = LocalCachedMapOptions.<Integer, Student>defaults()
				.maxIdle(10, TimeUnit.SECONDS)
				.syncStrategy(SyncStrategy.UPDATE)
				.reconnectionStrategy(ReconnectionStrategy.LOAD);
		this.cacheMap = client.getLocalCachedMap("lstdmcache", codec, options);
	}
	
	@Test
	void localCachedMapTest() {
		Map<Integer, Student> mapObj = Map.ofEntries(
					Map.entry(101, new Student("Gourav", 45, "Gujrat", List.of(23,45,66))),
					Map.entry(102, new Student("Aniket", 22, "Goa", List.of(11, 56)))
				);
		this.cacheMap.putAll(mapObj);
		Flux.interval(Duration.ofSeconds(1)).doOnNext(i -> {
			System.out.println(i + " : " + this.cacheMap.getCachedMap());
		}).subscribe();
		sleep(60);
	}
	
	@Test
	void localCachedMapUpdate() {
		this.cacheMap.put(101, new Student("Vinod sharma", 12, "jaipur", List.of(45,66)));
	}
}
