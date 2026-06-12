package com.projeto.apisoundlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.apisoundlist.model.Music;

public interface MusicRepository extends JpaRepository<Music, Long> {

}
