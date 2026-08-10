import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class TCPServerSocket {

    public static void main(String[] args) {
        int port = 5000;
        ExecutorService executor = Executors.newCachedThreadPool();

        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(port));

            System.out.println("Server started...");
            System.out.println("Waiting for clients on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");
                executor.submit(new ClientHandler(socket));
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket socket;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            Thread receiver = new Thread(() -> {
                String message;
                try {
                    while (running.get() && (message = in.readLine()) != null) {
                        if (message.equalsIgnoreCase("exit")) {
                            System.out.println("Client disconnected.");
                            running.set(false);
                            break;
                        }
                        System.out.println("Client: " + message);
                    }
                } catch (IOException e) {
                    System.out.println("Error: " + e.getMessage());
                } finally {
                    running.set(false);
                }
            });
            receiver.start();

            BufferedReader keyboard = new BufferedReader(
                    new InputStreamReader(System.in));

            String message;
            while (running.get() && (message = keyboard.readLine()) != null) {
                out.println(message);
                if (message.equalsIgnoreCase("exit")) {
                    running.set(false);
                    break;
                }
            }

            running.set(false);
            socket.close();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
