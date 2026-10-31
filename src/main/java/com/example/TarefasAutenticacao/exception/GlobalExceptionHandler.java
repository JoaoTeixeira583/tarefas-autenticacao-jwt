package com.example.TarefasAutenticacao.exception;

import com.example.TarefasAutenticacao.dto.ErroResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;


// para criar um conselheiro global para controllers
@RestControllerAdvice
public class GlobalExceptionHandler {

    // escolhe que a exceção que ele trata
    // exceção para mostrar a mensagem de email já duplicado
    @ExceptionHandler
    public ResponseEntity<ErroResponseDto> tratarEmailJaCadastrado(EmailJaCadastradoException ex){
        ErroResponseDto erroResponseDto = ErroResponseDto.builder()
                .status(HttpStatus.CONFLICT.value())
                .mensagem(ex.getMessage())
                .dataHora(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erroResponseDto);
    }

    // exceção para tratar campos invalidos com o @Valid
    @ExceptionHandler
    public ResponseEntity<ErroResponseDto> tratarDadosInvalidos(MethodArgumentNotValidException ex){
       // lista de erros com campos e mensagens
        List<String> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(error ->
                        //err.getField() fala o campo que da erro
                        //err.getDefaultMessage fala a mensagem da regra não comprida
                        error.getField() + ": " + error.getDefaultMessage()
                        )
                .toList();

        ErroResponseDto erroResponseDto = ErroResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .mensagem("Erro de validação nos campos enviados")
                .dataHora(LocalDateTime.now())
                .erros(erros)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroResponseDto);
    }

    // exceção para tratar para não ter JSON para ler
    @ExceptionHandler
    public ResponseEntity<ErroResponseDto> tratarCorpoIlegivel(HttpMessageNotReadableException ex){

        ErroResponseDto erroResponseDto = ErroResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .mensagem("Corpo da requisição ausente ou inválido")
                .dataHora(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroResponseDto);
    }

}
