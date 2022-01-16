package com.sp;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;

import com.sp.config.RedissonConfig;

@TestInstance(Lifecycle.PER_CLASS)
public class BaseTest {
	
	private RedissonConfig config = new RedissonConfig();
	protected RedissonClient client;
	protected RedissonReactiveClient rxclient;
	
	@BeforeAll
	public void init() {
		client = config.getClient();
		rxclient = config.getReactiveClient();
	}
	
	@AfterAll
	public void shutdown() {
		client.shutdown();
		rxclient.shutdown();
	}

	void sleep(int sleepSec) {
        try {
            TimeUnit.SECONDS.sleep(sleepSec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
