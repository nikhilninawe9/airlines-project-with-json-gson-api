package com.example.demo.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Component
@JsonInclude(Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Vendor {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "vid")
	private Integer vId;
	@Column(name = "vname")
	private String vName;
	@Column(name = "vaddress")
	private String vAddress;
	@Column(name = "vcode")
	private String vCode;
	@Column(name = "vdiscount")
	private Integer vDiscount;

	@JsonManagedReference
	@OneToMany(targetEntity = Product.class, mappedBy = "vendor", cascade = {
			CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = false)
	private List<Product> products;

	@JsonBackReference
	@ManyToOne(cascade = { CascadeType.ALL })
	private Continents continents;

	@OneToOne(mappedBy = "vendor")
	private Airline airline;

	public Vendor() {
		super();
	}

	public Vendor(Integer vId, String vName, String vAddress, String vCode, Integer vDiscount, List<Product> products,
			Continents continents) {
		super();
		this.vId = vId;
		this.vName = vName;
		this.vAddress = vAddress;
		this.vCode = vCode;
		this.vDiscount = vDiscount;
		this.products = products;
		this.continents = continents;
	}

	public Integer getvId() {
		return vId;
	}

	public void setvId(Integer vId) {
		this.vId = vId;
	}

	public String getvName() {
		return vName;
	}

	public void setvName(String vName) {
		this.vName = vName;
	}

	public String getvAddress() {
		return vAddress;
	}

	public void setvAddress(String vAddress) {
		this.vAddress = vAddress;
	}

	public String getvCode() {
		return vCode;
	}

	public void setvCode(String vCode) {
		this.vCode = vCode;
	}

	public Integer getvDiscount() {
		return vDiscount;
	}

	public void setvDiscount(Integer vDiscount) {
		this.vDiscount = vDiscount;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Continents getContinents() {
		return continents;
	}

	public void setContinents(Continents continents) {
		this.continents = continents;
	}

	@Override
	public String toString() {
		return "Vendor [vId=" + vId + ", vName=" + vName + ", vAddress=" + vAddress + ", vCode=" + vCode
				+ ", vDiscount=" + vDiscount + ", products=" + products + ", continents=" + continents + "]";
	}

}
