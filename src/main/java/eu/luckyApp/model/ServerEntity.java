package eu.luckyApp.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Max;

@Entity
@Table(name = "SERVER_ENTITY")
public class ServerEntity {

	public ServerEntity() {

	}

	@Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	private String ip;

	@Max(value = 65535)
	private int port;

	private long timeInterval;

	private int firstRegisterPos;



	// @Enumerated
	private String readedDataType;
	
	private int savedMeasurementNumber;
	
	private double scaleFactor;
	
	
	private double scaleFactorForElectricEnergy;
	
	
	/**
	 * @return the scaleFactorForElectricEnergy
	 */
	public double getScaleFactorForElectricEnergy() {
		return scaleFactorForElectricEnergy;
	}

	/**
	 * @param scaleFactorForElectricEnergy the scaleFactorForElectricEnergy to set
	 */
	public void setScaleFactorForElectricEnergy(double scaleFactorForElectricEnergy) {
		this.scaleFactorForElectricEnergy = scaleFactorForElectricEnergy;
	}

	@ElementCollection(fetch=FetchType.EAGER)	
	@CollectionTable(name="SENSOR_NAME",joinColumns={@JoinColumn(name="ID",referencedColumnName="id")})
	private List<String>sensorsName=new ArrayList<>();

	////@JsonIgnore
///	@OneToMany(/*mappedBy = "server",*/ fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST/*,CascadeType.REMOVE*/},orphanRemoval=true)
	// {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}
	// ,orphanRemoval=true
	// CascadeType.ALLCascadeType.MERGE,CascadeType.REMOVE,CascadeType.REFRESH}
//	private Collection<Measurement> measurements = new ArrayList<>();

	/**
	 * @return the sensorsName
	 */
	public List<String> getSensorsName() {
		return sensorsName;
	}

	/**
	 * @param sensorsName the sensorsName to set
	 */
	public void setSensorsName(List<String> sensorsName) {
		this.sensorsName = sensorsName;
	}

	
	
	public double getScaleFactor() {
		return scaleFactor;
	}

	public void setScaleFactor(double scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	private String description;

/*	public Collection<Measurement> getMeasurements() {
		return measurements;
	}*/

/*	public void setMeasurements(Collection<Measurement> measurements) {
		this.measurements = measurements;
	}*/

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

	public long getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(long timeInterval) {
		this.timeInterval = timeInterval;
	}

	public int getFirstRegisterPos() {
		return firstRegisterPos;
	}

	public void setFirstRegisterPos(int firstRegisterPos) {
		this.firstRegisterPos = firstRegisterPos;
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
				+ ", description=" + description + ", scaleFactor"+scaleFactor+"]";
	}

	public int getSavedMeasurementNumber() {
		return savedMeasurementNumber;
	}

	public void setSavedMeasurementNumber(int savedMeasurementNumber) {
		this.savedMeasurementNumber = savedMeasurementNumber;
	}

}
