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
            System.out.println("Conected in server " + port);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(nickName);

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

        }

    }
}
