package com.example.TarefasAutenticacao.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("/teste")
    public String teste(){
        String texto = "Área do admin";

        return texto;
    }

}
