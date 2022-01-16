package com.sp.fib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FibController {
	
	@Autowired
	private FibService service;
	
	@GetMapping("/fib/{index}")
	public Integer getFibSeries(@PathVariable int index) {
		return service.fibSeries(index);
	}

}
