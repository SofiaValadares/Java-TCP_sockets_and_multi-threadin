package src.pt.fe.up.cpd.t4g11.main.controller;

import src.pt.fe.up.cpd.t4g11.main.model.Game;
import src.pt.fe.up.cpd.t4g11.main.model.Player;

import static src.pt.fe.up.cpd.t4g11.main.controller.Server.players;
import static src.pt.fe.up.cpd.t4g11.main.controller.Server.printMessageInServer;

public class GameManager {

    public boolean isGameStartable() {
        int playerSize = 0;
        synchronized (players) {
            if(players.size() < 2) return false;
            for (Player player: players)
                if(!player.isInGame() && player.isConnected()) {
                    playerSize++;
                    if(playerSize >= 2) {return true;}
                }
            return false;
        }

    }

    public void setPlayersInGame(short gameId) {
        int playerSize = 0;
        StringBuilder playersInGame = new StringBuilder();
        while(playerSize != 2) {
            synchronized (players) {
                for (Player player: players)
                    if(!player.isInGame() && player.isConnected()) {
                        player.setInGame();
                        player.setGameId(gameId);
                        playerSize++;
                        playersInGame.append(player.getName()).append(", ");
                    }
            }
        }

        playersInGame.append("were added to game ").append(gameId);
        printMessageInServer(playersInGame.toString());
    }

    // Do here later function to start a new game, frist need game class

}
