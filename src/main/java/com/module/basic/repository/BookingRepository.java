package com.module.basic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.module.basic.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	@Query(value = "select * from booking where user_Id=?1", nativeQuery = true)
	public List<Booking> findbyUserId(String userId);	
	@Query(value = "select time from booking where store_code=?1 and day=?2", nativeQuery = true)
	public List<Integer> findByStoreCodeAndDay(long storeCode, int day);
	
}
