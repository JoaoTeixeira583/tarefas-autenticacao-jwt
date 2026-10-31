package com.example.TarefasAutenticacao.repository;

import com.example.TarefasAutenticacao.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    // para encontrar o email
    Optional<Usuario> findByEmail(String email);

    // para ver se o email e duplicado ou não
    boolean existsByEmail(String email);
}
