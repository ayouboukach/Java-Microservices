package com.learning.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRemoved implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 743151126797193431L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false,updatable = false)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Long id;
	private Long idBefore;
	@Column(name = "userid")
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
	@Column(insertable = true, updatable = false)
	@CreationTimestamp
	private Date dateDelete;
	private String role;
	private String[] authorities;
//	@JoinColumn(name = "roleid")
//	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
//	private Role role;
	private boolean isActive;
	private boolean isNotLocked;
	
//	@PrePersist
//	 void onCreate() {
//	 this.setDateDelete(new Date());
//	 }
}
