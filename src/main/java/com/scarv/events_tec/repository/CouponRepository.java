package com.scarv.events_tec.repository;

import com.scarv.events_tec.domain.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, UUID> {

  List<Coupon> findByEventIdAndValidAfter(UUID eventId, Date currentDate);
}
