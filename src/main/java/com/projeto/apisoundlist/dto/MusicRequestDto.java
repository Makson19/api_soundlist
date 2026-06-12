package com.projeto.apisoundlist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MusicRequestDto(
  @NotBlank(message="O título da música é obrigatório!") String title,
  @NotBlank(message="O nome do artista é obrigatório!") String artist,
  String genre,
  @NotNull(message="O tempo de duração da música é obrigatório") @Positive(message="O tempo de duração da música deve ser um valor positivo!") Integer duration,
  @NotNull(message="O id da playlist é obrigatório!") Long playlistId
) {

}
