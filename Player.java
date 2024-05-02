public class Player {
    private static int lastPlayerId = 0;
    private int playerId;
    private String name;

    public Player(String name) {
        lastPlayerId++;
        this.playerId = lastPlayerId;
        this.name = name;
    }

    public int getPlayerId() {
        return  this.playerId;
    }

    public String getName() {
        return this.name;
    }
}
