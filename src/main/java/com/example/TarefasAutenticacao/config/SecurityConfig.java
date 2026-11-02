package com.example.TarefasAutenticacao.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    // para criar um hash
    @Bean
    public PasswordEncoder passwordEncoder(){
         BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

         return bCryptPasswordEncoder;
    }

    // defina o acesso ás rotas
    // HttpSecurity fronece para você para definir a regras de acesso
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,JwtAuthenticationFilter jwtAuthenticationFilter){
          return httpSecurity
                  // desativar o csrf
                  .csrf(csrf -> csrf.disable())
                  // para não criar nem guardar um sessão,vai se identificar com o JWT
                  .sessionManagement(sm ->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                  // registrar na cadeia
                  .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                  // para liberar as rotas
                  .authorizeHttpRequests(req ->{
                      req.requestMatchers(HttpMethod.POST,"/auth/cadastro").permitAll();
                      req.requestMatchers(HttpMethod.POST,"/auth/login").permitAll();
                      // para permitir que o admin pod entrar em todas as rotas de admin
                      req.requestMatchers("/admin/**").hasRole("ADMIN");

                      // exige autenticação para as outras rotas que não estão acimas
                      req.anyRequest().authenticated();
                  })
                  .build();
    }
}
