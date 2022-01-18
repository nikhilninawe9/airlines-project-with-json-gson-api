package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Vendor;

public interface VendorService {
	public Integer saveVendor(Vendor v);

	public List<Vendor> findAllVendors();

	public Vendor findOneVendor(Integer id);

	public void deleteVendor(Integer id);

	public void updateVendor(Vendor v);

	public void getDiscountedProductPriceWithVendorDiscount(int vDiscount);

}
