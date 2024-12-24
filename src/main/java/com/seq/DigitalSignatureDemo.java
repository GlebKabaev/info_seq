package com.seq;

import java.security.*;
import java.util.Base64;

public class DigitalSignatureDemo {
    public static void main(String[] args) throws Exception {
        // Генерация пары ключей
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        // Исходное сообщение
        String message = "Это сообщение будет подписано.";

        // Создание подписи
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes());
        byte[] digitalSignature = signature.sign();

        System.out.println("Цифровая подпись: " + Base64.getEncoder().encodeToString(digitalSignature));

        // Проверка подписи
        signature.initVerify(publicKey);
        signature.update(message.getBytes());
        boolean isVerified = signature.verify(digitalSignature);

        System.out.println("Подпись верна: " + isVerified);
    }
}
