package com.example.demo.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Product;
import com.google.gson.JsonArray;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	// @Modifying is for non select queries
	// This is with simple sql query
	@Modifying
	@Transactional
	@Query(value = "update product set pvendor=? where pid=?", nativeQuery = true)
	int updateProductVendor(String pvendor, Integer pid);

	// This is with Hql query
	@Modifying
	@Query("update Product set pvendor=:vendor where pid=:id")
	int updateProductVendor1(String vendor, Integer id);

	@Modifying
	@Transactional
	@Query(value = "update Product set vendor_vid=?  where pid=?", nativeQuery = true)
	int updateProductVendorId(Integer vendor, Integer id);

	@Modifying
	@Transactional
	@Query(value = "update Product set pcost=?  where pid=? and pvendor=?", nativeQuery = true)
	int updateDiscountedPriceOfProduct(Double finalPCost, Integer pid, String pVen);

	@Modifying
	@Transactional
	@Query(value = "select my_table.*, c.cname from	(select p.pcode, p.pvendor, v.vcode, v.vname, v.continents_cid from product p inner join vendor v on p.vendor_vid=v.vid) as my_table inner join continents c on my_table.continents_cid=c.cid where c.clocation=?", nativeQuery = true)
	List<Object[]> getProductVendorContinentDetailsByCountryName(String countryLocation);

	@Modifying
	@Transactional
	@Query(value = "(select my_table.pvendor, my_table.pcost, my_table.pcode,c.continents_cid from (select p.pvendor, p.pcost, p.pcode, v.vid from product p inner join vendor v on p.vendor_vid=v.vid) as my_table inner join continents_vendors c on my_table.vid=c.vendors_vid)", nativeQuery = true)
	List<Object[]> getProductContinent();

	@Transactional
	@Query(value = "select p.* from product p;", nativeQuery = true)
	JsonArray getAllProdAsJsonArray();

}
