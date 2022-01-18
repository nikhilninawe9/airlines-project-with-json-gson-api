package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Product;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	public Integer saveProduct(Product p) {
		p = productRepository.save(p);
		return p.getpId();
	}

	public List<Product> findAllProducts() {
		List<Product> list = productRepository.findAll();
		if (list.isEmpty()) {
			throw new ProductNotFoundException("Products not found!..");
		} else {
			return list;
		}
	}

	public Product findOneProduct(Integer id) {
		Optional<Product> p = productRepository.findById(id);
		if (p.isPresent())
			return p.get();
		else
			throw new ProductNotFoundException("Product not found!..");
	}

	public void deleteProduct(Integer id) {
		productRepository.delete(findOneProduct(id));
	}

	public void updateProduct(Product p) {
		if (p.getpId() != null && productRepository.existsById(p.getpId())) {
			productRepository.save(p);
		} else {
			throw new ProductNotFoundException("Product does not exists with id: " + p.getpId());
		}
	}

	@Transactional
	public int updateProductVendor1(String vendor, Integer id) {
		if (id != null && productRepository.existsById(id)) {
			return productRepository.updateProductVendor1(vendor, id);
		} else {
			throw new ProductNotFoundException("Product does not exists with id: " + id);
		}
	}

	@Transactional
	public int updateProductVendor(String vendor, Integer id) {
		if (id != null && productRepository.existsById(id)) {
			return productRepository.updateProductVendor(vendor, id);
		} else {
			throw new ProductNotFoundException("Product does not exists with id: " + id);
		}
	}

	@Override
	public void saveProducts(List<Product> p1) {
		productRepository.saveAll(p1);
	}

}
