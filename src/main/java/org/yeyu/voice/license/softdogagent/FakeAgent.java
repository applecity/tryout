package org.yeyu.voice.license.softdogagent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class FakeAgent {
    public static void main(String[] args) {
        new FakeAgent().go();
    }

    void go() {
        try (ServerSocket ss = new ServerSocket(8384)) {
            System.out.println("start listening");
            while (true) {
                Socket clientSocket = ss.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ClientHandler implements Runnable {
        Socket clientSocket;
        BufferedReader br;
        PrintWriter pw;

        ClientHandler(Socket socket) throws IOException {
            clientSocket = socket;
            pw = new PrintWriter(clientSocket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }

        @Override
        public void run() {
            System.out.println("client connected, " + clientSocket.getRemoteSocketAddress().toString());
            try {

                while(true) {
                    String aa = br.readLine();
                    if(aa != null && aa.trim().length()>0) {
                        System.out.println("receive: " + aa);
                        pw.println("12345tr");
                        pw.flush();
                        System.out.println("send 12345tr");
                    } else {
                        br.close();
                        pw.close();
                        clientSocket.close();
                        System.out.println("receive: null, close socket");
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
