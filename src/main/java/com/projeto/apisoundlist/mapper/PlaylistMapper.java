package com.projeto.apisoundlist.mapper;

import java.util.List;

import com.projeto.apisoundlist.dto.MusicPlaylistDto;
import com.projeto.apisoundlist.dto.PlaylistRequestDto;
import com.projeto.apisoundlist.dto.PlaylistResponseDto;
import com.projeto.apisoundlist.model.Playlist;

public class PlaylistMapper {
  public static Playlist toEntity(PlaylistRequestDto requestDto) {
    var playlist = new Playlist();
    playlist.setName(requestDto.name());
    playlist.setDescription(requestDto.description());
    return playlist;
  }

  public static PlaylistResponseDto toDto(Playlist playlist) {
    List<MusicPlaylistDto> musics = playlist.getMusics().stream().map(music -> new MusicPlaylistDto(
      music.getId(),
      music.getTitle(),
      music.getArtist(),
      music.getGenre(),
      music.getDuration()
    )).toList();

    return new PlaylistResponseDto(
      playlist.getId(),
      playlist.getName(),
      playlist.getDescription(),
      musics
    );
  }
}
