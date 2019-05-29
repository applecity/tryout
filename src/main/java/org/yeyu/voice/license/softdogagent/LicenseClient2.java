package org.yeyu.voice.license.softdogagent;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class LicenseClient2 {
    Socket socket = null;
    PrintWriter writer = null;
    BufferedReader reader = null;

    public static void main(String[] args) {
        new LicenseClient2().go1();

    }

    void go1() {
        try (Socket socketl = new Socket("127.0.0.1", 8384);
                      PrintWriter writer = new PrintWriter(socketl.getOutputStream());
                      BufferedReader reader = new BufferedReader(new InputStreamReader(socketl.getInputStream()))) {

            System.out.println("connect server");
            Thread.sleep(2000);
            System.out.println("send message");
            writer.println("client init");
            writer.flush();

            System.out.println(reader.readLine());

            Thread.sleep(5000);
            System.out.println("close socket");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void go2() {
        setUpNetworking();

//        new Thread(new Runnable(){
//            String msgReceived;
//            public void run(){
//                try {
//                    while(true){
//                        msgReceived = reader.readLine();
//                        System.out.println("read " +msgReceived);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, "client-receive").start();

        writer.println("aaaaa");
        writer.flush();

        try {
            System.out.println("read " + reader.readLine());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

//        tearDownNetworking();
    }


    void go3() {
        try {
            Socket socket = new Socket("127.0.0.1", 8384);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            Future<String> rr = es.submit(new LicenseClient.Recv(reader));

//            Thread t2 = new Thread(new Recv2(reader));
//            t2.start();
            writer.println("client init");
            writer.flush();

            System.out.println(reader.readLine());
//            t2.join();
//            String serverResp = rr.get(2, TimeUnit.SECONDS);
//            System.out.println("serverResp: " + serverResp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class Recv2 implements Runnable {
        private BufferedReader reader;

        Recv2(BufferedReader reader) {
            this.reader = reader;
        }

        @Override
        public void run() {
            try {
//                while (true) {
                String r = reader.readLine();
                System.out.println("receive :" + r);
//                    if (r != null && r.trim().length() > 0) {
//                        break;
//                    }
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setUpNetworking() {
        try {
            socket = new Socket("127.0.0.1", 5000);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            System.out.println("networking established");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void tearDownNetworking() {
        try {
            reader.close();
            writer.close();
            socket.close();
            System.out.println("tear down");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
