package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Continents;
import com.example.demo.repository.ContinentsRepository;
import com.example.demo.repository.VendorRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

@RestController
@RequestMapping("/continent")
public class ContinentController {
	@Autowired
	private ContinentsRepository continentsRepository;

	@Autowired
	private VendorRepository vendorRepository;

	@GetMapping("/continents/all")
	public List<Continents> getAllContinents() {
		return continentsRepository.findAll();
	}

	@GetMapping("/get-continent/{productName}")
	public ResponseEntity<String> getContinentByProduct(@PathVariable String productName) {
		ResponseEntity<String> resp = null;
		String msg = "";

		String countryName = continentsRepository.getContinentByProduct(productName);
		if (countryName != null) {
			msg = "The country name for product " + productName + " is: " + countryName;
			resp = new ResponseEntity<String>(msg, HttpStatus.OK);
		} else {
			resp = new ResponseEntity<String>("The country name for product " + productName + " is not present",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}

	@PostMapping("/add-continent")
	public ResponseEntity<?> addContinent(@RequestBody Continents continent) {
		ResponseEntity<?> resp = null;
		if (continent.getContinentLocation() != null && continent.getContinentName() != null
				&& continent.getContinentLocation().trim().equalsIgnoreCase("")
				&& continent.getContinentName().trim().equalsIgnoreCase("")) {
			continent = continentsRepository.save(continent);
			resp = new ResponseEntity<Continents>(continent, HttpStatus.OK);
		} else {
			resp = new ResponseEntity<String>("Check the data, something is wrong!..", HttpStatus.BAD_REQUEST);
		}
		return resp;
	}

	@GetMapping("/continents/all-by-json")
	public String getAllContinentsByJsonApi() throws JsonSyntaxException, JsonProcessingException {
		List<Continents> continents = continentsRepository.findAll();
		ObjectMapper mapper = new ObjectMapper();
		JsonArray continentsArray = JsonParser.parseString(mapper.writeValueAsString(continents)).getAsJsonArray();
		JsonArray arr = new JsonArray();
		continentsArray.forEach(c -> {
			JsonObject ob = new JsonObject();
			ob = (JsonObject) c;
			arr.add(ob);
		});
		return arr.toString();
	}

	@GetMapping("/continents-with-selected-vendors")
	public String getContinentsWithSelectedVendors() throws JsonSyntaxException, JsonProcessingException {
		List<Continents> continents = continentsRepository.findAll();
		ObjectMapper mapper = new ObjectMapper();
		JsonArray continentArray = JsonParser.parseString(mapper.writeValueAsString(continents)).getAsJsonArray();
		JsonArray continentarr = new JsonArray();
		JsonArray vendorarr = new JsonArray();
		JsonArray finalVendorArray = new JsonArray();
		continentArray.forEach(ca -> {
			JsonObject o = new JsonObject();
			o = (JsonObject) ca;
			JsonArray vendors = (JsonArray) o.get("vendors");
			vendors.forEach(v -> {
				JsonObject ob = new JsonObject();
				ob = (JsonObject) v;
				ob.remove("vDiscount");
				ob.addProperty("vCarCompany", "BMW");
				vendorarr.add(ob);
			});
			finalVendorArray.addAll(vendorarr);
			continentarr.addAll(finalVendorArray);
		});
		return continentarr.toString();
	}

	@GetMapping("/getByIdjson/{id}")
	public String getJsonCont(@PathVariable Integer id) throws JsonSyntaxException, JsonProcessingException {
		// JsonObject response = new JsonObject();
		Optional<Continents> continent = continentsRepository.findById(id);
		ObjectMapper mapper = new ObjectMapper();
		JsonArray continentbyId = JsonParser.parseString(mapper.writeValueAsString(continent)).getAsJsonArray();
		JsonArray cnames = new JsonArray();
		continentbyId.forEach(c -> {
			JsonObject jo = new JsonObject();
			jo = (JsonObject) c;
			JsonElement cname = jo.get("continentName");
			cnames.add(cname);
		});
		return cnames.toString();
	}

	// Has to be modified!..
	@GetMapping("/get-modified-continent-subarray")
	public String getContinentsWithDiffArrays() throws JsonSyntaxException, JsonProcessingException {
		List<Continents> continents = continentsRepository.findAll();
		ObjectMapper mapper = new ObjectMapper();
		JsonArray continentsArray = JsonParser.parseString(mapper.writeValueAsString(continents)).getAsJsonArray();
		JsonArray newContinents = new JsonArray();
		JsonArray newFContinents = new JsonArray();
		// JsonObject games = new JsonObject();
		continentsArray.forEach(cont -> {
			JsonObject c = new JsonObject();
			c = (JsonObject) cont;
			c.remove("vendors");
			newContinents.add(c);
			JsonObject g = new JsonObject();
			g.addProperty("gameId", 1001);
			g.addProperty("gameName", "Counter Strike");
			g.addProperty("gameCountryOrigin", "India");

			// newContinents.addAll(games);
			newFContinents.addAll(newContinents);
		});
		return newFContinents.toString();
	}

	@GetMapping("/get-continentname-by-pvendor/{pvendor}")
	public String getContinentByPvendor(@PathVariable String pvendor) {
		String continentName = continentsRepository.getContinentByProductVendor(pvendor);
		return continentName;
	}

}
