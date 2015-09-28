package eu.luckyApp.settings.security;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;



@Entity
@Proxy(lazy = false)
@Table(name="USER")
//@XmlRootElement
public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;






	/**
	 * @return the roles
	 */
	public Set<Role> getRoles() {
		
		return roles;
	}
	/**
	 * @param roles the roles to set
	 */
	public void setRoles(Set<Role> roles) {
	
		this.roles = roles;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(unique=true,nullable=false)
	private String name;
	

	private String password;
	
	private boolean isEnabled;
	

	
	
	@ElementCollection(targetClass=Role.class,fetch=FetchType.EAGER)
	@Enumerated(value=EnumType.STRING)
	private Set<Role> roles=new HashSet<>();

	
	
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getUserName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setUserName(String userName) {
		this.name = userName;
	}
	/**
	 * @return the password
	 *////	
	@JsonIgnore
	public String getUserPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	@JsonProperty
	public void setUserPassword(String userPassword) {
		this.password = userPassword;
	}
	/**
	 * @return the isEnabled
	 */
	public boolean isEnabled() {
		return isEnabled;
	}
	/**
	 * @param isEnabled the isEnabled to set
	 */
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	


}
