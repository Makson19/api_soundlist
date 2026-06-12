package com.projeto.apisoundlist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.apisoundlist.dto.PlaylistRequestDto;
import com.projeto.apisoundlist.dto.PlaylistResponseDto;
import com.projeto.apisoundlist.service.PlaylistService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {
  @Autowired
  private PlaylistService playlistService;

  // GET "/api/playlists"
  @GetMapping
  public ResponseEntity<Page<PlaylistResponseDto>> findAll(@PageableDefault(size = 10) Pageable pagination) {
    return ResponseEntity.ok(playlistService.findAll(pagination));
  }

  // GET "/api/playlists/{id}"
  @GetMapping("/{id}")
  public ResponseEntity<PlaylistResponseDto> findById(@PathVariable Long id) {
    PlaylistResponseDto playlistResponseDto = playlistService.findById(id);
    return ResponseEntity.ok(playlistResponseDto);
  }

  // POST "/api/playlists/{id}"
  @PostMapping
  public ResponseEntity<?> save(@RequestBody @Valid PlaylistRequestDto playlistRequestDto) {
    var playlistCreated = playlistService.save(playlistRequestDto);
    return ResponseEntity.status(201).body(playlistCreated);
  }

  // PUT "/api/playlists/{id}"
  @PutMapping("/{id}")
  public ResponseEntity<PlaylistResponseDto> update(@PathVariable Long id, @RequestBody @Valid PlaylistRequestDto playlistRequestDto) {
    PlaylistResponseDto playlistResponseDto = playlistService.update(id, playlistRequestDto);
    return ResponseEntity.ok(playlistResponseDto);
  }

  // DELETE "/api/playlists/{id}"
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable Long id) {
    playlistService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
