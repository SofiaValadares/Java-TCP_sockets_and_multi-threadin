package src.pt.fe.up.cpd.t4g11.main.controller;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    private static final String USERS_FILE = "users.txt";

    public static void main(String[] args) throws IOException {
        if (args.length < 2) return;

        Scanner scanner = new Scanner(System.in);

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        System.out.print("Nick name: ");
        String nickName = scanner.nextLine();

        boolean nameExists = checkNameExists(nickName);


        if (nameExists) {
            System.out.print("Password: ");
            while (true) {
                String password = scanner.nextLine();

                if (checkPasswordCorrect(nickName, password)) {
                    break;
                } else {
                    System.out.print("Wrong password, try again: ");
                }

            }


        } else {
            System.out.print("Creat your new password: ");
            String newPassword = scanner.nextLine();

            // Adicionar o nome e a senha ao arquivo
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
                writer.write(nickName + ";" + newPassword);
                writer.newLine();
                System.out.println("User registered successfully.");
            } catch (IOException e) {
                System.out.println("An error occurred while registering the user.");
                e.printStackTrace();
            }
        }

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

    private static boolean checkNameExists(String name) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 0 && parts[0].equals(name)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean checkPasswordCorrect(String nickName, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2 && parts[0].equals(nickName) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
