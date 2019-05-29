package org.yeyu.socket.chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

public class VerySimpleChatServer {
    ArrayList<PrintWriter> clientOutputStreams;
    Logger lgr = Logger.getLogger(this.getClass().getName());

    public static void main(String[] args){
        new VerySimpleChatServer().go();
    }

    public void go(){
        clientOutputStreams = new ArrayList<PrintWriter>();
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            while(true){
                Socket clientSocket = serverSocket.accept();
                PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
                clientOutputStreams.add(pw);
                new Thread(new ClientHandler(clientSocket), "server-newclientsocket").start();
                lgr.info("got a connection from " + clientSocket.getRemoteSocketAddress().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            lgr.warning(e.getMessage());
        }
    }

    public class ClientHandler implements Runnable{
        BufferedReader reader;
        Socket sock;
        Logger lgr = Logger.getLogger(this.getClass().getName());

        public ClientHandler(Socket clientSocket){
            sock = clientSocket;
            try {
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);
            } catch (IOException e) {
                e.printStackTrace();
                lgr.warning(e.getMessage());
            }

        }

        public void run(){
            try {
                while(true){
                    String message = reader.readLine();
                    lgr.info("read " + message);
                    tellEeveryone(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                lgr.warning(e.getMessage());
            }
        }
    }

    public void tellEeveryone(String msg){
        for(PrintWriter pw: clientOutputStreams){
            pw.println(msg);
            pw.flush();
        }
    }
}
