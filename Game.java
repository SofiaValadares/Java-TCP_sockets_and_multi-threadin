import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Game {
    private static int lastGameId = 0;
    private int gameId;
    public List<Player> playersList;
    public Game() {
        lastGameId++;
        this.gameId = lastGameId;
        this.playersList = new ArrayList<>();
    }

    public int getGameId() {
        return this.gameId;
    }

    public void setNewPlayer(String name) {
        boolean check = true;

        for (Player player : this.playersList) {
            if (player.getName().equals(name)) {
                check = false;
                break;
            }
        }

        if (check) {
            Player newPlayer = new Player(name);
            this.playersList.add(newPlayer);
        } else {
            System.out.println("Já existe um player com esse nome");
        }
    }

    public static void main(String[] args) {
        Game aaaa = new Game();
        boolean loop = true;
        Scanner scanner = new Scanner(System.in);

        while (loop) {
            System.out.println("Add new player?");

            int numeroInteiro = scanner.nextInt();

            if (numeroInteiro == 1) {
                System.out.println("Digite um nome");

                String name = scanner.next();
                aaaa.setNewPlayer(name);
                System.out.println(aaaa);
            } else {
                loop = false;
            }
        }

        for (Player player : aaaa.playersList) {
            System.out.println("Nome do jogador: " + player.getName());
            // Adicione outros detalhes do jogador, se necessário
        }
    }
}
