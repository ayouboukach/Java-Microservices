package com.learning.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Person {
	
	@Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	@Column(unique = true)
	 private String name;
	 
}
