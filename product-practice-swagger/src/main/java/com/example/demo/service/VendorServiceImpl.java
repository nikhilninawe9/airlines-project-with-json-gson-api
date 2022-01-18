package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Vendor;
import com.example.demo.exception.VendorNotFoundException;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.VendorRepository;

@Service
public class VendorServiceImpl implements VendorService {

	@Autowired
	private VendorRepository vendorRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Integer saveVendor(Vendor v) {
		if (v != null) {
			v = vendorRepository.save(v);
			return 1;
		} else {
			return 0;
		}

	}

	@Override
	public List<Vendor> findAllVendors() {
		List<Vendor> vlist = vendorRepository.findAll();
		if (vlist.isEmpty()) {
			throw new VendorNotFoundException("No vendors in database!..");
		} else {
			return vlist;
		}
	}

	@Override
	public Vendor findOneVendor(Integer id) {
		Optional<Vendor> v = vendorRepository.findById(id);
		if (v.isPresent()) {
			return v.get();
		} else {
			throw new VendorNotFoundException("Vendor not found!..");
		}
	}

	@Override
	public void deleteVendor(Integer id) {
		if (findOneVendor(id) != null) {
			vendorRepository.deleteById(id);
		} else {
			throw new VendorNotFoundException("Vendor not present with id: " + id);
		}

	}

	@Override
	public void updateVendor(Vendor v) {
		if (v.getvId() != null && vendorRepository.existsById(v.getvId())) {
			vendorRepository.save(v);
		} else {
			throw new VendorNotFoundException("Vendor not present with id: " + v.getvId());
		}

	}

	@Override
	public void getDiscountedProductPriceWithVendorDiscount(int vDiscount) {
		// TODO Auto-generated method stub

	}

}
