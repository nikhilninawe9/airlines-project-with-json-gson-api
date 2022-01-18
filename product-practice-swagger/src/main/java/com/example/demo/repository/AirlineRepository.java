package com.example.demo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Airline;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, Integer> {

	@Transactional
	@Query(value = "select c.cname from airline a inner join continents c on a.continent_cid=c.cid where a.acode=?", nativeQuery = true)
	String getContinentNamebyAirlineCode(String airlineCode);

	@Modifying
	@Transactional
	@Query(value = "update airline set aname=? where vid=?", nativeQuery = true)
	Integer updateAirlineNamebyVendorCode(String aname, Integer vid);

	@Transactional
	@Query(value = "select vid from vendor where vcode=?", nativeQuery = true)
	Integer getVendorIdByVcode(String vcode);

	@Modifying
	@Transactional
	@Query(value = "update vendor set vcode=? where vid=?", nativeQuery = true)
	Integer updateVCodebyVid(String vcode, Integer vid);

	@Modifying
	@Transactional
	@Query(value = "update airline set continent_cid=?, vendor_vid=?, vid=? where aid=?", nativeQuery = true)
	Integer updateContinentByAirline(Integer cid, Integer vid, Integer vvid, Integer aid);

}
