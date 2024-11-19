package com.scarv.events_tec.service;

import com.scarv.events_tec.domain.coupon.Coupon;
import com.scarv.events_tec.domain.event.Event;
import com.scarv.events_tec.dto.CouponRequestDto;
import com.scarv.events_tec.repository.CouponRepository;
import com.scarv.events_tec.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CouponService {

  private final CouponRepository couponRepository;
  private final EventRepository eventRepository;

  public CouponService(CouponRepository couponRepository, EventRepository eventRepository) {
    this.couponRepository = couponRepository;
    this.eventRepository = eventRepository;
  }

  public Coupon addCoupontoEvent(UUID eventId, CouponRequestDto couponData){
    Event event = eventRepository.findById(eventId).
            orElseThrow(() -> new IllegalArgumentException("Event not found"));

    Coupon coupon = new Coupon();
    coupon.setCode(couponData.code());
    coupon.setDiscount(couponData.discount());
    coupon.setValid(new Date(couponData.valid()));
    coupon.setEvent(event);

    return couponRepository.save(coupon);
  }

  public List<Coupon> consultCoupon(UUID eventId, Date currentDate){
    return couponRepository.findByEventIdAndValidAfter(eventId, currentDate);
  }
}
