package src.pt.fe.up.cpd.t4g11.main.model;

import src.pt.fe.up.cpd.t4g11.main.controller.GameScreenManager;
import src.pt.fe.up.cpd.t4g11.main.view.GameScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import static java.lang.Thread.sleep;
import static src.pt.fe.up.cpd.t4g11.main.controller.Server.*;

public class Game implements Runnable {
    
    private static short lastGameId = 0;
    private final short gameId;
    private List<Player> gamePlayers;

    public Game() {
        lastGameId++;
        this.gameId = lastGameId;
    }

    @Override
    public void run() {
        short gameId = lastGameId;
        gamePlayers = new ArrayList<>();
        getPlayers(gameId);
        GameScreenManager screenManager = new GameScreenManager(gameId);
        playGame(screenManager, gameId);
    }

    public void getPlayers(short gameId) {
        for(Player player: players)
            if(player.getGameId() == gameId)
                this.gamePlayers.add(player);
    }

    public short getGameId() {
        return this.gameId;
    }

    private void playGame(GameScreenManager screenManager, short gameId) {

        boolean ready = loadingScreen(screenManager,gameId);
        if(ready) {
            try {
                screenManager.displayMessageInScreens("Lets play!");
                sleep(2000);
            } catch (InterruptedException i) {
                printMessageInServer("Exception in game " + gameId);
                printMessageInServer(i.getMessage());
            }

            short round = 1;

            while (true) {
                if(!allPlayersInGame(gameId)) {
                    if(!reconnectionChecker(screenManager,gameId)) break;
                    continue;
                }
                screenChecker(screenManager, gameId);
                Calculation calc = MathGame.getRandomCalculation();
                screenManager.displayMessageInScreens("Round " + round +"/20");
                screenManager.displayMessageInScreensLow("There is new calculation to do: " + calc);
                // TODO: Wait for user input.
                short results = calc.getResult();
                Player winnerOfTheRound = screenManager.playersAnser(results);


                screenManager.displayMessageInScreensLow("\nPlayer " + winnerOfTheRound.getName() + " was the faster!!!\nAnser was " + results);
                // TODO: The player that answers first gets points using calc.getPoints() then move to next
                winnerOfTheRound.increasePoints(calc.givePoints());
                round++;



                try {
                    Thread.sleep(3000); // 3 segundos
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (round > 20) {
                    break;
                }
            }
        } else {
            for (GameScreen screen: screenManager.getScreens())
                screen.dispose();
            removePlayersFromGame(gameId);
            Thread.currentThread().interrupt();
        }

        if(noPlayerInGame(gameId)) {
            removePlayersFromGame(gameId);
            Thread.currentThread().interrupt();
        } else {
            //TODO: Display winner and close game
            screenManager.displayMessageInScreens("End of the game! Thanks for all who played!!!");
            Collections.sort(gamePlayers, new PlayerPointsComparator());

            byte rank = 1;

            for (Player player : gamePlayers) {
                screenManager.displayMessageInScreensLow(rank + "º " + player.getName() + " - "  + player.getPoints() + " points");
                rank++;
            }

            try {
                Thread.sleep(3000); // 3 segundos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void screenChecker(GameScreenManager screenManager, short gameId) {
        for (Player player: gamePlayers) {
            if(player.isConnected() && player.getGameId() == gameId) {
                for(GameScreen screen: screenManager.getScreens()){
                    if(screen.getClient().getName().equals(player.getName()) && !screen.isOpen()) {
                        screen.openScreen();
                    }
                }
            }
        }
    }

    private static void removePlayersFromGame(short gameId) {
        synchronized (players) {
            for(int i = 0; i < players.size(); i++) {
                if(players.get(i).getGameId() == gameId) {
                    try {
                        if(!players.get(i).getPlayerSocket().isClosed())
                            sendMessageToClient(players.get(i).getPlayerSocket(), "exit");
                    } catch (IOException e) {
                        printMessageInServer("Error in removing " + players.get(i).getName());
                        printMessageInServer(e.getMessage());
                    }
                    players.remove(players.get(i));
                }
            }
        }
    }

    private boolean loadingScreen(GameScreenManager screenManager, short gameId) {
        byte time = 20;
        boolean gameReady = true;
        String disconnectedPlayers;
        while (time >= 0) {
            if(allPlayersInGame(gameId))
                screenManager.displayMessageInScreens("Minimum of 2 players reached. Game starts in " + time + " seconds...");
            else {
                gameReady = false;
                time = 60;
                while (time >= 0) {
                    disconnectedPlayers = getDisconnectedPlayers();
                    screenManager.displayMessageInScreens("Game start canceled, wait for player(s) " + disconnectedPlayers + " to reconnect in " + time);
                    if(allPlayersInGame(gameId)) {
                        time = 20;
                        gameReady = true;
                        screenChecker(screenManager, gameId);
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

    private byte getAmountOfPlayersInGame(short gameId) {
        byte sum = 0;
        for(Player player: gamePlayers)
            if(player.getGameId() == gameId && player.isConnected())
                sum++;

        return sum;
    }

    public String getDisconnectedPlayers() {
        StringBuilder disconnected = new StringBuilder();
        for(Player player : gamePlayers)
            if(!player.isConnected())
                disconnected.append(player.getName()).append(", ");

        if(disconnected.length() >= 2) {
            disconnected.deleteCharAt(disconnected.length()-1); // Remove whitespace
            disconnected.deleteCharAt(disconnected.length()-1); // Remove comma
        }
        return disconnected.toString();
    }

    private boolean allPlayersInGame(short gameId) {
        return getAmountOfPlayersInGame(gameId) == gamePlayers.size();
    }

    private boolean noPlayerInGame(short gameId) {
        return getAmountOfPlayersInGame(gameId) == 0;
    }

    private boolean reconnectionChecker(GameScreenManager screenManager, short gameId) {
        short time = 60;
        String disconnectedPlayers;
        while (time >= 0) {
            if(noPlayerInGame(gameId)) {
                printMessageInServer("All players in game " + gameId + " disconnected. Waiting for " + time + " seconds until they reconnect");
            } else {
                disconnectedPlayers = getDisconnectedPlayers();
                for(GameScreen screen: screenManager.getScreens()) {
                    if(!screen.isOpen() && screen.getClient().isConnected()) screen.openScreen();
                    screen.displayMessage("Player(s) " + disconnectedPlayers + " disconnected, wait for player to reconnect in " + time + " seconds");
                }
            }

            if(allPlayersInGame(gameId)) {
                return true;
            }
            try {
                sleep(1000); // Wait for 1 second between each iteration
            } catch (InterruptedException e) {
                e.getMessage();
            }
            time--;
        }
        return false;
    }

}
