package org.chatApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        PrintWriter output = null;
        BufferedReader userInput = null;

        try {
            serverSocket = new ServerSocket(1616);
            System.out.println("Server started, waiting for clients...");

            clientSocket = serverSocket.accept();
            System.out.println("Client connected : " + clientSocket);

            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);

            Thread readThread = new Thread(() -> {
                String message;
                try {
                    while ((message = input.readLine()) != null) {
                        System.out.println("CLIENT : " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        input.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            readThread.start();

            userInput = new BufferedReader(new InputStreamReader(System.in));
            String message;
            while ((message = userInput.readLine()) != null) {
                output.println(message);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            output.close();
            try {
                userInput.close();
                clientSocket.close();
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}

