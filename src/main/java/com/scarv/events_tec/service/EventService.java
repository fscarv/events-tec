package com.scarv.events_tec.service;

import com.scarv.events_tec.domain.address.Address;
import com.scarv.events_tec.domain.coupon.Coupon;
import com.scarv.events_tec.domain.event.Event;
import com.scarv.events_tec.dto.EventDetailsDto;
import com.scarv.events_tec.dto.EventRequestDto;
import com.scarv.events_tec.dto.EventResponseDto;
import com.scarv.events_tec.projecao.EventAddressProjection;
import com.scarv.events_tec.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventService {
  @Value("${aws.bucket.name}")
  private String bucketName;

  @Autowired
  private S3Client s3Client;
  @Autowired
  private EventRepository eventRepository;
  @Autowired
  private AddressService addressService;
  @Autowired
  private CouponService couponService;

  public Event createEvent(EventRequestDto data) {
    String imgUrl = null;
    if (data.image() != null) {
      this.uploadImg(data.image());
    }
    Event newEvent = new Event();
    newEvent.setTitle(data.title());
    newEvent.setDescription(data.description());
    newEvent.setEventUrl(data.eventUrl());
    newEvent.setDate(new Date(data.date()));
    newEvent.setImageUrl(imgUrl == null ? "" : imgUrl);
    newEvent.setRemote(data.remote());

    eventRepository.save(newEvent);

    if (!data.remote()) {
      this.addressService.createAddress(data, newEvent);
    }
    return newEvent;
  }

  public List<EventResponseDto> getUpcomingEvents(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<EventAddressProjection> eventsPage = this.eventRepository.findUpcomingEvents(new Date(), pageable);
    return eventsPage.map(event -> new EventResponseDto(
                    event.getId(),
                    event.getTitle(),
                    event.getDescription(),
                    event.getDate(),
                    event.getCity() != null ? event.getCity() : "",
                    event.getUf() != null ? event.getUf() : "",
                    event.getRemote(),
                    event.getEventUrl(),
                    event.getImageUrl())
            )
            .stream().toList();
  }

  private String uploadImg(MultipartFile multipartFile) {
    String filename = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

    try {
      PutObjectRequest putOb = PutObjectRequest.builder()
              .bucket(bucketName)
              .key(filename)
              .build();
      s3Client.putObject(putOb, RequestBody.fromByteBuffer(ByteBuffer.wrap(multipartFile.getBytes())));
      GetUrlRequest request = GetUrlRequest.builder()
              .bucket(bucketName)
              .key(filename)
              .build();

      return s3Client.utilities().getUrl(request).toString();
    } catch (Exception e) {
      System.out.println("erro ao subir arquivo");
      System.out.println(e.getMessage());
      return "";
    }
  }

  public List<EventResponseDto> getFilteredEvents(int page, int size, String city, String uf, Date startDate, Date endDate) {
    city = (city != null) ? city : "";
    uf = (uf != null) ? uf : "";
    startDate = (startDate != null) ? startDate : new Date(0);
    endDate = (endDate != null) ? endDate : new Date();

    Pageable pageable = PageRequest.of(page, size);

    Page<EventAddressProjection> eventsPage = this.eventRepository.findFilteredEvents(city, uf, startDate, endDate, pageable);
    return eventsPage.map(event -> new EventResponseDto(
                    event.getId(),
                    event.getTitle(),
                    event.getDescription(),
                    event.getDate(),
                    event.getCity() != null ? event.getCity() : "",
                    event.getUf() != null ? event.getUf() : "",
                    event.getRemote(),
                    event.getEventUrl(),
                    event.getImageUrl())
            )
            .stream().toList();
  }

  public EventDetailsDto getEventDetails(UUID eventId) {
    Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new IllegalArgumentException("Event not found"));

    Optional<Address> address = addressService.findByEventId(eventId);

    List<Coupon> coupons = couponService.consultCoupon(eventId, new Date());

    List<EventDetailsDto.CouponDto> couponDTOs = coupons.stream()
            .map(coupon -> new EventDetailsDto.CouponDto(
                    coupon.getCode(),
                    coupon.getDiscount(),
                    coupon.getValid()))
            .collect(Collectors.toList());

    return new EventDetailsDto(
            event.getId(),
            event.getTitle(),
            event.getDescription(),
            event.getDate(),
            address.isPresent() ? address.get().getCity() : "",
            address.isPresent() ? address.get().getUf() : "",
            event.getImageUrl(),
            event.getEventUrl(),
            couponDTOs);
  }

}
