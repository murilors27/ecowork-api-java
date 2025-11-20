package com.ecowork.dto.ai;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiRequestDTO {

    @NotBlank
    private String pergunta;
}