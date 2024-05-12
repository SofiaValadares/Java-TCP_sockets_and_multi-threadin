package src.pt.fe.up.cpd.t4g11.main.view;

import javax.swing.*;
import java.awt.*;

public class GameScreen extends JFrame {
    private JTextArea outputArea;

    public GameScreen() {
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

        initGUI();
    }

    private void initGUI() {
        setVisible(true);
    }

    public void displayMessage(String message) {
        // Limpa o conteúdo atual da área de saída
        outputArea.setText("");
        // Exibe a nova mensagem na área de saída
        outputArea.append("\n > " + message + "\n");
    }

    public void displayMessageLow(String message) {
        // Exibe a nova mensagem na área de saída
        outputArea.append(message);
    }
}
