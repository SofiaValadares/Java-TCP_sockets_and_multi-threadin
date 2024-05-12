package src.pt.fe.up.cpd.t4g11.main.model;

import java.io.IOException;
import java.net.Socket;

public class Player {
    private static int lastPlayerId = 0;
    private final int playerId;
    private Socket playerSocket;
    private final String name;
    private final short points;
    private boolean inGame;
    private short gameId;

    public Player(String name, Socket playerSocket) {
        lastPlayerId++;
        this.playerId = lastPlayerId;
        this.gameId = -1;
        this.playerSocket = playerSocket;
        this.name = name;
        this.points = 0;
        this.inGame = false;
    }

    public int getPlayerId() {
        return  this.playerId;
    }

    public String getName() {
        return this.name;
    }

    public Socket getPlayerSocket() {
        return this.playerSocket;
    }

    public boolean isConnected() {
        try {
            if(this.playerSocket.getInputStream().read() != -1) {
                return true;
            }
        } catch (IOException i) {
            i.getStackTrace();
            return false;
        }
        return false;
    }

    public void disconnect() throws IOException {
        this.playerSocket.close();
    }

    public short getPoints() {
        return points;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void removeFromGame() {
        this.inGame = false;
    }

    public void setInGame() {
        this.inGame = true;
    }

    public void setPlayerSocket(Socket newSocket) {
        this.playerSocket = newSocket;
    }

    public short getGameId() {
        return gameId;
    }

    public void setGameId(short gameId) {
        this.gameId = gameId;
    }
}
