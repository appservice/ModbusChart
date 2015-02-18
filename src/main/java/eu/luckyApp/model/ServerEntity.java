package eu.luckyApp.model;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;

@Entity
public class ServerEntity {

	public ServerEntity() {

	}

	/*public ServerEntity(String name, String ip, int port, int timeInterval,
			String description) {
		// this.id = id;
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.timeInterval = timeInterval;
		this.description = description;
	}*/

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	private String ip;

	@Max(value = 65535)
	private int port;

	private int timeInterval;

	private int firstRegisterPos;

	// for example read 3 x float from following registers
	private int readedDataCount;
	
	//@Enumerated
	private String readedDataType;

	
	
	private String description;

	
	
	
	
	public Long getId() {
		return id;
	}





	public void setId(Long id) {
		this.id = id;
	}





	public String getName() {
		return name;
	}





	public void setName(String name) {
		this.name = name;
	}





	public String getIp() {
		return ip;
	}





	public void setIp(String ip) {
		this.ip = ip;
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





	public int getFirstRegisterPos() {
		return firstRegisterPos;
	}





	public void setFirstRegisterPos(int firstRegisterPos) {
		this.firstRegisterPos = firstRegisterPos;
	}





	public int getReadedDataCount() {
		return readedDataCount;
	}





	public void setReadedDataCount(int readedDataCount) {
		this.readedDataCount = readedDataCount;
	}





	public String getReadedDataType() {
		return readedDataType;
	}





	public void setReadedDataType(String readedDataType) {
		this.readedDataType = readedDataType;
	}





	public String getDescription() {
		return description;
	}





	public void setDescription(String description) {
		this.description = description;
	}





	@Override
	public String toString() {
		return "ServerEntity [id=" + id + ", resourceName=" + name
				+ ", ipAddress=" + ip + ", port=" + port + ", timeInterval="
				+ timeInterval + ", readRegister=" + firstRegisterPos
				+ ", description=" + description + "]";
	}

}
