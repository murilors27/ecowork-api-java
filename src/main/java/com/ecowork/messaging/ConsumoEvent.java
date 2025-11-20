package com.ecowork.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsumoEvent {
    private Long registroId;
    private Long usuarioId;
    private String tipoConsumo;
    private String valor;
}