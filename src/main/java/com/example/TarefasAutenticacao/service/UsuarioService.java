package com.example.TarefasAutenticacao.service;

import com.example.TarefasAutenticacao.config.SecurityConfig;
import com.example.TarefasAutenticacao.dto.CadastroUsuarioRequestDto;
import com.example.TarefasAutenticacao.dto.UsuarioResponseDto;
import com.example.TarefasAutenticacao.entity.Usuario;
import com.example.TarefasAutenticacao.exception.EmailJaCadastradoException;
import com.example.TarefasAutenticacao.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponseDto cadastrarUsuario(CadastroUsuarioRequestDto cadastroUsuarioRequestDto){
        boolean existeEmail = usuarioRepository.existsByEmail(cadastroUsuarioRequestDto.getEmail());

        // verificar se ja existe umm email
        if(existeEmail){
           throw new EmailJaCadastradoException("Email já cadastrado");
        }

        // para fazer uma senha ficar criptografada
        // senha do PasswordEnconder
        String senhaOriginal = cadastroUsuarioRequestDto.getSenha();

        // tarnsformar a senha em hash
        String senhaHash = passwordEncoder.encode(senhaOriginal);

        Usuario usuario = new Usuario();
        usuario.setEmail(cadastroUsuarioRequestDto.getEmail());
        usuario.setSenhaHash(senhaHash);
        usuario.setPapel("USER");

        Usuario usuarioCadastrado =usuarioRepository.save(usuario);

        UsuarioResponseDto usuarioResponseDto = UsuarioResponseDto.builder()
                .id(usuarioCadastrado.getId())
                .email(usuarioCadastrado.getEmail())
                .papel(usuarioCadastrado.getPapel())
                .build();

        return usuarioResponseDto;
    }

}
