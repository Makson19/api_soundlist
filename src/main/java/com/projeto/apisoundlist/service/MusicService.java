package com.projeto.apisoundlist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.projeto.apisoundlist.dto.MusicRequestDto;
import com.projeto.apisoundlist.dto.MusicResponseDto;
import com.projeto.apisoundlist.mapper.MusicMapper;
import com.projeto.apisoundlist.repository.MusicRepository;
import com.projeto.apisoundlist.repository.PlaylistRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MusicService {
  @Autowired
  private MusicRepository musicRepository;

  @Autowired
  private PlaylistRepository playlistRepository;

  // GET musics
  public Page<MusicResponseDto> findAll(Pageable pagination) {
    return musicRepository.findAll(pagination).map((u) -> MusicMapper.toDto(u));
  }

  // GET music
  public MusicResponseDto findById(Long id) {
    var music = musicRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Música não encontrada!"));
    return MusicMapper.toDto(music);
  }

  // POST music
  public MusicResponseDto save(MusicRequestDto musicRequestDto) {
    var playlist = playlistRepository.findById(musicRequestDto.playlistId()).orElseThrow(() -> new EntityNotFoundException("Playlist não encontrada ou não existe!"));

    var music = MusicMapper.toEntity(musicRequestDto);
    music.setPlaylist(playlist);

    return MusicMapper.toDto(musicRepository.save(music));
  }

  // UPDATE music
  public MusicResponseDto update(Long id, MusicRequestDto musicRequestDto) {
    var musicTemp = musicRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Música não encontrada ou não existe!"));
    musicTemp.setTitle(musicRequestDto.title());
    musicTemp.setArtist(musicRequestDto.artist());
    musicTemp.setGenre(musicRequestDto.genre());
    musicTemp.setDuration(musicRequestDto.duration());

    var playlist = playlistRepository.findById(musicRequestDto.playlistId()).orElseThrow(() -> new EntityNotFoundException("Playlist não encontrada ou não existe!"));

    musicTemp.setPlaylist(playlist);

    return MusicMapper.toDto(musicRepository.save(musicTemp));
  }

  // DELETE music
  public void deleteById(Long id) {
    var music = musicRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Música não encontrada!"));
    musicRepository.delete(music);
  }
}
