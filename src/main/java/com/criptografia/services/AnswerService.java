package com.criptografia.services;

import com.criptografia.models.Answer;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

@Service
public class AnswerService {
    @Value("${token}")
    private String token;

    public Answer recuperaAnswer(){
        String recuperaUrl = "https://api.codenation.dev/v1/challenge/dev-ps/generate-data?token=";

        RestTemplate restTemplate = new RestTemplate();

        Answer answer = restTemplate.getForObject(recuperaUrl + token, Answer.class);

        return answer;
    }

    public String decodificaAnswer(String cifrado, int casas){
        String decifra;
        Character c;
        String aux;

        StringBuilder sb = new StringBuilder();

        cifrado = cifrado.toLowerCase();

        // Descriptografa cada caracter por vez
        for(int i=0;i<cifrado.length();i++){
            c = cifrado.charAt(i);
            aux = c.toString();
            int letraDecifradaASCII;
            if (aux.matches("[a-z]*")){
                // Transforma o caracter em codigo ASCII e faz a descriptografia
                letraDecifradaASCII = ((int) cifrado.charAt(i)) - casas;
                //Depois do A volta para o Z
                if (letraDecifradaASCII < 97){
                    int result = 97 - letraDecifradaASCII;
                    letraDecifradaASCII = 123 - result;
                }
            }else {
                // Transforma o caracter em codigo ASCII e faz a descriptografia
                letraDecifradaASCII = ((int) cifrado.charAt(i));
            }

            // Verifica se o codigo ASCII esta no limite dos caracteres imprimiveis
            while(letraDecifradaASCII < 32)
                letraDecifradaASCII += 94;

            // Transforma codigo ASCII descriptografado em caracter ao novo texto
            sb.append( (char)letraDecifradaASCII );
        }

        decifra = sb.toString().toLowerCase();

        System.out.println("decifra" + decifra );

        return decifra;
    }

    public String resumoAnswer(String decifrado){
        String sha1 = "";

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(decifrado.getBytes("utf8"));
            sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println( "The sha1 of \""+ decifrado + "\" is:");
        System.out.println( sha1 );

        return sha1;
    }

    public ResponseEntity<Answer> enviaAnswer(Answer answer){
        String enviaUrl = "https://api.codenation.dev/v1/challenge/dev-ps/submit-solution?token=";

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("numero_casas", answer.getNumero_casas());
        jsonObj.put("token", answer.getToken());
        jsonObj.put("cifrado", answer.getCifrado());
        jsonObj.put("decifrado", answer.getDecifrado());
        jsonObj.put("resumo_criptogratico", answer.getResumo_criptografico());

        FileWriter writeFile = null;

        try{
            writeFile = new FileWriter("answer.json");
            //Escreve no arquivo conteudo do Objeto JSON
            writeFile.write(jsonObj.toJSONString());
            writeFile.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("file", "answer");
        HttpEntity<JSONObject> request = new HttpEntity<JSONObject>(jsonObj,  headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Answer> response = restTemplate.postForEntity( enviaUrl + token, request , Answer.class);
        System.out.println( response );

        return response;
    }
}
