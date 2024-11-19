package com.scarv.events_tec.controller;

import com.scarv.events_tec.domain.coupon.Coupon;
import com.scarv.events_tec.dto.CouponRequestDto;
import com.scarv.events_tec.service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

  private final CouponService couponService;

  public CouponController(CouponService couponService) {
    this.couponService = couponService;
  }

  @PostMapping("/event/{eventId}")
  public ResponseEntity<Coupon> addCouponToEvent(@PathVariable UUID eventId, @RequestBody CouponRequestDto data){
    Coupon coupon = couponService.addCoupontoEvent(eventId, data);
    return ResponseEntity.ok(coupon);
  }
}
