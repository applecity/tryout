package org.yeyu.voice.license.softdogagent;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.*;

public class LicenseClient {
    public static void main(String[] args) {
        new LicenseClient().go2();

    }

    void go2() {
        Socket socket = null;
        PrintWriter writer = null;
        BufferedReader reader = null;
        try {
            socket = new Socket("127.0.0.1", 5000);
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            Future<String> rr = es.submit(new Recv(reader));

            new Thread(new Recv2(reader)).start();

            writer.println("client init");
            writer.flush();
            Thread.sleep(2000);
//            String serverResp = rr.get(2, TimeUnit.SECONDS);
//            System.out.println("serverResp: " + serverResp);

        } catch (IOException e) {
            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (TimeoutException e) {
//            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            writer.close();
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void go1() {
        ExecutorService es = Executors.newSingleThreadExecutor();
        try (Socket socket = new Socket("127.0.0.1", 5000);
             PrintWriter writer = new PrintWriter(socket.getOutputStream());
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            Future<String> rr = es.submit(new Recv(reader));
            writer.println("client init");
            writer.flush();
            String serverResp = rr.get(2, TimeUnit.SECONDS);
            System.out.println("serverResp: " + serverResp);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
//        } finally {
//            es.shutdown();
        }
        es.shutdown();
    }

    private class Recv implements Callable<String> {
        private BufferedReader reader;

        Recv(BufferedReader reader) {
            this.reader = reader;
        }

        @Override
        public String call() throws IOException {
            while (true) {
                String r = reader.readLine();
                System.out.println("receive :" + r);
                if (r != null && r.trim().length() > 0) {
                    return r;
                }
            }
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
                while (true) {
                    String r = reader.readLine();
                    System.out.println("receive :" + r);
//                    if (r != null && r.trim().length() > 0) {
//                        break;
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
