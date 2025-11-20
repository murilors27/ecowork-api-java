package com.ecowork.controller.api;

import com.ecowork.service.EcoAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final EcoAiService ecoAiService;

    @GetMapping("/perguntar")
    public String perguntar(@RequestParam String q) {
        return ecoAiService.gerarSugestao(q);
    }
}