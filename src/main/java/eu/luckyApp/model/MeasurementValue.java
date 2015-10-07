package eu.luckyApp.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

//@Entity
public class MeasurementValue implements Cloneable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonIgnore
	Long id;
	
	Double value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}


	
	
	

}
