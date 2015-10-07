package eu.luckyApp.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import eu.luckyApp.settings.security.CustomUserDetailsService;
import eu.luckyApp.settings.security.Role;
import eu.luckyApp.settings.security.UserRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = false, jsr250Enabled = true, proxyTargetClass = true, securedEnabled = false)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value(value = "${user.login}")
	public String userLogin;

	@Value(value = "${user.password}")
	public String userPassword;

	@Value(value = "${admin.login}")
	public String adminLogin;

	@Value(value = "${admin.password}")
	public String adminPassword;
	
	//@Value("#{Integer.parseInt(${remember_token_valid_time});}")
	public Integer rememberTokenValidTime=604800;

	@Autowired
	CustomUserDetailsService customUserDatailsService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		if ((userRepository.findUsersByRole(Role.ROLE_ADMIN)).isEmpty()) {
			auth.inMemoryAuthentication().withUser(userLogin).password(userPassword).roles("USER").and()
					.withUser(adminLogin).password(adminPassword).roles("ADMIN", "USER"); // "USER",
		} else {

			auth.userDetailsService(customUserDatailsService);
		}
	

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable()// .sessionManagement().disable()

				.authorizeRequests().antMatchers("/js/**", "**/favicon.ico", "/css/**", "/images/**", "/errors/**")
				.permitAll().antMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN").antMatchers(HttpMethod.POST, "/**")
				.hasRole("ADMIN").antMatchers(HttpMethod.PUT, "/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET, "/view/admin/**").hasRole("ADMIN").antMatchers(HttpMethod.GET, "/user/**")
				.hasRole("USER").anyRequest().authenticated()

				.and().formLogin().loginPage("/login.html").permitAll().loginProcessingUrl("/j_spring_security_check")
				.failureUrl("/login.html?login_error=true").passwordParameter("j_password")
				.usernameParameter("j_username")
				.and().rememberMe().tokenValiditySeconds(rememberTokenValidTime).key("uniqueAndSecret")
				// .and().httpBasic()
				.and().logout().permitAll().logoutUrl("/logout")
				.and().exceptionHandling().authenticationEntryPoint(new AlwaysSendUnauthorized401AuthenticationEntryPoint())
			//	.and().requiresChannel().anyRequest().requiresSecure()
			//	.and().headers().httpStrictTransportSecurity()
			;

	}

	// @Bean
	// BCryptPasswordEncoder passwordEncoder(){
	// return new BCryptPasswordEncoder();
	// }
}
