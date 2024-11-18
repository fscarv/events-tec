package com.scarv.events_tec.domain.address;

import com.scarv.events_tec.domain.event.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private UUID id;
  private String city;
  private String uf;

  @ManyToOne
  @JoinColumn(name = "event_id")
  private Event event;

}
