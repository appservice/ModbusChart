package eu.luckyApp.model;

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



@Entity
@Table(name="MEASUREMENT")
public class Measurement {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	/*@ManyToOne//(fetch=javax.persistence.FetchType.LAZY)
	@JoinColumn(name="server_id", referencedColumnName="id")
	private ServerEntity server;*/
	
	private long serverId ;
	
	@ElementCollection(fetch=javax.persistence.FetchType.EAGER)
	@CollectionTable(name="VALUE", 
						joinColumns={@JoinColumn(name="ID",referencedColumnName="id")})
	
	private List<Float> measuredValue=new ArrayList<>();

	
	
	/*public ServerEntity getServer() {
		return server;
	}

	public void setServer(ServerEntity server) {
		this.server = server;
	}*/

	public long getServerId() {
		return serverId;
	}

	public void setServerId(long serverId) {
		this.serverId = serverId;
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

	public List<Float> getMeasuredValue() {
		return measuredValue;
	}

	public void setMeasuredValue(List<Float> measuredValue) {
		this.measuredValue = measuredValue;
	}
	
	

}
