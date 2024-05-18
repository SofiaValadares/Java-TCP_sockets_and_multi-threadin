package src.pt.fe.up.cpd.t4g11.main.model;

import java.util.Comparator;

public class PlayerPointsComparator implements Comparator<Player> {
    @Override
    public int compare(Player p1, Player p2) {
        return Short.compare(p2.getPoints(), p1.getPoints());
    }
}