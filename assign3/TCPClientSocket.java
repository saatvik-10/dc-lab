import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.*;

public class TCPClientSocket {
    public static void main(String[] args) {

        String serverIP = "172.19.4.184";

        int port = 5000;

        try {
            Socket socket = new Socket(serverIP, port);

            System.out.println("Connected to server!");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            AtomicBoolean running = new AtomicBoolean(true);

            Thread receiver = new Thread(() -> {
                String message;
                try {
                    while (running.get() && (message = in.readLine()) != null) {
                        if (message.equalsIgnoreCase("exit")) {
                            System.out.println("Server disconnected.");
                            running.set(false);
                            break;
                        }
                        System.out.println("Server: " + message);
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
