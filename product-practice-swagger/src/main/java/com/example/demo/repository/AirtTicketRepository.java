package com.example.demo.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.AirTickets;

@Repository
public interface AirtTicketRepository extends JpaRepository<AirTickets, Integer> {

	@Transactional
	@Query(value = "select c.cities from cities c", nativeQuery = true)
	List<Object[]> getCities();

	@Modifying
	@Transactional
	@Query(value = "update air_tickets set final_tkt_price=? where air_tkt_id=?", nativeQuery = true)
	int updateAirtktDiscount(Double finalprice, Integer aittktid);

	@Transactional
	@Query(value = "select * from air_tickets where air_tkt_id=?", nativeQuery = true)
	AirTickets getDiscountOnTkt(Integer airtktid);
}
