package com.projeto.apisoundlist.dto;

public record MusicPlaylistDto(
  Long id,
  String title,
  String artist,
  String genre,
  Integer duration
) {

}
