package com.example.TarefasAutenticacao.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
          return httpSecurity
                  // desativar o csrf
                  .csrf(csrf -> csrf.disable())
                  .sessionManagement(sm ->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                  // para liberar as rotas
                  .authorizeHttpRequests(req ->{
                      req.requestMatchers(HttpMethod.POST,"/auth/cadastro").permitAll();
                      req.requestMatchers(HttpMethod.POST,"/auth/login").permitAll();

                      // exige autenticação para as outras rotas que não estão acimas
                      req.anyRequest().authenticated();
                  })
                  .build();
    }

}
