package eu.luckyApp.settings.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUser extends User implements UserDetails {

	public SecurityUser(User user) {
		if(user!=null){
			this.setUserName(user.getUserName());
			this.setId(user.getId());
			this.setRoles(user.getRoles());
			this.setUserPassword(user.getUserPassword());
			this.setEnabled(user.isEnabled());
		}
		
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority>authorities =new ArrayList<>();
		Set<Role>roles=this.getRoles();
		if(roles!=null){
			for(Role role:roles){
				SimpleGrantedAuthority authority=new SimpleGrantedAuthority(role.name());
				authorities.add(authority);
			}
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return super.getUserPassword();
	}

	@Override
	public String getUsername() {
		return super.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

}
