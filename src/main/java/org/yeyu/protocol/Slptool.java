package org.yeyu.protocol;


import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Slptool {
//    final static String[] SLPTOOL_MULTICAST_COMMAND_HYPERSCALE = {"/bin/sh", "-c", "/usr/bin/slptool findsrvs service:SMASHOverSSH \"(service-id=SMASHOverSSH)\""};
    final static String[] SLPTOOL_MULTICAST_COMMAND_HYPERSCALE = {"/bin/sh", "-c", "/usr/bin/slptool findsrvs service:management-hardware.Lenovo"};
    Logger lgr = Logger.getLogger("slptool");

    public static void main(String... a) {
        new Slptool().d1();
    }

    Slptool() {
        setLogingProperties(lgr, Level.ALL);
    }

    void d1() {
        lgr.info("start");
        List<InetAddress> ias = thinkserverMulticastFinder();
//        ias.stream().forEach(inetAddress -> lgr.info(inetAddress.toString()));
        lgr.info("end");
    }

    private List<InetAddress> thinkserverMulticastFinder() {
        List<InetAddress> list = new ArrayList<>();
        List<String> hsLines = hsServerFinder();
        if (hsLines != null) {
            for (String line : hsLines) {
                try {
                    list.add(extractAddress(line));
                } catch (UnknownHostException e) {
                    lgr.severe(e.getMessage());
                }
            }
        }
        return list;
    }

    public List<String> hsServerFinder() {
        return slpMulticastRunner(SLPTOOL_MULTICAST_COMMAND_HYPERSCALE);
//        return slpMulticastRunner2();
    }

    public List<String> slpMulticastRunner2() {
        List<String> empty = new ArrayList<>();

//        String line = "/usr/bin/slptool findsrvs service:SMASHOverSSH \"(service-id=SMASHOverSSH)\"";
        String line = "/usr/bin/slptool findsrvs service:management-hardware.Lenovo";
        CommandLine cmdLine = CommandLine.parse(line);
        DefaultExecutor exec = new DefaultExecutor();

        CollectingLogOutputStream clos = new CollectingLogOutputStream();
        exec.setStreamHandler(new PumpStreamHandler(clos));

        try {
            exec.execute(cmdLine);
        } catch (IOException e) {
            lgr.severe(e.getMessage());
        }

        clos.getLines().stream().forEach(s -> lgr.info("clos: " + s));
        return clos.getLines();
    }

    public List<String> slpMulticastRunner(String[] slptoolCmd) {
        List<String> empty = new ArrayList<>();
        try {
            Process slpProc = Runtime.getRuntime().exec(slptoolCmd);
            int rc = slpProc.waitFor();
            if (rc != 0) {
                lgr.severe("RC {} executing slptool--" + rc);
                return (empty);
            }

            lgr.info("1");
            List<String> lines = new ArrayList<>();
            try (InputStream slptoolOut = slpProc.getInputStream(); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(slptoolOut))) {
                String lineString = null;
                while ((lineString = bufferedReader.readLine()) != null) {
                    lines.add(lineString);
                }

                if (lines == null || lines.size() <= 0) {
                    lgr.severe("No output from slptool Multicast exec, cmd:" + Stream.of(slptoolCmd).reduce((s1, s2) -> s1 + " " + s2).get());
                } else {
                    lines.stream().forEach(s -> lgr.info("is: " + s));
                }
            } catch (Exception e) {
                lgr.severe("1 exception");
                lgr.severe(e.getMessage());
            }

            lgr.info("2");
            try (InputStream slptoolError = slpProc.getErrorStream(); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(slptoolError))) {
                List<String> errLines = new ArrayList<>();
                String lineString = null;
                while ((lineString = bufferedReader.readLine()) != null) {
                    errLines.add(lineString);
                }

                if (errLines == null || errLines.size() <= 0) {
                    lgr.severe("No error output from slptool Multicast exec, cmd:" + Stream.of(slptoolCmd).reduce((s1, s2) -> s1 + " " + s2).get());
                } else {
                    errLines.stream().forEach(s -> lgr.info("es: " + s));
                }
            } catch (Exception e) {
                lgr.severe("2 exception");
                lgr.severe(e.getMessage());
            }

            return lines;
        } catch (IOException | InterruptedException e) {
            lgr.severe("3 exception");
            lgr.severe(e.getMessage());
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

    /**
     * 配置Logger对象输出日志文件路径
     *
     * @param logger
     * @param level  在日志文件中输出level级别以上的信息
     * @throws SecurityException
     * @throws IOException
     */
    public void setLogingProperties(Logger logger, Level level) {
        FileHandler fh;
        try {
            fh = new FileHandler(getLogName(), true);
            logger.addHandler(fh);// 日志输出文件
            logger.setLevel(level);
            fh.setFormatter(new SimpleFormatter());// 输出格式
            // logger.addHandler(new ConsoleHandler());//输出到控制台
        } catch (SecurityException e) {
            logger.log(Level.SEVERE, "安全性错误", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "读取文件日志错误", e);
        }
    }

    /**
     * 得到要记录的日志的路径及文件名称
     *
     * @return
     */
    private String getLogName() {
        StringBuffer logPath = new StringBuffer();
        logPath.append(System.getProperty("user.home"));
        logPath.append("//slptest");
        File file = new File(logPath.toString());
        if (!file.exists())
            file.mkdir();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        logPath.append("//" + sdf.format(new Date()) + ".log");
        return logPath.toString();
    }
}
