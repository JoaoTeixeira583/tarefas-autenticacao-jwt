package com.example.TarefasAutenticacao.controller;

import com.example.TarefasAutenticacao.dto.CadastroUsuarioRequestDto;
import com.example.TarefasAutenticacao.dto.UsuarioResponseDto;
import com.example.TarefasAutenticacao.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioResponseDto> cadastrarUsuario(@RequestBody @Valid CadastroUsuarioRequestDto cadastroUsuarioRequestDto){
        UsuarioResponseDto cadastradoUsuario = usuarioService.cadastrarUsuario(cadastroUsuarioRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(cadastradoUsuario);
    }

}
