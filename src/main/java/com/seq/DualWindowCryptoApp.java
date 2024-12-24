package com.seq;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.*;
import java.util.Base64;

public class DualWindowCryptoApp {
    private static PublicKey publicKey;
    private static PrivateKey privateKey;
    private static byte[] digitalSignature;

    public static void main(String[] args) {
        // Создаем два окна
        JFrame senderFrame = createSenderFrame();
        JFrame receiverFrame = createReceiverFrame();

        senderFrame.setVisible(true);
        receiverFrame.setVisible(true);
    }

    private static JFrame createSenderFrame() {
        JFrame senderFrame = new JFrame("Отправитель");
        senderFrame.setSize(400, 300);
        senderFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        senderFrame.setLayout(new GridLayout(4, 1));

        JLabel label = new JLabel("Сообщение:");
        JTextArea messageArea = new JTextArea("Введите сообщение...");
        JButton generateKeysButton = new JButton("Сгенерировать ключи");
        JButton sendButton = new JButton("Зашифровать и подписать");

        senderFrame.add(label);
        senderFrame.add(new JScrollPane(messageArea));
        senderFrame.add(generateKeysButton);
        senderFrame.add(sendButton);

        // Обработчик для генерации ключей
        generateKeysButton.addActionListener(e -> {
            try {
                KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
                keyGen.initialize(2048);
                KeyPair pair = keyGen.generateKeyPair();
                publicKey = pair.getPublic();
                privateKey = pair.getPrivate();
                JOptionPane.showMessageDialog(senderFrame, "Ключи успешно сгенерированы!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(senderFrame, "Ошибка при генерации ключей!");
            }
        });

        // Обработчик для подписания и шифрования
        sendButton.addActionListener(e -> {
            try {
                String message = messageArea.getText();
                // Подписать сообщение
                Signature signature = Signature.getInstance("SHA256withRSA");
                signature.initSign(privateKey);
                signature.update(message.getBytes());
                digitalSignature = signature.sign();

                // Передача зашифрованного сообщения в окно получателя
                ReceiverWindow.receiveMessage(message, digitalSignature, publicKey);

                JOptionPane.showMessageDialog(senderFrame, "Сообщение подписано и отправлено!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(senderFrame, "Ошибка при подписании и шифровании сообщения!");
            }
        });

        return senderFrame;
    }

    private static JFrame createReceiverFrame() {
        return ReceiverWindow.createReceiverFrame();
    }
}

class ReceiverWindow {
    private static JTextArea receivedMessageArea = new JTextArea();
    private static byte[] receivedSignature;
    private static PublicKey receivedPublicKey;

    public static JFrame createReceiverFrame() {
        JFrame receiverFrame = new JFrame("Получатель");
        receiverFrame.setSize(400, 300);
        receiverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        receiverFrame.setLayout(new GridLayout(4, 1));

        JLabel label = new JLabel("Полученное сообщение:");
        JButton verifyButton = new JButton("Проверить подпись");

        receiverFrame.add(label);
        receiverFrame.add(new JScrollPane(receivedMessageArea));
        receiverFrame.add(verifyButton);

        // Обработчик для проверки подписи
        verifyButton.addActionListener(e -> {
            try {
                String receivedMessage = receivedMessageArea.getText();
                Signature signature = Signature.getInstance("SHA256withRSA");
                signature.initVerify(receivedPublicKey);
                signature.update(receivedMessage.getBytes());
                boolean isValid = signature.verify(receivedSignature);

                JOptionPane.showMessageDialog(receiverFrame, "Подпись " + (isValid ? "верна" : "неверна"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(receiverFrame, "Ошибка при проверке подписи!");
            }
        });

        return receiverFrame;
    }

    public static void receiveMessage(String message, byte[] signature, PublicKey publicKey) {
        receivedMessageArea.setText(message);
        receivedSignature = signature;
        receivedPublicKey = publicKey;
    }
}

