package org.yeyu.socket.chatroom;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;

public class SimpleChatClient {

    JTextArea incoming;
    JTextField outgoing;
    BufferedReader reader;
    PrintWriter writer;
    Socket socket;
    Logger lgr = Logger.getLogger(this.getClass().getName());

    public static void main(String[] args){
        new SimpleChatClient().go();
    }

    public void go(){
        JFrame frame = new JFrame("yeyu's simple chat client");
        JPanel mainPanel = new JPanel();
        incoming = new JTextArea(15, 30);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);

        JScrollPane qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        outgoing = new JTextField(20);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                writer.println(outgoing.getText());
                writer.flush();
                outgoing.setText("");
                outgoing.requestFocus();
            }
        });

        mainPanel.add(incoming);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);

        setUpNetworking();

        new Thread(new Runnable(){
            String msgReceived;
            public void run(){
                try {
                    while(true){
                        msgReceived = reader.readLine();
                        lgr.info("read " +msgReceived);
                        EventQueue.invokeLater(new Runnable(){
                            public void run(){
                                incoming.append(msgReceived+"\n");
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, "client-receive").start();

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frame.setSize(400, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void setUpNetworking(){
        try {
            socket = new Socket("127.0.0.1", 5000);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            lgr.info("networking established");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
