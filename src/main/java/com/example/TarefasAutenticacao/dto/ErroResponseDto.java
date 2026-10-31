package com.example.TarefasAutenticacao.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// para não aparecer campo null no json
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErroResponseDto {

    private int status;
    private String mensagem;
    private LocalDateTime dataHora;
    private List<String> erros;
}
