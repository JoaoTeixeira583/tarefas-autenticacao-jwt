package com.example.TarefasAutenticacao.config;

import com.example.TarefasAutenticacao.entity.Usuario;
import com.example.TarefasAutenticacao.repository.UsuarioRepository;
import com.example.TarefasAutenticacao.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    // para conferir cada token em cada requisição
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // le o cabecalho Authorization que o cliente mandou na requisição e guarda um header
        String header = request.getHeader("Authorization");

        // testar se e null ou não começa com Bearer
        if(header == null || !header.startsWith("Bearer ")){
             filterChain.doFilter(request,response);
             return;
        }

        // cortar o prefixo do token
        String token = header.substring(7);

        // validar o token
        boolean tokenValido = tokenService.validarToken(token);

        // se o token for invalido
        if(!tokenValido){
            filterChain.doFilter(request,response);
            return;
        }

        // extrair o email
        // para descobrir quem e o usuario
        String email = tokenService.extrairEmail(token);

        // buscar o usuario no repositorio
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        // para ver se esta vazio ou não
        if(usuarioOpt.isEmpty()){
            filterChain.doFilter(request,response);
            return;
        }

        Usuario usuario = usuarioOpt.get();

        // montar a autenticação
        // para mostrar qual usuario pertence a essa requisição se e mostra que tem essas permissões
        UsernamePasswordAuthenticationToken autenticacao = new UsernamePasswordAuthenticationToken(
                usuario,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getPapel()))
        );

        // registrar no contexto
        // para saber quem é o usuario
        SecurityContextHolder.getContext().setAuthentication(autenticacao);

        // continuar a fila
        filterChain.doFilter(request,response);
    }
}
