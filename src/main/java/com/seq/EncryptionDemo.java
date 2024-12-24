package com.seq;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class EncryptionDemo {
    public static void main(String[] args) throws Exception {
        // Генерация ключа
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();

        // Исходное сообщение
        String message = "Это сообщение нужно зашифровать.";

        // Шифрование
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedMessage = cipher.doFinal(message.getBytes());

        System.out.println("Зашифрованное сообщение: " + Base64.getEncoder().encodeToString(encryptedMessage));

        // Дешифрование
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedMessage = cipher.doFinal(encryptedMessage);

        System.out.println("Дешифрованное сообщение: " + new String(decryptedMessage));
    }
}

