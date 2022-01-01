package com.learning.security;

import static java.util.Arrays.stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.learning.model.entity.User;

/**
 * @author Ayoub
 *
 */
public class UserPrincipal implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;

	public UserPrincipal(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return stream(getAuthorities(this.user)).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	String[] getAuthorities(User user) {
		List<String> authorities = new ArrayList<String>();
		if (user.getRoles() != null) {
			for (int i = 0; i < user.getRoles().getPrivileges().size(); i++) {
				authorities.add(i, user.getRoles().getPrivileges().get(i).getName());
			}
		}

		return authorities.toArray(new String[0]);
	}

	public String getUserId() {
		return this.user.getUserId();
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.user.isNotLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.user.isActive();
	}
}
