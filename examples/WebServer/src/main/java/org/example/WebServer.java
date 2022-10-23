package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Simple web server.
 */
public class WebServer {
    public static void main(String[] args) {
        // Port number for http request
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 8080;
        // The maximum queue length for incoming connection
        int queueLength = args.length > 2 ? Integer.parseInt(args[2]) : 50;

        try (ServerSocket serverSocket = new ServerSocket(port, queueLength)) {
            System.out.println("Web Server is starting up, listening at port " + port + ".");
            System.out.println("You can access http://localhost:" + port + " now.");

            while (true) {
                // Make the server socket wait for the next client request
                Socket socket = serverSocket.accept();
                System.out.println("Got connection!");

                // To read input from the client
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

                // Get request
                HttpRequest request = HttpRequest.parse(input);

                // Process request
                Processor proc = new Processor(socket, request);
                StringBuilder string = new StringBuilder(request.getRequestLine());
                string.delete(0,4);
                string.delete(string.length() - 9, string.length() + 1);
                System.out.println();
                System.out.println(string);
                System.out.println();

                if(request.getRequestLine().contains("/create/")){
                    string.delete(0,6);
                    proc.create();
                }
                else if(request.getRequestLine().contains("/exec/counter")){
                    proc.execute();
                }
                else if(request.getRequestLine().contains("/delete/")){
                    proc.delete();
                }
                else {
                    proc.process();
                }
            }
        }
        catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        finally {
            System.out.println("Server has been shutdown!");
        }
    }
}
