package src.pt.fe.up.cpd.t4g11.main.controller;

import src.pt.fe.up.cpd.t4g11.main.model.Player;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Server {

    public static final List<Player> players = new ArrayList<>();

    public static void main(String[] args) {
        if (args.length < 1) return;
        int port = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            printMessageInServer("Server started. Waiting for clients...");
            Thread heartbeatThread = new Thread(new HeartbeatMonitor());
            heartbeatThread.start();

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

                printPlayersInQueue();

                if(canStartGame()) {
                    startNewGame();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean canStartGame() {
        short counter = 0;
        for(Player player : players) {
            if(!player.isInGame() && player.isConnected()) {
                counter++;
            }
            if(counter == 2) {
                return true;
            }
        }

        return false;
    }

    private static void startNewGame() {
        Thread game = new Thread(new GameManager());
        game.start();
    }

    private static void addPlayer(String playerName, Socket clientSocket) {
        // Add player to the queue
        Player player = new Player(playerName,clientSocket);
        boolean returned = false;
        synchronized (players) {
            for(Player listPlayer : players)
                if(listPlayer.getName().equals(player.getName())) {
                    listPlayer.setPlayerSocket(clientSocket);
                    System.out.println("Player " + playerName + " returns to the queue.");
                    returned = true;
                }

            if(!returned) players.add(player);
        }
    }

    private static void printPlayersInQueue() {
        printMessageInServer("Connected players: ", false);
        for (Player player : players) {
            if(player.isConnected())
                printMessageInServer(player.getName() + ", ", false);
        }
        printMessageInServer("");
    }

    private static Socket getNewClient(ServerSocket serverSocket) {
        Socket clientSocket; // TODO: Can the socket be different when reconnecting?
        try {
            clientSocket = serverSocket.accept();
            printMessageInServer("New client connected: " + clientSocket);
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
        for (Player player : players)
            if(player.getName().equals(playerName) && player.isConnected())
                return true;

        return false;
    }

    public static void sendMessageToClient(Socket clientSocket, String message) throws IOException {
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        writer.println(message);
        writer.close();
    }

    // Do here class to thread play game, frinst need game class

}
