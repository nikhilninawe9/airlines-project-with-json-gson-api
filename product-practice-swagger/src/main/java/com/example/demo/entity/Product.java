package com.example.demo.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Component
@JsonInclude(Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pid")
	private Integer pId;
	@Column(name = "pcode")
	private String pCode;
	@Column(name = "pcost")
	private Double pCost;
	@Column(name = "pvendor")
	private String pVendor;

	@JsonBackReference
	@ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private Vendor vendor;

	public Product() {
		super();
	}

	public Product(Integer pId, String pCode, Double pCost, String pVendor) {
		super();
		this.pId = pId;
		this.pCode = pCode;
		this.pCost = pCost;
		this.pVendor = pVendor;
	}

	public Integer getpId() {
		return pId;
	}

	public void setpId(Integer pId) {
		this.pId = pId;
	}

	public String getpCode() {
		return pCode;
	}

	public void setpCode(String pCode) {
		this.pCode = pCode;
	}

	public Double getpCost() {
		return pCost;
	}

	public void setpCost(Double pCost) {
		this.pCost = pCost;
	}

	public String getpVendor() {
		return pVendor;
	}

	public void setpVendor(String pVendor) {
		this.pVendor = pVendor;
	}

	@Override
	public String toString() {
		return "Product [pId=" + pId + ", pCode=" + pCode + ", pCost=" + pCost + ", pVendor=" + pVendor + "]";
	}

}
