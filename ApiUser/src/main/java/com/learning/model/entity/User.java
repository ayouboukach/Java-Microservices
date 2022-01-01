package com.learning.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Ayoub
 *
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
//@Table(name="user")
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Long id;
	@Column(name = "userid", unique = true)
	private String userId;
	@NotNull
	@Column(nullable = false)
	private String firstname;
	@NotNull
	@Column(nullable = false)
	private String lastname;
	@NotNull
	@Column(nullable = false)
	private String username;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	@NotNull
	@Column(nullable = false)
	private String email;
//	@ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
//	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "userid", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "roleid", referencedColumnName = "id"))
//	@JsonBackReference
////	@Json
	@JsonProperty(access = Access.WRITE_ONLY)
	private String roleid;
	@Transient
	private Role roles;
	@NotNull
	@NotBlank(message = "Uploading your profile image failure")
	private String profileImageUrl;
	private Date lastLoginDate;
	private Date lastLoginDateDisplay;
	private Date joinDate;
	private String role; // ROLE_USER{ read, edit }, ROLE_ADMIN {delete}
	private String[] authorities;
	private boolean isActive;
	private boolean isNotLocked;
}