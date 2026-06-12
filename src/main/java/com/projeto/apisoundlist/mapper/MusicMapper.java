package com.projeto.apisoundlist.mapper;

import com.projeto.apisoundlist.dto.MusicRequestDto;
import com.projeto.apisoundlist.dto.MusicResponseDto;
import com.projeto.apisoundlist.model.Music;

public class MusicMapper {
  
  public static Music toEntity(MusicRequestDto requestDto) {
    var music = new Music();
    music.setTitle(requestDto.title());
    music.setArtist(requestDto.artist());
    music.setGenre(requestDto.genre());
    music.setDuration(requestDto.duration());
    return music;
  }

  public static MusicResponseDto toDto(Music music) {
    return new MusicResponseDto(
      music.getId(),
      music.getTitle(),
      music.getArtist(),
      music.getGenre(),
      music.getDuration(),
      music.getPlaylist().getId()
    );
  }
}
