package com.example.TarefasAutenticacao.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiracao}")
    private long expiracao;

    private SecretKey chave;

    // Ele transforma a secret(um Texto)  em uma chave criptográfica pornta para assinar os tokens
    // PostConstruct O spring cria o objeto, depois preenche a secret com o @Value, e só então chama o método.
    @PostConstruct
    public void iniciarChave(){
        // secret.getBytes(StandardCharsets.UTF_8): converte o texto em bytes, que é o formato que a criptografia entende. O UTF-8 garante o mesmo resultado em qualquer computador.
        // Keys.hmacShaKeyFor(...): pega os bytes e cria uma chave própria para o algoritmo HMAC-SHA (o HS256). Se os bytes forem poucos (menos de 32), lança erro.
        // chave = ...: guarda a chave no campo, para os outros métodos usarem.
        chave = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // metodo para gerar Token
    public String gerarToken(String email){
        // para ver o momento atual
        Date agora = new Date();

        // data para calcular a data de expiração
        Date dataExpiracao = new Date(agora.getTime() + expiracao);

        return Jwts.builder()
                // grava o token de quem é aquele token,tipo o assunto
                // para saber que o dono do token
                .subject(email)
                // pegar o a data de agora
                .issuedAt(agora)
                // para decidir a data de expiração
                .expiration(dataExpiracao)
                // para fazer a assinatura
                .signWith(chave)
                .compact();
    }

    // metodo para extrair e-mail
    public String extrairEmail(String token){
        // cria o leitor de tokens
        return Jwts.parser()
                // para conferir a assinatura
                .verifyWith(chave)
                // termina a leitura da chave
                .build()
                // abra o token,confere a assinatura com a chave, confere se não expirou
                .parseSignedClaims(token)
                // devolva o conteúdo do token
                .getPayload()
                // Lê o e-mail gravado no gerarToken
                .getSubject();
    }

    // metodo para validar token
    public boolean validarToken(String token){
        try {
            Jwts.parser()
                    // para conferir a assinatura
                    .verifyWith(chave)
                    // termina a leitura da chave
                    .build()
                    // abra o token,confere a assinatura com a chave, confere se não expirou
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }

}
