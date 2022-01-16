package com.sp.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Student {
	
	private String name;
    private Integer age;
    private String city;
    private List<Integer> marks;

}
