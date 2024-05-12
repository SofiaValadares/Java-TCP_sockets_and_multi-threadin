package src.pt.fe.up.cpd.t4g11.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Teste extends JFrame {
    private JTextArea outputArea;
    private JTextField inputField;

    public Teste() {
        setTitle("Terminal Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBackground(Color.BLACK);
        outputArea.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        inputField = new JTextField();
        inputField.setBackground(Color.BLACK);
        inputField.setForeground(Color.WHITE);
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleInput();

            }
        });
        add(inputField, BorderLayout.SOUTH);

        initGUI();
    }

    private void initGUI() {
        setVisible(true);
    }

    private void handleInput() {
        String input = inputField.getText();
        outputArea.append("> " + input + "\n");


    }

    public String getInput() {
        return inputField.getText();
    }

    public void testTestoAA(String string_aqui) {
        outputArea.append(string_aqui);
    }

    // Função para passar o texto que será digitado pelo terminal
    public void setInputText(String text) {
        inputField.setText(text);
    }

    // Função para pegar o texto que foi exibido no terminal
    public String getOutputText() {
        inputField.setText("");
        return outputArea.getText();
    }

    public static void main(String[] args) {
        Teste teste = new Teste();


        // Exemplo de uso: passando o texto para ser digitado pelo termina

        // Aqui você pode fazer qualquer outra coisa que precisa ser feita na main

        // Exemplo de uso: pegando o texto que foi exibido no terminal
        String outputText = teste.getOutputText();

        teste.testTestoAA("hello word!!\n");
        System.out.println("Texto exibido no terminal:\n" + outputText);
    }
}
