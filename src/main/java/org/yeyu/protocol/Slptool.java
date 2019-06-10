package org.yeyu.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Slptool {
    final static String[] SLPTOOL_MULTICAST_COMMAND_HYPERSCALE = {"/bin/sh", "-c", "slptool findsrvs service:SMASHOverSSH \"(service-id=SMASHOverSSH)\""};

    public static void main(String... a) {
        new Slptool().d1();
    }

    void d1() {
        List<InetAddress> ias = thinkserverMulticastFinder();
        ias.stream().forEach(inetAddress -> System.out.println(inetAddress.toString()));
    }

    private List<InetAddress> thinkserverMulticastFinder() {
        List<InetAddress> list = new ArrayList<>();
        List<String> hsLines = hsServerFinder();
        if (hsLines != null) {
            for (String line : hsLines) {
                try {
                    list.add(extractAddress(line));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public List<String> hsServerFinder() {
        return slpMulticastRunner(SLPTOOL_MULTICAST_COMMAND_HYPERSCALE);
    }

    public List<String> slpMulticastRunner(String[] slptoolCmd) {
        List<String> empty = new ArrayList<>();
        try {
            Process slpProc = Runtime.getRuntime().exec(slptoolCmd);
            int rc = slpProc.waitFor();
            if (rc != 0) {
                System.out.println("RC {} executing slptool--" + rc);
                return (empty);
            }

            InputStream slptoolOut = slpProc.getInputStream();
            if (slptoolOut == null) {
                System.out.println("Null input stream from slptool exec");
                return (empty);
            }

            List<String> lines = new ArrayList<>();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(slptoolOut));
            String lineString = null;
            while ((lineString = bufferedReader.readLine()) != null) {
                lines.add(lineString);
            }

            if (lines == null || lines.size() <= 0) {
                System.out.println("No output from slptool Multicast exec, cmd:" + Stream.of(slptoolCmd).reduce((s1, s2) -> s1+ " " +s2).get());
                return (empty);
            }

            return lines;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return (empty);
        }
    } /* slpMulticastRunner */

    protected InetAddress extractAddress(String line) throws UnknownHostException {
        Pattern pattern = Pattern.compile("([^/]*//)((?:\\[[a-f0-9\\:]+\\])|(?:[^,:]*))(?:\\:\\d+)?(,.*)",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            return InetAddress.getByName(matcher.group(2));
        }
        return null;
    }
}
