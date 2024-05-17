package src.pt.fe.up.cpd.t4g11.main.model;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class Player {
    private static int lastPlayerId = 0;
    private final int playerId;
    private Socket playerSocket;
    private final String name;
    private final short points;
    private boolean inGame;
    private short gameId;
    private final ReentrantLock reentrantLock;

    public Player(String name, Socket playerSocket) {
        lastPlayerId++;
        this.playerId = lastPlayerId;
        this.gameId = -1;
        this.playerSocket = playerSocket;
        this.name = name;
        this.points = 0;
        this.inGame = false;
        this.reentrantLock = new ReentrantLock();
    }

    public int getPlayerId() {
        reentrantLock.lock();
        try {
            return this.playerId;
        } finally {
            reentrantLock.unlock();
        }

    }

    public String getName() {
        reentrantLock.lock();
        try {
            return this.name;
        } finally {
            reentrantLock.unlock();
        }

    }

    public Socket getPlayerSocket() {
        reentrantLock.lock();
        try {
            return this.playerSocket;
        } finally {
            reentrantLock.unlock();
        }

    }

    public boolean isConnected() {
        reentrantLock.lock();
        try {
            if(this.playerSocket.getInputStream().read() != -1) {
                reentrantLock.unlock();
                return true;
            }
        } catch (IOException i) {
            i.getStackTrace();
        }
        reentrantLock.unlock();
        return false;
    }

    public void disconnect() throws IOException {
        reentrantLock.lock();
        this.playerSocket.close();
        reentrantLock.unlock();
    }

    public short getPoints() {
        return points;
    }

    public boolean isInGame() {
        reentrantLock.lock();
        try {
            return inGame;
        } finally {
            reentrantLock.unlock();
        }

    }

    public void removeFromGame() {
        this.inGame = false;
    }

    public void setInGame() {
        reentrantLock.lock();
        this.inGame = true;
        reentrantLock.unlock();
    }

    public void setPlayerSocket(Socket newSocket) {
        reentrantLock.lock();
        this.playerSocket = newSocket;
        reentrantLock.unlock();
    }

    public short getGameId() {
        reentrantLock.lock();
        try {
            return gameId;
        } finally {
            reentrantLock.unlock();
        }

    }

    public void setGameId(short gameId) {
        reentrantLock.lock();
        this.gameId = gameId;
        reentrantLock.unlock();
    }
}
