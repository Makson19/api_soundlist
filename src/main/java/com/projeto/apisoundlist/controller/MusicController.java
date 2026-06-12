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

import com.projeto.apisoundlist.dto.MusicRequestDto;
import com.projeto.apisoundlist.dto.MusicResponseDto;
import com.projeto.apisoundlist.service.MusicService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/musics")
public class MusicController {
  @Autowired
  private MusicService musicService;

  // GET "/api/musics"
  @GetMapping
  public ResponseEntity<Page<MusicResponseDto>> findAll(@PageableDefault(size=10) Pageable pagination) {
    return ResponseEntity.ok(musicService.findAll(pagination));
  }

  // GET "/api/musics/{id}"
  @GetMapping("/{id}")
  public ResponseEntity<MusicResponseDto> findById(@PathVariable Long id) {
    MusicResponseDto musicResponseDto = musicService.findById(id);
    return ResponseEntity.ok(musicResponseDto);
  }

  // POST "/api/musics/{id}"
  @PostMapping
  public ResponseEntity<?> save(@RequestBody @Valid MusicRequestDto musicRequestDto) {
    var musicCreated = musicService.save(musicRequestDto);
    return ResponseEntity.status(201).body(musicCreated);
  }

  // PUT "/api/musics/{id}" 
  @PutMapping("/{id}")
  public ResponseEntity<MusicResponseDto> update(@PathVariable Long id, @RequestBody @Valid MusicRequestDto musicRequestDto) {
    MusicResponseDto musicResponseDto = musicService.update(id, musicRequestDto);
    return ResponseEntity.ok(musicResponseDto);
  }

  // DELETE "/api/musics/{id}"
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable Long id) {
    musicService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
