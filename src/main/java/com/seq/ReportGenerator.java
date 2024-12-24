package com.seq;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ReportGenerator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ввод данных
        System.out.print("Введите дату рождения (формат ДДММГГГГ): ");
        int dateOfBirth = scanner.nextInt();
        System.out.print("Введите значение D: ");
        int D = scanner.nextInt();
        System.out.print("Введите значение W: ");
        int W = scanner.nextInt();

        // Вычисление Y
        int X = dateOfBirth;
        int Y = (X % 100) * D + W;

        // Создание Word-документа
        try (XWPFDocument document = new XWPFDocument();
             FileOutputStream out = new FileOutputStream("report.docx")) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("Отчет");
            run.addBreak();
            run.setText("Дата рождения: " + dateOfBirth);
            run.addBreak();
            run.setText("Функция Y = (X mod 100) * D + W = " + Y);

            document.write(out);
            System.out.println("Отчет сохранен в файл report.docx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

