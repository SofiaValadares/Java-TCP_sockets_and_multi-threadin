import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    private static final Set<String> connectedPlayers = new HashSet<>();
    private static final Set<String> priorityPlayers = new HashSet<>();

    public static void main(String[] args) {
        if (args.length < 1) return;
        int port = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                // Cria uma nova instância de ClientHandler
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void playersPriorityQueue(String playerName) {
        if (priorityPlayers.contains(playerName)) {
            System.out.println("Player " + playerName + " return to his position on the queue.");
        } else {
            System.out.println(("New player " + playerName + " in the queue"));
            priorityPlayers.add(playerName);
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private String playerName;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ) {
                // Lê o nome do jogador do cliente
                playerName = in.readLine();

                synchronized (connectedPlayers) {
                    if (connectedPlayers.contains(playerName)) {
                        System.out.println("Player " + playerName + " is already connected, connection will be ended.");
                        return; // Encerra a conexão com o cliente
                    } else {
                        // Adiciona o jogador à lista de jogadores conectados
                        connectedPlayers.add(playerName);

                        if (priorityPlayers.contains(playerName)) {
                            System.out.println("Player " + playerName + " return to the queue.");
                        } else {
                            priorityPlayers.add(playerName);
                            System.out.println("New player " + playerName + " connected.");
                        }
                    }
                }

                // Se o jogador não estiver conectado anteriormente, continua a lógica do jogo...

                // Exemplo: Enviar uma mensagem de boas-vindas para o jogador
                out.println("Welcome, " + playerName + "!");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
