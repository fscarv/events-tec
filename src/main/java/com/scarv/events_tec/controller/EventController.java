package com.scarv.events_tec.controller;

import com.scarv.events_tec.domain.coupon.Coupon;
import com.scarv.events_tec.domain.event.Event;
import com.scarv.events_tec.dto.EventDetailsDto;
import com.scarv.events_tec.dto.EventRequestDto;
import com.scarv.events_tec.dto.EventResponseDto;
import com.scarv.events_tec.service.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/events")
public class EventController {

  private final EventService eventService;

  public EventController(EventService eventService) {
    this.eventService = eventService;
  }

  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<Event> create(@RequestParam("title") String title,
                                      @RequestParam(value = "description", required = false) String description,
                                      @RequestParam("date") Long date,
                                      @RequestParam("city") String city,
                                      @RequestParam("state") String state,
                                      @RequestParam("remote") Boolean remote,
                                      @RequestParam("eventUrl") String eventUrl,
                                      @RequestParam(value = "image", required = false) MultipartFile image) {
    EventRequestDto eventRequestDto = new EventRequestDto(title, description, date, city, state, remote, eventUrl, image);
    Event newEvent = eventService.createEvent(eventRequestDto);
    return ResponseEntity.ok().body(newEvent);
  }

  @GetMapping
  public ResponseEntity<List<EventResponseDto>> getEvents(@RequestParam(defaultValue = "0" ) int page,
                                                          @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok().body(eventService.getUpcomingEvents(page, size));
  }

  @GetMapping("/{eventId}")
  public ResponseEntity<EventDetailsDto> getEventDetails(@PathVariable UUID eventId) {
    EventDetailsDto eventDetails = eventService.getEventDetails(eventId);
    return ResponseEntity.ok(eventDetails);
  }

  @GetMapping("/filter")
  public ResponseEntity<List<EventResponseDto>> getFilteredEvents(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size,
                                                                  @RequestParam String city,
                                                                  @RequestParam String uf,
                                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
    List<EventResponseDto> events = eventService.getFilteredEvents(page, size, city, uf, startDate, endDate);
    return ResponseEntity.ok(events);
  }
}
