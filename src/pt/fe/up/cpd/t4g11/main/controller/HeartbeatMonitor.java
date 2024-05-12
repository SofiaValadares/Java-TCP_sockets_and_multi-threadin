package src.pt.fe.up.cpd.t4g11.main.controller;

import src.pt.fe.up.cpd.t4g11.main.model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import static java.lang.Thread.sleep;
import static src.pt.fe.up.cpd.t4g11.main.controller.Server.players;
import static src.pt.fe.up.cpd.t4g11.main.controller.Server.*;

// To see if some player disconected
public class HeartbeatMonitor implements Runnable {
    @Override
    public synchronized void run() {
        while (true) {
            try {
                for (Player player : players) {
                    Socket socket = player.getPlayerSocket();
                    if(socket.isClosed()) {
                        player.disconnect();
                        System.out.println("Player " + player.getName() + " disconnected (no heartbeat).");
                    }
                }

                try {
                    sleep(10000); // Verifica a cada 10 segundos
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (SocketException i) {
                i.getMessage();
            } catch (IOException e) {
                // src.pt.fe.up.cpd.t4g11.main.model.Player disconnected due to IOException, remove from the map
                e.printStackTrace();
            }
        }
    }
}