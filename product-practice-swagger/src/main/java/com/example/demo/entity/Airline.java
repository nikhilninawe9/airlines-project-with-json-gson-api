package com.example.demo.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Component
@JsonInclude(Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Airline {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "aid")
	private Integer airlineId;
	@Column(name = "acode")
	private String airlineCode;
	@Column(name = "aname")
	private String airlineName;
	@Column(name = "aowner")
	private String airlineOwnerName;

	@JsonBackReference
	@ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private Continents continent;

	@OneToOne(cascade = CascadeType.ALL)
	private Vendor vendor;

	public Airline() {
		super();
	}

	public Airline(Integer airlineId, String airlineCode, String airlineName, String airlineOwnerName) {
		super();
		this.airlineId = airlineId;
		this.airlineCode = airlineCode;
		this.airlineName = airlineName;
		this.airlineOwnerName = airlineOwnerName;
	}

	public Integer getAirlineId() {
		return airlineId;
	}

	public void setAirlineId(Integer airlineId) {
		this.airlineId = airlineId;
	}

	public String getAirlineCode() {
		return airlineCode;
	}

	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
	}

	public String getAirlineName() {
		return airlineName;
	}

	public void setAirlineName(String airlineName) {
		this.airlineName = airlineName;
	}

	public String getAirlineOwnerName() {
		return airlineOwnerName;
	}

	public void setAirlineOwnerName(String airlineOwnerName) {
		this.airlineOwnerName = airlineOwnerName;
	}

	@Override
	public String toString() {
		return "Airline [airlineId=" + airlineId + ", airlineCode=" + airlineCode + ", airlineName=" + airlineName
				+ ", airlineOwnerName=" + airlineOwnerName + "]";
	}

}
