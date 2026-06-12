package com.projeto.apisoundlist.dto;

public record MusicResponseDto(
  Long id,
  String title,
  String artist,
  String genre,
  Integer duration,
  Long playlistId
) {

}
