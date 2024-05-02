import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) return;

        Scanner scanner = new Scanner(System.in);

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        // Fazer aqui o sistema de login do player, tipo pegar o nome e fazer login
        System.out.print("Nick name: ");
        String nickName = scanner.nextLine();

        try (Socket socket = new Socket(hostname, port)) {
            System.out.println("Connected to server.");

            // Envie o nome do jogador para o servidor
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(nickName);

            // Crie um thread para ler continuamente as mensagens do servidor
            Thread serverListenerThread = new Thread(() -> {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String response;
                    while ((response = reader.readLine()) != null) {
                        System.out.println("\nServer: " + response);

                        if (response.equals("Error: Player is already connected.")) {
                            System.exit(0);
                        }
                    }
                } catch (IOException e) {
                    // Se uma SocketException for lançada, a conexão foi fechada pelo servidor
                    System.out.println("Connection closed by server.");
                    System.exit(0);
                }
            });
            serverListenerThread.start();

            // Loop para enviar mensagens para o servidor
            while (true) {
                System.out.print("Enter message (or type 'exit' to quit): ");
                String message = scanner.nextLine();

                // Se o usuário digitar 'exit', sair do loop
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }

                // Enviar a mensagem para o servidor
                out.println(message);
            }
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException e) {
            // Tratar outras exceções, se necessário
            e.printStackTrace();
        }
    }
}
