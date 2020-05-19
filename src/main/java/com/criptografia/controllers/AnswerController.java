package com.criptografia.controllers;

import com.criptografia.models.Answer;
import com.criptografia.services.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/answer")
public class AnswerController {
    @Autowired
    private AnswerService answerService;

    @GetMapping
    public boolean decodificaCasas(){
        Answer answer = answerService.recuperaAnswer();
        String decifra = answerService.decodificaAnswer(answer.getCifrado(), answer.getNumero_casas());
        answer.setDecifrado(decifra);
        String sha1 = answerService.resumoAnswer(decifra);
        answer.setResumo_criptografico(sha1);
        ResponseEntity<Answer> resultado = answerService.enviaAnswer(answer);

        return true;
    }
}
