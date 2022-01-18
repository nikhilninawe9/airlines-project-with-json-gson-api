package com.example.demo.utility;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

@Entity
@Component
public class Cities {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer cityId;
	private String cities;

	public Cities(String cities) {
		super();
		this.cities = cities;
	}

	public Cities() {
		super();
	}

	public Integer getCityId() {
		return cityId;
	}

	public String getCities() {
		return cities;
	}

	@Override
	public String toString() {
		return "Cities [cityId=" + cityId + ", cities=" + cities + "]";
	}
}
