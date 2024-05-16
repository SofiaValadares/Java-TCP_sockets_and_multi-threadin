package src.pt.fe.up.cpd.t4g11.main.model;

import src.pt.fe.up.cpd.t4g11.main.controller.GameScreenManager;
import src.pt.fe.up.cpd.t4g11.main.view.GameScreen;

import static java.lang.Thread.sleep;
import static src.pt.fe.up.cpd.t4g11.main.controller.Server.players;
import static src.pt.fe.up.cpd.t4g11.main.controller.Server.printMessageInServer;

public class Game implements Runnable {
    
    private static short lastGameId = 0;
    private final short gameId;

    public Game() {
        lastGameId++;
        this.gameId = lastGameId;

    }

    public short getGameId() {
        return this.gameId;
    }

    @Override
    public void run() {
        short gameId = (short)(lastGameId-1);
        GameScreenManager screenManager = new GameScreenManager(gameId);
        while(true) {
            playGame(screenManager, gameId);
        }
    }

    private static void playGame(GameScreenManager screenManager, short gameId) {
        boolean ready = loadingScreen(screenManager,gameId);
        if(ready) {
            while (true) {
                if(!allPlayersInGame(gameId)) {
                    reconnectionChecker(screenManager,gameId);
                }
                screenChecker(screenManager, gameId);
            }
        } else {
            for (GameScreen screen: screenManager.getScreens())
                screen.dispose();
            Thread.currentThread().interrupt();
            removePlayersFromGame();
        }
    }

    private static void screenChecker(GameScreenManager screenManager, short gameId) {
        synchronized (players) {
            for (Player player: players) {
                if(player.isConnected() && player.getGameId() == gameId) {
                    for(GameScreen screen: screenManager.getScreens()){
                        if(screen.getClient().equals(player.getName()) && !screen.isOpen()) {
                            screen.openScreen();
                            printMessageInServer("Opened screen");
                        }
                    }
                }
            }
        }
    }

    private static void removePlayersFromGame() {
        synchronized (players) {
            for(Player player : players)
                player.setGameId((short) -1);
        }
    }

    private static boolean loadingScreen(GameScreenManager screenManager, short gameId) {
        byte time = 20; boolean gameReady = true;
        while (time >= 0) {
            if(allPlayersInGame(gameId)) {
                for (GameScreen screen : screenManager.getScreens()) {
                    screen.displayMessage("Minimum of 2 players reached. Game starts in " + time + " seconds...");
                }
                screenChecker(screenManager, gameId);
            }
            else {
                gameReady = false;
                time = 60;
                while (time >= 0) {
                    for(GameScreen screen: screenManager.getScreens())
                        screen.displayMessage("Game start canceled, wait for player to reconnect in " + time + " seconds");
                    if(allPlayersInGame(gameId)) {
                        time = 20;
                        gameReady = true;
                        break;
                    }
                    try {
                        sleep(1000); // Wait for 1 second between each iteration
                    } catch (InterruptedException e) {
                        e.getMessage();
                    }
                    time--;
                }

            }
            try {
                sleep(1000); // Wait for 1 second between each iteration
            } catch (InterruptedException e) {
                e.getMessage();
            }

            time--;
        }
        return gameReady;
    }

    private static boolean allPlayersInGame(short gameId) {
        for(Player player: players)
            if(player.getPlayerId() == gameId && player.isConnected())
                return true;

        return false;

    }

    private static void reconnectionChecker(GameScreenManager screenManager, short gameId) {
        short time = 60;
        while (time >= 0) {
            for(GameScreen screen: screenManager.getScreens()) {
                if(screen.isOpen())
                    screen.displayMessage("Player disconnected, wait for player to reconnect in " + time + " seconds");
            }

            if(allPlayersInGame(gameId)) {
                break;
            }
            try {
                sleep(1000); // Wait for 1 second between each iteration
            } catch (InterruptedException e) {
                e.getMessage();
            }
            time--;
        }
    }

}
