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


        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Waiting for clients...");

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
                        Thread heartbeatThread = new Thread(new HeartbeatMonitor(playerName));
                        heartbeatThread.start();
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

    static class HeartbeatMonitor implements Runnable {
        private final String playerName;

        public HeartbeatMonitor(String playerName) {
            this.playerName = playerName;
        }

        @Override
        public void run() {
            Socket socket = playerSockets.get(playerName);
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (true) {
                    if (reader.readLine() == null) {
                        // Player disconnected, remove from the map
                        playerSockets.remove(playerName);
                        System.out.println("Player " + playerName + " disconnected (no heartbeat).");
                        break;
                    }
                }
            } catch (IOException e) {
                // Player disconnected due to IOException, remove from the map
                playerSockets.remove(playerName);
                System.out.println("Player " + playerName + " disconnected (IOException).");
            }
        }
    }
}
