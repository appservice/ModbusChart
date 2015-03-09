package eu.luckyApp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Entity
@Table(name = "MEASUREMENT")
public class Measurement {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@JsonIgnore
	@ManyToOne(cascade=CascadeType.REMOVE,targetEntity=ServerEntity.class)
	@JoinColumn(name="server_id")
	private ServerEntity server;

	@ElementCollection(fetch = javax.persistence.FetchType.EAGER)
	@CollectionTable(name = "VALUE", joinColumns = { @JoinColumn(name = "ID", referencedColumnName = "id") })
	@JsonProperty("values")
	private List<Double> measuredValue = new ArrayList<>();

	public ServerEntity getServer() {
		return server;
	}

	public void setServer(ServerEntity server) {
		this.server = server;
	}

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
		return "id: "+id+" "+" |date: "+date.toString()+" "+" |server : "+server+
				" |measured list: "+measuredValue;
	}

	
}
