package com.projeto.apisoundlist.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tb_musics")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Music {
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @Column(length=255, nullable=false)
  private String title;

  @Column(length=255, nullable=false)
  private String artist;

  @Column(length=255)
  private String genre;

  @Column(nullable=false)
  private Integer duration;

  @ManyToOne
  @JoinColumn(name="playlist_id")
  private Playlist playlist;
}
