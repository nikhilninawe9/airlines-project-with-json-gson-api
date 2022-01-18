package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Component
@JsonInclude(Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AirTickets {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer airTktId;
	private String startingPoint;
	private String destination;
	private Double tktPrice;
	private Double discountOnTkt;
	private Double finalTktPrice;

	public AirTickets() {
		super();
	}

	public AirTickets(Integer airTktId, String startingPoint, String destination, Double tktPrice, Double discountOnTkt,
			Double finalTktPrice) {
		super();
		this.airTktId = airTktId;
		this.startingPoint = startingPoint;
		this.destination = destination;
		this.tktPrice = tktPrice;
		this.discountOnTkt = discountOnTkt;
		this.finalTktPrice = finalTktPrice;
	}

	public Integer getAirTktId() {
		return airTktId;
	}

	public String getStartingPoint() {
		return startingPoint;
	}

	public String getDestination() {
		return destination;
	}

	public Double getTktPrice() {
		return tktPrice;
	}

	public Double getDiscountOnTkt() {
		return discountOnTkt;
	}

	public Double getFinalTktPrice() {
		return finalTktPrice;
	}

	@Override
	public String toString() {
		return "AirTickets [airTktId=" + airTktId + ", startingPoint=" + startingPoint + ", destination=" + destination
				+ ", tktPrice=" + tktPrice + ", discountOnTkt=" + discountOnTkt + ", finalTktPrice=" + finalTktPrice
				+ "]";
	}

}
