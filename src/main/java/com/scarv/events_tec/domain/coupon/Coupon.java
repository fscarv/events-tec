package com.scarv.events_tec.domain.coupon;

import com.scarv.events_tec.domain.event.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "coupon")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private UUID id;
  private String code;
  private String discount;
  private Date valid;

  @ManyToOne
  @JoinColumn(name = "event_id")
  private Event event;
}