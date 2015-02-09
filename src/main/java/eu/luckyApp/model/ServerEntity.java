package eu.luckyApp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;

@Entity
public class ServerEntity {
	
	
	
	public ServerEntity() {

	}
	
	
	
	public ServerEntity( String resourceName, String ipAddress,
			int port, int timeInterval, String description) {
		//this.id = id;
		this.resourceName = resourceName;
		this.ipAddress = ipAddress;
		this.port = port;
		this.timeInterval = timeInterval;
		this.description = description;
	}



	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String resourceName;
	
	private String ipAddress;
	
	@Max(value=65535)
	private int port;
	
	
	private int timeInterval;
	
	private int readRegister;
	
	public int getReadRegister() {
		return readRegister;
	}



	public void setReadRegister(int readRegister) {
		this.readRegister = readRegister;
	}



	private String description;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(int timeInterval) {
		this.timeInterval = timeInterval;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}



	@Override
	public String toString() {
		return "ServerEntity [id=" + id + ", resourceName=" + resourceName
				+ ", ipAddress=" + ipAddress + ", port=" + port
				+ ", timeInterval=" + timeInterval + ", readRegister="
				+ readRegister + ", description=" + description + "]";
	}



	
	







	

	
}
