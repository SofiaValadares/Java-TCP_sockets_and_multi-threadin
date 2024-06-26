package src.pt.fe.up.cpd.t4g11.main.view;

import src.pt.fe.up.cpd.t4g11.main.model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import static src.pt.fe.up.cpd.t4g11.main.controller.Server.sendMessageToClient;

public class GameScreen extends JFrame {
    private final JTextArea outputArea;
    private final JTextField inputField; // Novo campo de entrada
    public Integer lastInput = null;
    private final Player client;
    private boolean open;

    public GameScreen(Player client) {
        this.client = client;
        setTitle("Client - "+ client.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    sendMessageToClient(client.getPlayerSocket(), "exit");
                    client.disconnect();
                    setVisible(false);
                    open = false;
                } catch (IOException i) {
                    i.getStackTrace();
                }
            }
        });
        setSize(600, 400);
        setLayout(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBackground(Color.BLACK);
        outputArea.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        // Inicializa e configura o campo de entrada
        inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = inputField.getText().trim();
                if (!input.isEmpty()) {
                    // Aqui você pode manipular o número inteiro fornecido pelo usuário
                    lastInput = Integer.parseInt(input);
                    // Exemplo de exibição do número na área de saída
                    // Limpa o campo de entrada após processamento
                    inputField.setText("");
                }
            }
        });
        add(inputField, BorderLayout.SOUTH);

        openScreen();
    }

    public boolean isOpen() {
        return open;
    }

    public void openScreen() {
        setVisible(true);
        open = true;
    }

    public Player getClient() {
        return client;
    }

    public void displayMessage(String message) {
        // Limpa o conteúdo atual da área de saída
        outputArea.setText("");
        // Exibe a nova mensagem na área de saída
        outputArea.append("\n > " + message + "\n");
    }

    public void displayMessageLow(String message) {
        // Exibe a nova mensagem na área de saída
        outputArea.append(message + "\n");
    }
}
