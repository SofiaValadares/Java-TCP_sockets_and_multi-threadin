import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 * This program demonstrates a simple TCP/IP socket client.
 *
 * @author www.codejava.net
 */
public class TimeClient {
 
    public static void main(String[] args) {
        if (args.length < 2) return;
 
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
 
        try (Socket socket = new Socket(hostname, port)) {
 
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            InputStream input = socket.getInputStream();
            BufferedReader server = new BufferedReader(new InputStreamReader(input));

            int number;
            BufferedReader commandLine = new BufferedReader(new InputStreamReader(System.in));
            String option, result;

            do {
                System.out.print("Client - Digite um valor: ");
                number = Integer.parseInt(commandLine.readLine());
                writer.println(number);
                result = server.readLine();
                System.out.println("Server reply - " + result);
                System.out.println("Client - Deseja digitar um novo valor [Y/n]?");
                option = commandLine.readLine();
                if(!option.equals("Y")) {
                    break;
                }
            } while (true);
 
        } catch (UnknownHostException ex) {
 
            System.out.println("Server not found: " + ex.getMessage());
 
        } catch (IOException ex) {
 
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}