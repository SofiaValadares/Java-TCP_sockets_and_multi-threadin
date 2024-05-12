package src.pt.fe.up.cpd.t4g11.main.controller;


import src.pt.fe.up.cpd.t4g11.main.model.Game;
import src.pt.fe.up.cpd.t4g11.main.model.Player;
import src.pt.fe.up.cpd.t4g11.main.view.GameScreen;

import static java.lang.Thread.*;
import static src.pt.fe.up.cpd.t4g11.main.controller.Server.players;

public class GameManager implements Runnable {

    private static short connectedPlayers = 0;
    public static boolean gameActive = true;

    @Override
    public void run() {
        Game theGame = new Game();
        setPlayersInGame((short) theGame.getGameId());
        GameScreen gameScreen = new GameScreen();

        if(loadingScreen(gameScreen, theGame)) {
            while (true) {



                try {
                    sleep(5000); // Verifica a cada 5 segundos
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static boolean loadingScreen(GameScreen gameScreen, Game game) {
        int time = 20;
        do {
            gameScreen.displayMessage("Minimum of 2 players reached. Game starts in " + time + " seconds...");
            checkConnection((short) game.getGameId());

            try {
                sleep(1000); // Wait for 1 second between each iteration
            } catch (InterruptedException e) {
                e.getMessage();
            }

            time--;
        } while (time > 0 && connectedPlayers >= 2);

        if (time == 0) {
            gameScreen.displayMessage("GAME START!!!!");
        } else {
            gameScreen.displayMessage("Game start canceled, not enough players :(");
            gameActive = false;
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            gameScreen.dispose();
            Thread.currentThread().interrupt();
        }

        return gameActive;
    }

    private static void checkConnection(short gameId) { // TODO: Finish reconnect
        short amountConnected = 0;
        for(Player player : players) {
            if(gameId == player.getGameId()) {
                if(player.isConnected()) {
                    amountConnected++;
                } else {
                    player.removeFromGame();
                }
            }
        }
        connectedPlayers = amountConnected;
    }

    private static void playGame() {

    }

    private synchronized static void setPlayersInGame(short gameId) {
        int playerSize = 0;
        while(playerSize != 2) {
            for (Player player: players)
                if(!player.isInGame()) {
                    player.setInGame();
                    player.setGameId(gameId);
                    playerSize++;
                }
        }
    }

    // Do here later function to start a new game, frist need game class

}
