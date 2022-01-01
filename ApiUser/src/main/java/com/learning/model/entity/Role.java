package com.learning.model.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Role {

	private String id;
	private String name;
	private List<Privilege> privileges = new ArrayList<Privilege>();
}
