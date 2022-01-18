package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Product;

public interface ProductService {
	public Integer saveProduct(Product p);

	public List<Product> findAllProducts();

	public Product findOneProduct(Integer id);

	public void deleteProduct(Integer id);

	public void updateProduct(Product p);

	public int updateProductVendor1(String vendor, Integer id);

	public int updateProductVendor(String vendor, Integer id);

	public void saveProducts(List<Product> p1);

}
