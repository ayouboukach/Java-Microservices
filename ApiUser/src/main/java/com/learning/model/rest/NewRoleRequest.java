package com.learning.model.rest;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewRoleRequest {
	
	@NotBlank
	private String rolename;
	@NotBlank
	private String description;

}
