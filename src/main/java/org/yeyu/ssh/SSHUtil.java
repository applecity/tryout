package org.yeyu.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SSHUtil {
    private static int intTimeOut = 60000;
//    private final static Logger LOG = LoggerFactory.getLogger(SSHUtil.class);

    public static Optional<String> exec(String destinationHost, String user, String pass, String cmd) {
        return exec(destinationHost, 22, user, pass, cmd);
    }

    public static Optional<String> exec(String destinationHost, Integer port, String user, String pass, String cmd) {
        Optional<String> rr = Optional.empty();

        JSch jSch = new JSch();
        Session session = null;
        ChannelExec channel = null;
        try {
            session = jSch.getSession(user, destinationHost, port);
            session.setPassword(pass);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(intTimeOut);

            channel = (ChannelExec)session.openChannel("exec");
            channel.setCommand(cmd);
            InputStream commandOutput = channel.getInputStream();
            channel.connect();

            StringBuilder outputBuffer = new StringBuilder();
            int readByte = commandOutput.read();
            while(readByte != 0xffffffff)
            {
                outputBuffer.append((char)readByte);
                readByte = commandOutput.read();
            }
            rr = Optional.of(outputBuffer.toString());
        } catch (JSchException e) {
            String msg = String.format("Could not open session to host: %s", destinationHost);
//            LOG.error(msg, e);
        } catch (IOException e) {
            String msg = String.format("Could not read output  of command executed on host: %s", destinationHost);
//            LOG.error(msg, e);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
        return rr;
    }

    public static Optional<List<String>> exec(String destinationHost, String user, String pass, List<String> cmds) {
        return exec(destinationHost, 22, user, pass, cmds);
    }

    public static Optional<List<String>> exec(String destinationHost, Integer port, String user, String pass, List<String> cmds) {
        Optional<List<String>> rr = Optional.empty();
        List<String> msgs = new ArrayList<String>(cmds.size());

        JSch jSch = new JSch();
        Session session = null;
        ChannelExec channel = null;
        try {
            session = jSch.getSession(user, destinationHost, port);
            session.setPassword(pass);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(intTimeOut);

            for(String cmd : cmds) {
                channel = (ChannelExec)session.openChannel("exec");
                msgs.add(channeldo(channel, cmd));
            }

            rr = Optional.of(msgs);
        } catch (JSchException e) {
            String msg = String.format("Could not open session to host: %s", destinationHost);
//            LOG.error(msg, e);
        } catch (IOException e) {
            String msg = String.format("Could not read output  of command executed on host: %s", destinationHost);
//            LOG.error(msg, e);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
        return rr;
    }

    private static String channeldo(ChannelExec channel , String cmd)  throws JSchException, IOException{
        StringBuilder outputBuffer = new StringBuilder();
        channel.setCommand(cmd);
        InputStream commandOutput = channel.getInputStream();
        channel.connect();

        int readByte = commandOutput.read();
        while(readByte != 0xffffffff)
        {
            outputBuffer.append((char)readByte);
            readByte = commandOutput.read();
        }
        return outputBuffer.toString();
    }

}
