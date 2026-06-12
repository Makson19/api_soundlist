package com.projeto.apisoundlist.dto;

import java.util.List;

public record PlaylistResponseDto(
  Long id,
  String name,
  String description,
  List<MusicPlaylistDto> musics
) {

}
