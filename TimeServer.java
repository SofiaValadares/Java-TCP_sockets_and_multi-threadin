import java.io.*;
import java.net.*;
import java.util.Date;
 
/**
 * This program demonstrates a simple TCP/IP socket server.
 *
 * @author www.codejava.net
 */
public class TimeServer {
 
    public static void main(String[] args) {
        if (args.length < 1) return;
 
        int port = Integer.parseInt(args[0]);
        Validation validator = new Validation();
 
        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            System.out.println("Server is listening on port " + port);

            int sum = 0; //Change to various pcs when treating multithreading
            Socket socket = serverSocket.accept();
            System.out.println("New client connected"); //Add Client ID when treating multithreads
            while (true) {

                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                String line = reader.readLine();

                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);

                int readNumber = validator.validInteger(line);

                if(readNumber == -1) {
                    writer.println("Please input a valid number");
                } else {
                    sum += readNumber;
                    writer.println("Number read: "+readNumber+" and summed. Current sum "+sum);
                }

            }
 
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}