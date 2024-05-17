package src.pt.fe.up.cpd.t4g11.main.controller;

import java.io.BufferedReader;
import java.io.IOException;

public class ServerToClientReciever implements Runnable {

    private final BufferedReader reader;

    public ServerToClientReciever(BufferedReader reader) {
        this.reader = reader;
    }

    public void readFromServer() {
        try {
            if(reader.ready()) {
                String message = reader.readLine();
                if(message != null) {
                    if(message.equalsIgnoreCase("exit")) System.exit(0);
                    else System.out.println(message);
                }
            }
        } catch ( IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void run() {
        while (true) {
            readFromServer();
        }
    }
}
