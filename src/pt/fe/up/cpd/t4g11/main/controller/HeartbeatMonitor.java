package src.pt.fe.up.cpd.t4g11.main.controller;

import src.pt.fe.up.cpd.t4g11.main.model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import static java.lang.Thread.sleep;
import static src.pt.fe.up.cpd.t4g11.main.controller.Server.players;


public class HeartbeatMonitor implements Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                synchronized (players) {
                    for (Player player : players) {
                        if(!player.isConnected()) {
                            player.disconnect();
                            System.out.println("Player " + player.getName() + " disconnected (no heartbeat).");
                        }
                    }
                }

                try {
                    sleep(5000);
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