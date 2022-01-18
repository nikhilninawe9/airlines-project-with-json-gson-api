package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Product;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.repository.ContinentsRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ContinentsRepository continentRepository;
	int counter = 0;

	@PostMapping("/create")
	public ResponseEntity<String> createProduct(@RequestBody Product product) {
		Integer id = productService.saveProduct(product);
		String message = "Product created with id: " + id + " !..";
		return new ResponseEntity<String>(message, HttpStatus.CREATED);
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAllProducts() {
		ResponseEntity<?> resp = null;
		try {
			List<Product> pl = productService.findAllProducts();
			resp = new ResponseEntity<List<Product>>(pl, HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			e.printStackTrace();
			resp = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}

	@GetMapping("/find/{id}")
	public ResponseEntity<?> fetchOneProduct(@PathVariable Integer id) {
		ResponseEntity<?> resp = null;
		try {
			Product p = productService.findOneProduct(id);
			resp = new ResponseEntity<Product>(p, HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			e.printStackTrace();
			resp = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {
		ResponseEntity<String> resp = null;
		try {
			productRepository.updateProductVendorId(null, id);
			productService.deleteProduct(id);
			resp = new ResponseEntity<String>("Product with id " + id + " successfully deleted!..", HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			e.printStackTrace();
			resp = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}

	@PutMapping("/modify")
	public ResponseEntity<String> updateProduct(@RequestBody Product product) {
		ResponseEntity<String> resp = null;
		try {
			productService.updateProduct(product);
			resp = new ResponseEntity<String>("Product with id " + product.getpId() + " successfully updated!..",
					HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			e.printStackTrace();
			resp = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}

	@PatchMapping("/update/vendor/{id}/{vname}")
	public ResponseEntity<String> updateProductVendor(@PathVariable Integer id, @PathVariable String vname) {
		ResponseEntity<String> resp = null;
		try {
			productService.updateProductVendor(vname, id);
			resp = new ResponseEntity<String>("Product vendor with id " + id + " successfully updated!..",
					HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			e.printStackTrace();
			resp = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}

	@PostMapping("/save-all-products")
	public ResponseEntity<String> createAllProducts(@RequestBody List<Product> products)
			throws JsonSyntaxException, JsonProcessingException {
		ResponseEntity<String> resp = null;
		if (products != null) {
			productRepository.saveAll(products);
			resp = new ResponseEntity<String>("List of products has been saved!..", HttpStatus.CREATED);
		} else {
			resp = new ResponseEntity<String>("List of products is empty!..", HttpStatus.NO_CONTENT);
		}
		return resp;
	}

	@PutMapping("/update-vendor/{pid}/{vid}")
	public ResponseEntity<String> updateVendor(@PathVariable Integer pid, @PathVariable Integer vid) {
		ResponseEntity<String> resp = null;
		Optional<Product> product = productRepository.findById(pid);
		System.out.println(product);
		return resp;
	}

	@GetMapping("/json")
	public String getAllProductsasJson() throws JsonSyntaxException, JsonProcessingException {
		List<Product> prods = productRepository.findAll();
		ObjectMapper mapper = new ObjectMapper();
		JsonArray products = JsonParser.parseString(mapper.writeValueAsString(prods)).getAsJsonArray();
		return products.toString();
	}

	@GetMapping("/json-without-cost")
	public String getAllProductsasJsonPractice() throws JsonSyntaxException, JsonProcessingException {
		List<Product> prods = productRepository.findAll();
		ObjectMapper mapper = new ObjectMapper();
		JsonArray products = JsonParser.parseString(mapper.writeValueAsString(prods)).getAsJsonArray();
		JsonArray finalProducts = new JsonArray();
		List<String> companies = new ArrayList<String>(Arrays.asList("Yamaha", "Suzuki", "Apple", "Samsung"));
		products.forEach(pr -> {
			JsonObject o = new JsonObject();
			o = (JsonObject) pr;
			o.addProperty("pCompany", companies.get(counter));
			counter++;
			o.remove("pCost");
			finalProducts.add(o);
		});
		return finalProducts.toString();
	}

}
