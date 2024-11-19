package com.scarv.events_tec.projecao;

import java.util.Date;
import java.util.UUID;

public interface EventAddressProjection {
  UUID getId();
  String getTitle();
  String getDescription();
  Date getDate();
  String getImageUrl();
  String getEventUrl();
  Boolean getRemote();
  String getCity();
  String getUf();
}
