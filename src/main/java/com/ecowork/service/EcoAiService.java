package com.ecowork.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EcoAiService {

    private final ChatClient chatClient;

    public EcoAiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String gerarSugestao(String pergunta) {

        log.info("💬 Enviando pergunta para Spring AI: {}", pergunta);

        String resposta = chatClient
                .prompt()
                .messages(
                        new SystemMessage("""
                                Você é o EcoBot, assistente da plataforma EcoWork.
                                Ajuda empresas a reduzirem seu impacto ambiental.
                                Responda sempre em português brasileiro, de forma prática e objetiva,
                                com foco em ações de sustentabilidade corporativa que podem ser
                                adotadas por colaboradores e gestores.
                                """),
                        new UserMessage(pergunta)
                )
                .call()
                .content();

        log.info("🤖 Resposta da IA recebida com sucesso.");

        return resposta;
    }
}