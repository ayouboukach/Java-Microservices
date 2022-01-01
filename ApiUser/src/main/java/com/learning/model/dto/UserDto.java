package com.learning.model.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * @author Ayoub
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8565274258631187838L;
	private String userId;
	private String firstname;
	private String lastname;
	private String username;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	private String email;
	private String profileImageUrl;
	private Date lastLoginDate;
	private Date lastLoginDateDisplay;
	private Date joinDate;
	private String role;
	private String[] authorities;
	private boolean isActive;
	private boolean isNotLocked;
}
