package src.pt.fe.up.cpd.t4g11.main.controller;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) return;

        Scanner scanner = new Scanner(System.in);

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        System.out.print("Nick name: ");
        String nickName = scanner.nextLine();

        try (Socket socket = new Socket(hostname, port)) {
            System.out.println("Connected to server.");

            // Envie o nome do jogador para o servidor
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(nickName);

            Thread notifier = new Thread(new ClientToServerNotifier(out));
            notifier.start();

            Thread receiver = new Thread(new ServerToClientReciever(serverReader));
            receiver.start();

            // Loop para enviar mensagens adicionais para o servidor
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
        } catch (ConnectException c) {
            System.out.println("Connection to server refused!");
        } catch (IOException e) {
            // Tratar outras exceções, se necessário
            e.printStackTrace();
        }
    }

}
