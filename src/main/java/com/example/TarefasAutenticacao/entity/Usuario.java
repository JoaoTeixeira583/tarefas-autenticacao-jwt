package com.example.TarefasAutenticacao.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // para não permitir null e deixar esse campo como unico
    @Column(nullable = false,unique = true)
    // para não permitir espaço vazio
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String senhaHash;

    @NotBlank
    @Column(nullable = false)
    private String papel;
}
