package eu.luckyApp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Entity
//@Table(name = "MEASUREMENT")
public class Measurement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6923668406252775414L;

/*	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore*/
	private Long id;

/*	@Temporal(TemporalType.TIMESTAMP)*/
	private Date date;

	private Double energyConsumption;
	
	

/*	@ElementCollection(fetch = javax.persistence.FetchType.EAGER)
	@CollectionTable(name = "VALUE", joinColumns = { @JoinColumn(name = "ID", referencedColumnName = "id") })
	// @OneToMany(fetch=FetchType.EAGER)*/
	// @JoinTable(name="VALUE",joinColumns{@JoinColumn(name="MES_ID",referencedColumnName="id")})
	@JsonProperty("values")
	private List<Double> measuredValue = new ArrayList<>();

	/*
	 * public ServerEntity getServer() { return server; }
	 * 
	 * public void setServer(ServerEntity server) { this.server = server; }
	 */

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the energyConsumption
	 */
	public Double getEnergyConsumption() {
		return energyConsumption;
	}

	/**
	 * @param energyConsumption the energyConsumption to set
	 */
	public void setEnergyConsumption(Double energyConsumption) {
		this.energyConsumption = energyConsumption;
	}
	
	public List<Double> getMeasuredValue() {
		return measuredValue;
	}

	public void setMeasuredValue(List<Double> measuredValue) {
		this.measuredValue = measuredValue;
	}

	@Override
	public String toString() {
		return "id: " + id + " " + " |date: " + date.toString() + " "
				+ /* " |server : "+server+ */
				" |measured list: " + measuredValue+ " |energyConsumtion: "+energyConsumption ;
	}


	public void add(Measurement m) {
		int i = 0;
		// Collections.

		while (this.measuredValue.size() < m.measuredValue.size()) {
			this.measuredValue.add(0.0);
		}

		while (this.measuredValue.size() > m.measuredValue.size()) {
			this.measuredValue.remove(this.measuredValue.size() - 1);
		}
		// while(m.measuredValue.size()<this.measuredValue.size()){
		// m.measuredValue.add(0.0);
		// }

		for (Double value : this.measuredValue) {
			this.getMeasuredValue().set(i, value + m.getMeasuredValue().get(i));/// dyvisor
			i++;
		}
		this.setEnergyConsumption(m.getEnergyConsumption());
		this.setDate(m.getDate());
	}

	
	public void addAndCalculatePerHour(Measurement m,double dyvider) {
		int i = 0;
		// Collections.

		while (this.measuredValue.size() < m.measuredValue.size()) {
			this.measuredValue.add(0.0);
		}

		while (this.measuredValue.size() > m.measuredValue.size()) {
			this.measuredValue.remove(this.measuredValue.size() - 1);
		}


		for (Double value : this.measuredValue) {
			this.getMeasuredValue().set(i, value + m.getMeasuredValue().get(i)/dyvider);/// dyvisor
			i++;
		}

		this.setDate(m.getDate());
		this.setEnergyConsumption(m.energyConsumption);
	}


	public void clearValuesList() {

		for (int i = 0; i < measuredValue.size(); i++) {
			this.measuredValue.set(i, 0.0);
		}
	}
}
