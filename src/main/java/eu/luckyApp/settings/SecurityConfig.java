package eu.luckyApp.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	//@Value(value="user.password")
	//public String userPassword="start123";



	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.inMemoryAuthentication()
				.withUser("user").password("start123").roles("USER")
				.and()
				.withUser("admin").password("admin123").roles("ADMIN","USER"); //"USER", 
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {


		http.csrf().disable()

				.authorizeRequests()
				.antMatchers("/js/**").permitAll()			 
				.antMatchers(HttpMethod.DELETE,"/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.POST,"/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.PUT,"/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET,"/view/admin/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET,"/*", "/user/**").hasRole("USER")
				.anyRequest().authenticated()
				.and().formLogin().loginPage("/login.html").permitAll().loginProcessingUrl("/j_spring_security_check")
				.failureUrl("/login.html?login_error=true").passwordParameter("j_password")
				.usernameParameter("j_username")
				.and().rememberMe().tokenValiditySeconds(7*24*3600).key("uniqueAndSecret")
				//.and().httpBasic()
				.and().logout().permitAll().logoutUrl("/logout");
		
		
				
				

	}

}
