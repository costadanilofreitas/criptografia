package com.criptografia;

import com.criptografia.models.Answer;
import com.criptografia.services.AnswerService;
import org.springframework.http.ResponseEntity;

public class Decodifica {
    private AnswerService answerService;

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
