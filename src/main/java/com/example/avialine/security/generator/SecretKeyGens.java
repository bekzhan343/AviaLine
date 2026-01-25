package com.example.avialine.security.generator;


import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

public class SecretKeyGens {
    public static void main(String[] args) {

        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        String s = Base64.getEncoder().encodeToString(key.getEncoded());

        System.out.println(s);

    }
}
