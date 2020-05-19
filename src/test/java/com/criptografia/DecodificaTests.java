package com.criptografia;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DecodificaTests {
    @Autowired
    Decodifica decodifica;

    @Test
    public void testarDecodifica(){
        Assertions.assertTrue(decodifica.decodificaCasas());
    }

}
