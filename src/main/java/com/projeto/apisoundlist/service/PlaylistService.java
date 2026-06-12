package com.projeto.apisoundlist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.projeto.apisoundlist.dto.PlaylistRequestDto;
import com.projeto.apisoundlist.dto.PlaylistResponseDto;
import com.projeto.apisoundlist.mapper.PlaylistMapper;
import com.projeto.apisoundlist.model.Playlist;
import com.projeto.apisoundlist.repository.PlaylistRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PlaylistService {
  @Autowired
  private PlaylistRepository playlistRepository;

  // GET playlists
  public Page<PlaylistResponseDto> findAll(Pageable pagination) {
    return playlistRepository.findAll(pagination).map((u) -> PlaylistMapper.toDto(u));
  }

  // GET playlist
  public PlaylistResponseDto findById(Long id) {
    var playlist = playlistRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Playlist não encontrada!"));
    return PlaylistMapper.toDto(playlist);
  }

  // POST playlist
  public PlaylistResponseDto save(PlaylistRequestDto playlistRequestDto) {
    var playlist = PlaylistMapper.toEntity(playlistRequestDto);
    return PlaylistMapper.toDto(playlistRepository.save(playlist));
  }

  // UPDATE playlist
  public PlaylistResponseDto update(Long id, PlaylistRequestDto playlistRequestDto) {
    Playlist playlistTemp = playlistRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Playlist não encontrada!"));
    playlistTemp.setName(playlistRequestDto.name());
    playlistTemp.setDescription(playlistRequestDto.description());
    return PlaylistMapper.toDto(playlistRepository.save(playlistTemp));
  }

  // DELETE playlist
  public void deleteById(Long id) {
    var playlist = playlistRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Playlist não encontrada!"));
    playlistRepository.delete(playlist);
  }
}
