package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.AirTickets;
import com.example.demo.entity.Airline;
import com.example.demo.repository.AirlineRepository;
import com.example.demo.repository.AirtTicketRepository;
import com.example.demo.repository.ContinentsRepository;
import com.example.demo.repository.VendorRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

@RestController
@RequestMapping("/airline")
public class AirlineController {
	@Autowired
	private AirlineRepository airlineRepository;
	@Autowired
	private AirtTicketRepository airTicketRepository;
	@Autowired
	private ContinentsRepository continentsRepository;
	@Autowired
	private VendorRepository vendorRepository;

	@PostMapping("/create-airline")
	public ResponseEntity<?> createAirlines(@RequestBody Airline airline) {
		ResponseEntity<?> resp = null;
		if (airline != null) {
			airlineRepository.save(airline);
			resp = new ResponseEntity<String>("Airline has been saved!..", HttpStatus.OK);
		} else {
			throw new RuntimeException("No content in airline object");
		}
		return resp;
	}

	@GetMapping("/get-all-airlines")
	public ResponseEntity<?> getAllAirlines() {
		ResponseEntity<?> resp = null;
		List<Airline> listAirlines = null;
		try {
			listAirlines = airlineRepository.findAll();
			resp = new ResponseEntity<List<Airline>>(listAirlines, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			resp = new ResponseEntity<String>("Cant fetch the airline data!..", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}

	@GetMapping("/get-continent/{airlineCode}")
	public ResponseEntity<?> getContinentByAirlineCode(@PathVariable String airlineCode) {
		ResponseEntity<?> resp = null;
		try {
			String continent = airlineRepository.getContinentNamebyAirlineCode(airlineCode);
			if (!continent.isEmpty()) {
				resp = new ResponseEntity<String>(continent, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp = new ResponseEntity<String>("No continent found for given airline code: " + airlineCode,
					HttpStatus.NOT_FOUND);
		}
		return resp;
	}

	@PutMapping("/update-airlinename/{vcode}/{aname}")
	public ResponseEntity<?> updateAirlineName(@PathVariable String vcode, @PathVariable String aname) {
		ResponseEntity<?> resp = null;
		try {
			Integer vid = airlineRepository.getVendorIdByVcode(vcode);
			airlineRepository.updateVCodebyVid("V-0110", vid);
			Integer flag = airlineRepository.updateAirlineNamebyVendorCode(aname, vid);
			System.out.println(flag);
			if (flag > 0) {
				resp = new ResponseEntity<String>("Airline name has been updated successfully", HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp = new ResponseEntity<String>("Incorrect vendor code: " + vcode, HttpStatus.NOT_FOUND);
		}
		return resp;
	}

	@PostMapping("/book-ticket")
	public ResponseEntity<?> bookTicket(@RequestBody AirTickets airTickets) {
		ResponseEntity<?> resp = null;
		String start = airTickets.getDestination();
		String end = airTickets.getStartingPoint();
		try {
			List<String> cities = new ArrayList<String>();
			List<Object[]> listCities = airTicketRepository.getCities();
			listCities.forEach(l -> {
				cities.add((String) l[0]);
			});
			System.out.println(cities);
			if (cities.contains(airTickets.getStartingPoint()) && cities.contains(airTickets.getDestination())) {
				if (start.equals(end)) {
					resp = new ResponseEntity<String>("Cant fly within same cities!..", HttpStatus.BAD_REQUEST);
				} else {
					airTickets = airTicketRepository.save(airTickets);
					resp = new ResponseEntity<AirTickets>(airTickets, HttpStatus.OK);
				}
			} else {
				resp = new ResponseEntity<String>("Given cities are not allowed to travel!..", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp = new ResponseEntity<String>("Something went wrong!..", HttpStatus.OK);
		}
		return resp;
	}

	@PutMapping("/update-tkt-price/{airtktid}")
	public ResponseEntity<?> updateTicketPrice(@PathVariable Integer airtktid) {
		ResponseEntity<?> resp = null;
		try {
			AirTickets tkt = airTicketRepository.getDiscountOnTkt(airtktid);
			Double discount = tkt.getDiscountOnTkt();
			Double tktprice = tkt.getTktPrice();
			Double amountToBeSub = (tktprice * discount / 100);
			Double finalPrice = tktprice - amountToBeSub;
			airTicketRepository.updateAirtktDiscount(finalPrice, airtktid);
			resp = new ResponseEntity<String>(
					"Final price with discount for ticket with ticket id: " + airtktid + " is " + finalPrice,
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			resp = new ResponseEntity<String>("Something went wrong please check!..", HttpStatus.BAD_REQUEST);
		}
		return resp;
	}

	@PutMapping("/add-continent-in-airline/{airlineId}/{clocation}/{vendorname}")
	public ResponseEntity<?> updateContinentAndVendorInAirline(@PathVariable Integer airlineId,
			@PathVariable String clocation, @PathVariable String vendorname) {
		ResponseEntity<?> resp = null;
		try {
			Integer cid = continentsRepository.getContinentIdByConLocation(clocation);
			Integer vendorId = vendorRepository.getVendorIdByVendorName(vendorname);
			airlineRepository.updateContinentByAirline(cid, vendorId, vendorId, airlineId);
			resp = new ResponseEntity<String>("Continent has been updated as " + cid, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			resp = new ResponseEntity<String>("Cant update the continent, Please check the data..",
					HttpStatus.BAD_REQUEST);
		}
		return resp;
	}

	@GetMapping("/get-all-airtkt")
	public String getAllAirTkt() throws JsonSyntaxException, JsonProcessingException {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		List<AirTickets> tickets = airTicketRepository.findAll();
		ObjectMapper mapper = new ObjectMapper();
		JsonArray finalArray = new JsonArray();
		JsonArray airTicketsArray = JsonParser.parseString(mapper.writeValueAsString(tickets)).getAsJsonArray();
		airTicketsArray.forEach(ticket -> {
			JsonObject obj = new JsonObject();
			obj = (JsonObject) ticket;
			obj.addProperty("bookedOn", dateformat.format(date));
			finalArray.add(obj);
		});
		return finalArray.toString();
	}

}
