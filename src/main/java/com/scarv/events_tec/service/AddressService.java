package com.scarv.events_tec.service;

import com.scarv.events_tec.domain.address.Address;
import com.scarv.events_tec.domain.event.Event;
import com.scarv.events_tec.dto.EventRequestDto;
import com.scarv.events_tec.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AddressService {

  private final AddressRepository addressRepository;

  public AddressService(AddressRepository addressRepository) {
    this.addressRepository = addressRepository;
  }

  public Address createAddress(EventRequestDto data, Event event) {
    Address address = new Address();
    address.setCity(data.city());
    address.setUf(data.state());
    address.setEvent(event);

    return addressRepository.save(address);
  }

  public Optional<Address> findByEventId(UUID eventId) {
    return addressRepository.findByEventId(eventId);
  }
}
