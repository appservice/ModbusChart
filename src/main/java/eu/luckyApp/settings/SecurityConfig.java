package eu.luckyApp.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
		auth.inMemoryAuthentication().withUser("user").password("start123")
				.roles("USER").and().withUser("admin").password("admin123")
				.roles("USER", "ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {


		http.csrf().disable()

				.authorizeRequests()
				//.anyRequest().permitAll()
				.antMatchers("/**")
				/*.anyRequest()*/.hasRole("USER")
				.and().httpBasic().and()
			//	.formLogin().loginPage("/login.html").loginProcessingUrl("/j_spring_security_check.action").permitAll()
			//	.and()
				.logout().permitAll().logoutUrl("/logout").logoutSuccessUrl("/")
				//.and().rememberMe()
				.and().exceptionHandling().accessDeniedPage("/401");//.
		
				
				

	}

}
