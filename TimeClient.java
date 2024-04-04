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

            writer.println("new Date()?".toString());

            int number = 0;
            Scanner read = new Scanner(System.in);

            while (true) {
                System.out.println("Deseja digitar um novo valor [Y/n]?");

                String option = read.nextLine();

                if (option.equals("Y")) {
                    number = read.nextInt();
                    writer.println(number);
                } else if (option.equals("n")) {
                    break;
                } else {
                    System.out.println("Digite uma opcao valida: Y-sim ou n-nao");
                }

            }



            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
            String time = reader.readLine();
 
            System.out.println(time);
 
 
        } catch (UnknownHostException ex) {
 
            System.out.println("Server not found: " + ex.getMessage());
 
        } catch (IOException ex) {
 
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}