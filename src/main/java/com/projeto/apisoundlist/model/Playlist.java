package com.projeto.apisoundlist.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tb_playlists")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Playlist {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length=255, nullable=false)
  String name;

  @Column(length=255)
  String description;

  @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Music> musics = new ArrayList<>();
}
