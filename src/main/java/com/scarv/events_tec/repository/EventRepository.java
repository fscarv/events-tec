package com.scarv.events_tec.repository;

import com.scarv.events_tec.domain.event.Event;
import com.scarv.events_tec.projecao.EventAddressProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

  @Query("SELECT e.id AS id, e.title AS title, e.description AS description, e.date AS date, e.imageUrl AS imgUrl, e.eventUrl AS eventUrl, e.remote AS remote, a.city AS city, a.uf AS uf " +
          "FROM Event e LEFT JOIN Address a ON e.id = a.event.id " +
          "WHERE e.date >= :currentDate")
  Page<EventAddressProjection> findUpcomingEvents(@Param("currentDate") Date currentDate, Pageable pageable);

  @Query("SELECT e.id AS id, e.title AS title, e.description AS description, e.date AS date, e.imageUrl AS imgUrl, e.eventUrl AS eventUrl, e.remote AS remote, a.city AS city, a.uf AS uf " +
          "FROM Event e JOIN Address a ON e.id = a.event.id " +
          "WHERE (:city = '' OR a.city LIKE %:city%) " +
          "AND (:uf = '' OR a.uf LIKE %:uf%) " +
          "AND (e.date >= :startDate AND e.date <= :endDate)")
  Page<EventAddressProjection> findFilteredEvents(@Param("city") String city,
                                                  @Param("uf") String uf,
                                                  @Param("startDate") Date startDate,
                                                  @Param("endDate") Date endDate,
                                                  Pageable pageable);
}