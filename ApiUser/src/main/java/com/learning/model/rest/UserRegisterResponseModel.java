package com.learning.model.rest;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterResponseModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4203406699657420643L;
	private String userId;
	private String firstname;
	private String lastname;
	private String username;
	private String email;
	private Date joinDate;
	private String role;
	private boolean isActive;
	private boolean isNotLocked;
}
