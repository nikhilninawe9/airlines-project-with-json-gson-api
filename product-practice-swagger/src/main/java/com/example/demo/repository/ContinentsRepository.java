package com.example.demo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Continents;

@Repository
public interface ContinentsRepository extends JpaRepository<Continents, Integer> {

	// we cannot give @Modify to select custom query.
	@Transactional
	@Query(value = "select c.cname from (select v.continents_cid, p.pid, p.pvendor from product p inner join vendor v on p.vendor_vid=v.vid) as mt inner join continents c  on mt.continents_cid=c.cid where pvendor=?", nativeQuery = true)
	String getContinentByProduct(String productName);

	@Transactional
	@Query(value = "select cid from continents where clocation=?", nativeQuery = true)
	Integer getContinentIdByConLocation(String clocation);

	@Transactional
	@Query(value = "select c.cname from product p inner join continents_vendors cv on p.vendor_vid=cv.vendors_vid\r\n"
			+ "inner join continents c on cv.continents_cid=c.cid where pvendor = ?;", nativeQuery = true)
	String getContinentByProductVendor(String pvendor);

	@Transactional
	@Query(value = "select * from continents where cid=?", nativeQuery = true)
	Continents getContinentBycid(Integer cid);
}
