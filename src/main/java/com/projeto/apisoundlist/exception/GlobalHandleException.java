package com.projeto.apisoundlist.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.projeto.apisoundlist.dto.ExceptionResponseDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalHandleException {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleEntityNotFoundException(EntityNotFoundException ex){
      Map<String, String> errors = new HashMap<>();
      errors.put("mensagem", ex.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponseDto(
        "404",
        errors,
        LocalDateTime.now()
      ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> handleValidation(MethodArgumentNotValidException ex){
      Map<String, String> errors = new HashMap<>();

      ex.getBindingResult().getFieldErrors().forEach((e) -> {
        errors.put(e.getField(), e.getDefaultMessage());
      });

      return ResponseEntity.badRequest().body(new ExceptionResponseDto(
        "400",
        errors,
        LocalDateTime.now()
      ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleInternalServerError(Exception ex){
      Map<String, String> errors = new HashMap<>();
      errors.put("mensagem", ex.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponseDto(
        "500",
        errors,
        LocalDateTime.now()
      ));
    }
}
