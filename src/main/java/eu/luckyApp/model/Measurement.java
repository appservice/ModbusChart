package eu.luckyApp.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "MEASUREMENT")
public class Measurement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6923668406252775414L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	/*
	 * @JsonIgnore
	 * 
	 * @ManyToOne()
	 * 
	 * @JoinColumn(name="server_id") private ServerEntity server;
	 */

	@ElementCollection(fetch = javax.persistence.FetchType.EAGER)
	@CollectionTable(name = "VALUE", joinColumns = { @JoinColumn(name = "ID", referencedColumnName = "id") })
	// @OneToMany(fetch=FetchType.EAGER)
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
				" |measured list: " + measuredValue;
	}

	/*
	 * public Measurement add(Measurement m){ Measurement mes=new Measurement();
	 * List<Double> values=m.getMeasuredValue(); //List<Double> newValues;
	 * 
	 * 
	 * int i=0; for(Double val:values){ values.set(i,
	 * val+this.getMeasuredValue().get(i)); }
	 * 
	 * mes.setDate(m.getDate());
	 * 
	 * return mes; }
	 */

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
		// while(m.measuredValue.size()<this.measuredValue.size()){
		// m.measuredValue.add(0.0);
		// }

		for (Double value : this.measuredValue) {
			this.getMeasuredValue().set(i, value + m.getMeasuredValue().get(i)/dyvider);/// dyvisor
			i++;
		}

		this.setDate(m.getDate());
	}

	/**
	 * 
	 * /** This method makes a "deep clone" of any Java object it is given.
	 */
	public Measurement deepClone() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (Measurement) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void clearValuesList() {

		for (int i = 0; i < measuredValue.size(); i++) {
			this.measuredValue.set(i, 0.0);
		}
	}
}
