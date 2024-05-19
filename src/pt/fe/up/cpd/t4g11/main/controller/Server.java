package src.pt.fe.up.cpd.t4g11.main.controller;

import src.pt.fe.up.cpd.t4g11.main.model.Game;
import src.pt.fe.up.cpd.t4g11.main.model.Player;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public static final List<Player> players = new ArrayList<>();
    private static final String USERS_FILE = "users.txt";

    public static void main(String[] args) {
        if (args.length < 1) return;
        int port = Integer.parseInt(args[0]);

        // Criar o arquivo de usuários se não existir
        createUsersFile();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            printMessageInServer("Server started. Waiting for clients...");
            GameManager gameManager = new GameManager();

            // loop to add new players
            while (true) {
                Socket clientSocket = getNewClient(serverSocket);
                if(clientSocket == null) continue;

                String playerName = readClientName(clientSocket);

                if (isPlayerConnected(playerName)) {
                    printMessageInServer("Player " + playerName + " is already connected.");

                    sendMessageToClient(clientSocket, "Error: Player is already connected.");

                    clientSocket.close();

                    continue; // Skip adding player to the queue and map
                }

                addPlayer(playerName, clientSocket);

                if(gameManager.isGameStartable()) {
                    Game game = new Game();
                    gameManager.setPlayersInGame(game.getGameId());
                    Thread thread = new Thread(game);
                    thread.start();
                }

                printPlayersInQueue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createUsersFile() {
        File file = new File(USERS_FILE);
        try {
            if (file.createNewFile()) {
                System.out.println("User file created: " + file.getName());
            } else {
                System.out.println("User file already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the user file.");
            e.printStackTrace();
        }
    }

    private static void addPlayer(String playerName, Socket clientSocket) {
        // Add player to the queue
        Player player = new Player(playerName, clientSocket);
        boolean returned = false;
        synchronized (players) {
            for(Player listPlayer : players)
                if(listPlayer.getName().equals(player.getName())) {
                    listPlayer.setPlayerSocket(clientSocket);
                    System.out.println("Player " + playerName + " returns to the queue.");
                    returned = true;
                }

            if(!returned) { players.add(player); }
        }
    }

    private static void printPlayersInQueue() throws IOException{
        printMessageInServer("Players waiting for game: ", false);
        synchronized (players) {
            for (Player player : players) {
                if(player.isConnected() && !player.isInGame())
                    printMessageInServer(player.getName() + ", ", false);
            }
            printMessageInServer("");
        }
    }

    private static Socket getNewClient(ServerSocket serverSocket) {
        Socket clientSocket; // TODO: Can the socket be different when reconnecting?
        try {
            clientSocket = serverSocket.accept();
            return clientSocket;
        } catch (IOException i) {
            printMessageInServer("Error in accepting connection from client");
            return null;
        }
    }

    private static String readClientName(Socket clientSocket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        return reader.readLine();
    }

    public static void printMessageInServer(String message) {
        printMessageInServer(message, true);
    }

    private static void printMessageInServer(String message, boolean newline) {
        if(newline) System.out.println(message);
        else System.out.print(message);
    }

    private static boolean isPlayerConnected(String playerName) {
        synchronized (players) {
            for (Player player : players)
                if(player.getName().equals(playerName) && player.isConnected())
                    return true;

            return false;
        }
    }

    public static void sendMessageToClient(Socket clientSocket, String message) throws IOException {
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        writer.println(message);
    }

    // Do here class to thread play game, for instance need game class
}
