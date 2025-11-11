package com.LakeSide.LakeSide.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder //for jwt 
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount implements UserDetails{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String fullName;
	
	@Column
	private String email;
	
	@Column
	private String password;
	
	@Column
	private Boolean isLoggedIn;
	
	@Column
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@Transient
	private String token; // Token should not be persisted in database

	public Boolean getIsLoggedIn() {
		return isLoggedIn;
	}

	public void setIsLoggedIn(Boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public UserAccount() {
		super();
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public enum Role{
		USER,
		ADMIN,
		OWNER
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Convert role to Spring Security authority (ROLE_ prefix is required)
		if (role == null) {
			return Collections.emptyList();
		}
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}

	@Override
	public String getUsername() {
		return getEmail();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true; // Account never expires
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; // Account is never locked
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // Credentials never expire
	}

	@Override
	public boolean isEnabled() {
		return isLoggedIn != null && isLoggedIn; // Account is enabled if logged in
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
