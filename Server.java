import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Server {
    private static final Queue<String> playerQueue = new LinkedList<>();
    private static final ConcurrentMap<String, Socket> playerSockets = new ConcurrentHashMap<>();


    public static void main(String[] args) {
        if (args.length < 1) return;
        int port = Integer.parseInt(args[0]);

        Thread playersManeger = new Thread(new PlayersManeger());
        playersManeger.start();

        Thread heartbeatThread = new Thread(new HeartbeatMonitor());
        heartbeatThread.start();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Waiting for clients...");

            // loop to add new players
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                // Read player name from client
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String playerName = reader.readLine();

                // Player is arready conected?
                if (playerSockets.containsKey(playerName)) {
                    System.out.println("Player " + playerName + " is already connected.");

                    // Send error message to client
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                    writer.println("Error: Player is already connected.");
                    writer.close();

                    // Close connection
                    clientSocket.close();

                    continue; // Skip adding player to the queue and map
                }

                // Add player to the queue and map
                playerSockets.put(playerName, clientSocket);
                synchronized (playerQueue) {
                    if (playerQueue.contains(playerName)) {
                        System.out.println("Player " + playerName + " returns to the queue.");
                    } else {
                        playerQueue.offer(playerName);

                        // Thead to verify in player is conected
                        //Thread heartbeatThread = new Thread(new HeartbeatMonitor());
                        //heartbeatThread.start();
                    }
                }


                System.out.print("Connected players: ");
                for (String playerNameConnected : playerSockets.keySet()) {
                    System.out.print(playerNameConnected + ", ");
                }
                System.out.println();



            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // To see if some player disconected
    static class HeartbeatMonitor implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    // Iterar sobre as chaves do mapa
                    for (String playerName : playerSockets.keySet()) {
                        Socket socket = playerSockets.get(playerName);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        // Verificar se há um "heartbeat" do jogador
                        if (reader.readLine() == null) {
                            // Player disconnected, remove from the map
                            playerSockets.remove(playerName);
                            System.out.println("Player " + playerName + " disconnected (no heartbeat).");
                        }
                    }
                } catch (IOException e) {
                    // Player disconnected due to IOException, remove from the map
                    e.printStackTrace();
                }
            }
        }
    }

    // To verify if is possible start a new game
    static class PlayersManeger implements Runnable {
        private static final int MIM_PLAYERS_THRESHOLD = 2;
        @Override
        public void run() {
            while (true) {
                int numPlayersConnected = playerSockets.size();

                if (numPlayersConnected >= MIM_PLAYERS_THRESHOLD) {
                    GameScreen gameScreen = new GameScreen();

                    int time;

                    for (time = 20; time > 0; time--) {
                        if (playerSockets.size() < MIM_PLAYERS_THRESHOLD) {
                            break;
                        }

                        gameScreen.displayMessage("Minimum of 2 player reached. Game start in " + time + " seconds...");

                        try {
                            Thread.sleep(1000); // Espera 1 segundo entre cada iteração
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if (time == 0) {
                        gameScreen.displayMessage("GAME START!!!!");
                    } else {
                        gameScreen.displayMessage("Game start canceld for not enogh players :(");

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        gameScreen.dispose();
                    }

                }


                try {
                    Thread.sleep(5000); // Verifica a cada 5 segundos
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // Do here later function to start a new game, frist need game class

    }


    // Do here class to thread play game, frinst need game class

}
