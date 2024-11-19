package com.scarv.events_tec.repository;

import com.scarv.events_tec.domain.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {

  Optional<Address> findByEventId(UUID eventId);
}
