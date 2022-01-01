package com.learning.model.rest;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequestModel {

	
	@Length(min = 8, message = "*Your username must have at least 8 characters")
	@NotBlank(message ="*Please provide your username" )
	private String username;
	@Length(min = 10, message = "*Your password must have at least 10 characters")
	@NotBlank(message ="*Please provide your password" )
	private String password;

}
