package com.scarv.events_tec.dto;

import org.springframework.web.multipart.MultipartFile;

public record EventRequestDto(String title,
                              String description,
                              Long date,
                              String city,
                              String state,
                              Boolean remote,
                              String eventUrl,
                              MultipartFile image) {
}
