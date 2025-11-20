package com.ecowork.messaging;

import com.ecowork.config.RabbitConfig;
import com.ecowork.models.LogEvento;
import com.ecowork.repository.LogEventoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumoConsumer {

    private final LogEventoRepository logEventoRepository;

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void receive(ConsumoEvent event) {

        log.info("📥 Evento recebido na fila: {}", event);

        LogEvento logEvento = LogEvento.builder()
                .tipo("REGISTRO_CONSUMO")
                .mensagem("Registro " + event.getRegistroId() +
                        " criado pelo usuário " + event.getUsuarioId() +
                        " | Tipo: " + event.getTipoConsumo() +
                        " | Valor: " + event.getValor())
                .dataHora(LocalDateTime.now())
                .build();

        logEventoRepository.save(logEvento);

        log.info("💾 Log de auditoria salvo com sucesso.");
    }
}