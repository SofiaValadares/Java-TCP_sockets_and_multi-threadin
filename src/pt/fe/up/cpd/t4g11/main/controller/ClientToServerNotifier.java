package src.pt.fe.up.cpd.t4g11.main.controller;

import java.io.PrintWriter;

import static java.lang.Thread.sleep;

public class ClientToServerNotifier implements Runnable {

    private final PrintWriter writer;

    public ClientToServerNotifier(PrintWriter writer) {
        this.writer = writer;
    }

    public void hiServer() {
        try {
            writer.println("");
            sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void run() {
        while (true) {
            hiServer();
        }
    }
}
