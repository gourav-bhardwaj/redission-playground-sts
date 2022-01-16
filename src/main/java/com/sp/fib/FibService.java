package com.sp.fib;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class FibService {
	
	@Cacheable(value="math-fib", key = "#index")
	public int fibSeries(int index) {
		return fibonnaci(index);
	}
	
	@CacheEvict(value="math-fib", key="#index")
	public void cacheClear(int index) {
		System.out.println("------------Cache Clear--------------");
	}
	
	private int fibonnaci(int index) {
		if(index < 2)
			return index;
		return fibonnaci(index - 1) + fibonnaci(index - 2);
	}

}
