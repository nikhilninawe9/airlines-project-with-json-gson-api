package com.example.demo.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Integer> {
	@Modifying
	@Transactional
	@Query(value = "select p.pcode, v.vname from product p inner join vendor v on p.vendor_vid=v.vid order by p.pcode;", nativeQuery = true)
	List<String> getPcodeVname();

	@Modifying
	@Transactional
	@Query(value = "select p.*, v.vdiscount from product p inner join  vendor v on p.vendor_vid=v.vid where p.vendor_vid = ?; ", nativeQuery = true)
	List<Object[]> getProductsForDiscount(Integer id);

	@Modifying
	@Transactional
	@Query(value = "update Vendor set vdiscount=?  where pvendor=?", nativeQuery = true)
	int updateDiscountedPriceOfProduct(Double finalPCost, String pVendor);

	@Modifying
	@Transactional
	@Query(value = "update vendor set vdiscount=? where vid=?", nativeQuery = true)
	int updateVendorDiscount(Integer vDiscount, Integer vid);

	@Transactional
	@Query(value = "select vid from vendor where vname=?", nativeQuery = true)
	Integer getVendorIdByVendorName(String vname);

}
