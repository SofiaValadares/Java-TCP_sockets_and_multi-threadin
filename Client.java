import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(nickName);

            ScheduledExecutorService listenerExecutor = Executors.newSingleThreadScheduledExecutor();
            ScheduledExecutorService senderExecutor = Executors.newSingleThreadScheduledExecutor();


            // Tarefa para ouvir as mensagens do servidor
            listenerExecutor.scheduleWithFixedDelay(() -> {
                try {
                    String response;
                    while ((response = serverReader.readLine()) != null) {
                        System.out.println("\nServer: " + response);
                        if (response.equals("Error: Player is already connected.")) {
                            shutdownExecutors(listenerExecutor, senderExecutor);
                            System.exit(0);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed by server.");
                    shutdownExecutors(listenerExecutor, senderExecutor);
                    System.exit(0);
                }
            }, 0, 100, TimeUnit.MILLISECONDS);

            // Tarefa para enviar mensagens vazias para o servidor
            senderExecutor.scheduleWithFixedDelay(() -> out.println(""), 0, 100, TimeUnit.MILLISECONDS);


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

            shutdownExecutors(listenerExecutor, senderExecutor);
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException e) {
            // Tratar outras exceções, se necessário
            e.printStackTrace();
        }
    }

    private static void shutdownExecutors(ExecutorService... executors) {
        for (ExecutorService executor : executors) {
            if (executor != null) {
                executor.shutdown();
            }
        }
    }

}
