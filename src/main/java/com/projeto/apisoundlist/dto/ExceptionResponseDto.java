package com.projeto.apisoundlist.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ExceptionResponseDto(
  String status,
  Map<String, String> errors,
  LocalDateTime localDateTime
) {

}
