package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Continents;
import com.example.demo.entity.Product;
import com.example.demo.entity.Vendor;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.exception.VendorNotFoundException;
import com.example.demo.repository.ContinentsRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.VendorRepository;
import com.example.demo.service.ProductService;
import com.example.demo.service.VendorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

@RestController
@RequestMapping("/vendor")
public class VendorController {

	@Autowired
	private VendorService vendorService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private VendorRepository vendorRepository;

	@Autowired
	private ContinentsRepository continentsRepository;

	Double finalPCost;
	Integer count = 0;

	@GetMapping("/find/{id}")
	public ResponseEntity<?> getVendorById(@PathVariable int id) {
		ResponseEntity<?> resp = null;
		try {
			Vendor v = vendorService.findOneVendor(id);
			resp = new ResponseEntity<Vendor>(v, HttpStatus.OK);
		} catch (VendorNotFoundException e) {
			e.printStackTrace();
			resp = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}

	@GetMapping("/all")
	public ResponseEntity<?> getVendors() {
		ResponseEntity<?> resp = null;
		try {
			List<Vendor> vlist = vendorService.findAllVendors();
			resp = new ResponseEntity<List<Vendor>>(vlist, HttpStatus.OK);
		} catch (VendorNotFoundException e) {
			e.printStackTrace();
			resp = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}

	@PostMapping("/create")
	public ResponseEntity<String> createVendor(@RequestBody Vendor vendor) {
		int id = 0;
		String msg = "";
		if (vendor.getProducts().isEmpty()) {
			Vendor v1 = new Vendor(vendor.getvId(), vendor.getvName(), vendor.getvAddress(), vendor.getvCode(), null,
					null, null);
			id = vendorService.saveVendor(v1);
			if (id != 0) {
				msg = "Vendor created with id: " + id + " !..";
				return new ResponseEntity<String>(msg, HttpStatus.CREATED);
			} else {
				msg = "Vendor not created with id: " + vendor.getvId() + " !..";
				return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
			}
		} else {
			id = vendorService.saveVendor(vendor);
			List<Product> p1 = new LinkedList<Product>();
			try {
				vendor.getProducts().forEach(pr -> {
					p1.add(pr);
				});
				productService.saveProducts(p1);
			} catch (ProductNotFoundException e) {
				e.printStackTrace();
			}
			if (id != 0) {
				msg = "Vendor created with id: " + id + " !..";
				return new ResponseEntity<String>(msg, HttpStatus.CREATED);
			} else {
				msg = "Vendor not created with id: " + id + " !..";
				return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
			}
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteVendor(@PathVariable int id) {
		ResponseEntity<String> resp = null;
		String msg = "";
		try {
			productRepository.updateProductVendorId(null, id);
			vendorService.deleteVendor(id);
			msg = "Vendor with id: " + id + " deleted!..";
			resp = new ResponseEntity<String>(msg, HttpStatus.OK);
		} catch (VendorNotFoundException e) {
			e.printStackTrace();
			resp = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}

	@PutMapping("/modify")
	public ResponseEntity<String> updateVendor(@RequestBody Vendor v) {
		ResponseEntity<String> resp = null;
		String msg = "";
		try {
			vendorService.updateVendor(v);
			msg = "Vendor with id: " + v.getvId() + " is been updated successfully!..";
			resp = new ResponseEntity<String>(msg, HttpStatus.OK);
		} catch (VendorNotFoundException e) {
			e.printStackTrace();
			resp = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}

	@GetMapping("/p-and-v")
	public ResponseEntity<?> getpV() {
		ResponseEntity<?> resp = null;
		try {
			List<String> o = vendorRepository.getPcodeVname();
			List<Object> list = new ArrayList<>();
			Map<String, Object> map2 = new HashMap<>();
			for (String i : o) {
				Map<String, Object> map1 = new HashMap<>();
				String arr[] = i.split(",");
				map1.put("id", arr[0]);
				map1.put("name", arr[1]);
				list.add(map1);
			}
			map2.put("abc", list);
			resp = new ResponseEntity<Map<String, Object>>(map2, HttpStatus.OK);
		} catch (VendorNotFoundException e) {
			e.printStackTrace();
			resp = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}

	@PutMapping("/{vDiscount}/{vId}")
	public ResponseEntity<?> getDiscountedPriceOfProduct(@PathVariable int vDiscount, @PathVariable int vId) {
		ResponseEntity<?> resp = null;
		String msg = "";
		List<Object[]> prodForDisc = vendorRepository.getProductsForDiscount(vId);
		if (prodForDisc != null) {
			for (Object[] prod : prodForDisc) {
				Integer pid = (Integer) prod[0];
				Double prodCost = (Double) prod[2];
				String prodVendor = (String) prod[3];
				finalPCost = prodCost - (prodCost * vDiscount / 100);
				vendorRepository.updateVendorDiscount(vDiscount, vId);
				productRepository.updateDiscountedPriceOfProduct(finalPCost, pid, prodVendor);
			}
			msg = "Discounted price shown as: " + finalPCost + " for vendor: " + vId + " with discount percentage: "
					+ vDiscount + " %";
			resp = new ResponseEntity<String>(msg, HttpStatus.OK);
		} else {
			throw new ProductNotFoundException("No products available");
		}
		return resp;
	}

	@GetMapping("/getp&V&C/{clocation}")
	public ResponseEntity<?> getAllTableDetailsLocationWise(@PathVariable String clocation) {
		ResponseEntity<?> resp = null;
		try {
			List<Object[]> list = productRepository.getProductVendorContinentDetailsByCountryName(clocation);
			List<Map<String, Object>> ml = new ArrayList<>();
			list.forEach(l -> {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("ProdCode", l[0]);
				m.put("ProductName", l[1]);
				m.put("VendorCode", l[2]);
				m.put("VendorName", l[3]);
				m.put("ContinentId", l[4]);
				m.put("ContinentName", l[5]);
				ml.add(m);
			});
			resp = new ResponseEntity<List<Map<String, Object>>>(ml, HttpStatus.OK);
		} catch (VendorNotFoundException e) {
			e.printStackTrace();
			resp = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}

	@GetMapping("/getp-C")
	public ResponseEntity<?> getProdCont() {
		ResponseEntity<?> resp = null;
		try {
			List<Object[]> list = productRepository.getProductContinent();
			List<Map<String, Object>> ml = new ArrayList<>();
			list.forEach(l -> {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("ProdVendor", l[0]);
				m.put("ProductCost", l[1]);
				m.put("ProductCode", l[2]);
				m.put("ContinentId", l[3]);
				ml.add(m);
			});
			resp = new ResponseEntity<List<Map<String, Object>>>(ml, HttpStatus.OK);
		} catch (VendorNotFoundException e) {
			e.printStackTrace();
			resp = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}

	@GetMapping("/getvendor-withmod")
	public String getVendor() throws JsonSyntaxException, JsonProcessingException {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();

		List<Vendor> vendors = vendorRepository.findAll();

		ObjectMapper mapper = new ObjectMapper();
		JsonArray vendorsarr = JsonParser.parseString(mapper.writeValueAsString(vendors)).getAsJsonArray();

		// List for inputs
		List<String> cars = new ArrayList<>(Arrays.asList("Suzuki", "BMW", "Mercedes", "Toyota", "Mazda"));
		List<String> bname = new ArrayList<>(
				Arrays.asList("Dabur", "Baidyanath", "Vicco", "Nycil", "ITC", "Lux", "Rin"));

		// Product Array
		JsonArray finalProduct = new JsonArray();
		// Vendor Array
		JsonArray finalVendor = new JsonArray();

		vendorsarr.forEach(v -> {
			JsonObject job = new JsonObject();
			job = (JsonObject) v;
			job.remove("vDiscount");
			job.addProperty("vendorCar", cars.get(count));
			count++;
			JsonArray prodarr = (JsonArray) job.get("products");
			prodarr.forEach(pr -> {
				JsonObject ob = new JsonObject();
				ob = (JsonObject) pr;
				ob.remove("pCost");
				ob.addProperty("pBrandName", bname.get(count));
				ob.addProperty("pPurchasedon", dateformat.format(date));
				count++;
				finalProduct.add(ob);
			});
			finalVendor.add(job);
		});
		return finalVendor.toString();
	}

	// Changed entire array
	@GetMapping("/get-final-modified-continent")
	public String getFinalModifiedVendor() throws JsonSyntaxException, JsonProcessingException {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		List<Continents> continents = continentsRepository.findAll();
		ObjectMapper mapper = new ObjectMapper();
		JsonArray continentsarr = JsonParser.parseString(mapper.writeValueAsString(continents)).getAsJsonArray();

		// Product Array
		JsonArray finalProduct = new JsonArray();
		// Vendor Array
		JsonArray finalVendor = new JsonArray();
		// Continent Array
		JsonArray finalContinent = new JsonArray();

		// List for inputs
		List<String> cars = new ArrayList<>(Arrays.asList("Suzuki", "BMW", "Mercedes", "Toyota", "Mazda"));
		List<String> bname = new ArrayList<>(
				Arrays.asList("Dabur", "Baidyanath", "Vicco", "Nycil", "ITC", "Lux", "Rin"));

		continentsarr.forEach(c -> {
			JsonObject object = new JsonObject();
			object = (JsonObject) c;
			object.remove("airlines");
			object.addProperty("continentArea", "51000 sq-ft");
			JsonArray vendorsarr = (JsonArray) object.get("vendors");
			vendorsarr.forEach(v -> {
				JsonObject job = new JsonObject();
				job = (JsonObject) v;
				job.remove("vDiscount");
				job.addProperty("vendorCar", cars.get(count));
				count++;
				JsonArray prodarr = (JsonArray) job.get("products");
				prodarr.forEach(pr -> {
					JsonObject ob = new JsonObject();
					ob = (JsonObject) pr;
					ob.remove("pCost");
					ob.addProperty("pBrandName", bname.get(count));
					ob.addProperty("pPurchasedon", dateformat.format(date));
					count++;
					// Adding each array of element inside product array
					finalProduct.add(ob);
				});
				// Adding each array of element inside vendor array
				finalVendor.add(job);
			});
			// Adding each array of element inside continent array
			finalContinent.add(object);
		});
		// Adding each array inside continent array which is final
		return finalContinent.toString();
	}

	// Changed 1 object
	@GetMapping("/modify-vendor")
	public String getModifiedVendor() throws JsonSyntaxException, JsonProcessingException {
		List<Vendor> vendors = vendorRepository.findAll();
		ObjectMapper mapper = new ObjectMapper();
		JsonArray modifiedVendor = new JsonArray();
		JsonArray vendorsArray = JsonParser.parseString(mapper.writeValueAsString(vendors)).getAsJsonArray();
		vendorsArray.forEach(vendor -> {
			JsonObject obj1 = new JsonObject();
			obj1 = (JsonObject) vendor;
			obj1.remove("vDiscount");
			modifiedVendor.add(obj1);
		});
		return modifiedVendor.toString();
	}

	@GetMapping("/getp-C-with-json")
	public String getProdContWithJson() throws JsonSyntaxException, JsonProcessingException {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		JsonArray finalProductArray = new JsonArray();

		List<Object[]> list = productRepository.getProductContinent();
		// Now Object Array has been converted into Json format ie Map
		List<Map<String, Object>> ml = new ArrayList<>();
		list.forEach(l -> {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("ProdVendor", l[0]);
			m.put("ProductCost", l[1]);
			m.put("ProductCode", l[2]);
			m.put("ContinentId", l[3]);
			ml.add(m);
		});

		ObjectMapper mapper = new ObjectMapper();
		JsonArray productArray = JsonParser.parseString(mapper.writeValueAsString(ml)).getAsJsonArray();
		productArray.forEach(product -> {
			JsonObject p = new JsonObject();
			p = (JsonObject) product;
			p.remove("ContinentId");
			p.addProperty("createdOn", dateformat.format(date));
			finalProductArray.add(p);
		});
		return finalProductArray.toString();
	}
}
