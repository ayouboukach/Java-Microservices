package com.learning.model.rest;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Ayoub
 *
 */
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequestModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6072652376924021126L;
	@Pattern(regexp = "^(?=.{4,20}$)(?![_.])(?!.*[_.]{2})[a-z._]+(?<![_.])$", message = "Firstname is content just caracteres alphabets and  must have between 4 and 20 with no spaces")
	private String firstname;
	@Pattern(regexp = "^(?=.{4,20}$)(?![_.])(?!.*[_.]{2})[a-z._]+(?<![_.])$", message = "Lastname is content just caracteres alphabets and  must have between 4 and 20 with no spaces")
	private String lastname;
	@Pattern(regexp = "^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-z._]+(?<![_.])$", message = "Username is content just caracteres alphabets and  must have between 8 and 20 with no spaces")
	private String username;
	@Pattern(regexp = "^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$", message = "Please provide your email correct")
	private String email;
}
