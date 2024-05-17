package src.pt.fe.up.cpd.t4g11.main.controller;

import src.pt.fe.up.cpd.t4g11.main.model.Game;
import src.pt.fe.up.cpd.t4g11.main.model.Player;
import src.pt.fe.up.cpd.t4g11.main.view.GameScreen;

import java.util.ArrayList;
import java.util.List;

import static src.pt.fe.up.cpd.t4g11.main.controller.Server.players;

public class GameScreenManager {
    private final List<GameScreen> screens;
    private final short gameId;

    public GameScreenManager(short gameId) {
        this.gameId = gameId;
        screens = new ArrayList<>();
        createScreens();
    }

    public void createScreens() {
        for(Player player : players) {
            if(player.getGameId() == gameId) {
                GameScreen gameScreen = new GameScreen(player);
                screens.add(gameScreen);
            }
        }
    }

    public List<GameScreen> getScreens() {
        return screens;
    }

    public void displayMessageInScreens(String message) {
        for(GameScreen screen: screens) {
            screen.displayMessage(message);
        }
    }
}
