package src.pt.fe.up.cpd.t4g11.main.controller;


import src.pt.fe.up.cpd.t4g11.main.model.Game;
import src.pt.fe.up.cpd.t4g11.main.model.Player;
import src.pt.fe.up.cpd.t4g11.main.view.GameScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.*;
import static src.pt.fe.up.cpd.t4g11.main.controller.Server.players;

public class GameManager implements Runnable {

    private static final byte MAX_NUMBER_OF_GAMES = 10;

    @Override
    public synchronized void run() {
        while(true) {
            if(isGameStartable()) {
                Game game = new Game();
                setPlayersInGame(game.getGameId());
                Thread thread = new Thread(game);
                thread.start();
            }
        }
    }

    private static boolean isGameStartable() {
        int playerSize = 0;
        synchronized (players) {
            for (Player player: players)
                if(!player.isInGame() && player.isConnected()) {
                    playerSize++;
                    if(playerSize >= 2) return true;
                }
            return false;
        }

    }

    private synchronized static void setPlayersInGame(short gameId) {
        int playerSize = 0;
        while(playerSize != 2) {
            synchronized (players) {
                for (Player player: players)
                    if(!player.isInGame() && player.isConnected()) {
                        player.setInGame();
                        player.setGameId(gameId);
                        playerSize++;
                    }
            }
        }
    }

    // Do here later function to start a new game, frist need game class

}
