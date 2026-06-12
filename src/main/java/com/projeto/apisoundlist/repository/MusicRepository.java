package com.projeto.apisoundlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.apisoundlist.model.Music;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {

}
