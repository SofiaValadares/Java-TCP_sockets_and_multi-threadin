package src.pt.fe.up.cpd.t4g11.main.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    
    private static int lastGameId = 0;
    private final int gameId;

    public Game() {
        lastGameId++;
        this.gameId = lastGameId;

    }

    public int getGameId() {
        return this.gameId;
    }
}
