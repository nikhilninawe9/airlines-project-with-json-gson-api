package com.example.demo.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Component
@JsonInclude(Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Continents {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cid")
	private Integer continentId;
	@Column(name = "cname")
	private String continentName;
	@Column(name = "clocation")
	private String continentLocation;

	@JsonManagedReference
	@OneToMany(targetEntity = Vendor.class, cascade = {
			CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = false)
	private List<Vendor> vendors;

	@JsonManagedReference
	@OneToMany(targetEntity = Airline.class, cascade = {
			CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = false)
	private List<Airline> airlines;

	public Continents() {
		super();
	}

	public Continents(Integer continentId, String continentName, String continentLocation, List<Vendor> vendors,
			List<Airline> airlines) {
		super();
		this.continentId = continentId;
		this.continentName = continentName;
		this.continentLocation = continentLocation;
		this.vendors = vendors;
		this.airlines = airlines;
	}

	public Integer getContinentId() {
		return continentId;
	}

	public String getContinentName() {
		return continentName;
	}

	public String getContinentLocation() {
		return continentLocation;
	}

	public List<Vendor> getVendors() {
		return vendors;
	}

	public List<Airline> getAirlines() {
		return airlines;
	}

	@Override
	public String toString() {
		return "Continents [continentId=" + continentId + ", continentName=" + continentName + ", continentLocation="
				+ continentLocation + ", vendors=" + vendors + ", airlines=" + airlines + "]";
	}

}
