package com.scarv.events_tec.domain.event;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

  @Id
  @GeneratedValue
  private UUID id;
  private String title;
  private String description;
  private String imageUrl;
  private String eventUrl;
  private Boolean remote;
  private Date date;
}
