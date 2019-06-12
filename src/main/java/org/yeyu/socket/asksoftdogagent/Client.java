package org.yeyu.socket.asksoftdogagent;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static java.lang.Integer.parseInt;
public class Client {
    private static Logger lgr = Logger.getLogger("xx");
    public static void main(String[] args) {
        lgr.setLevel(Level.ALL);
        queryAgent("sn", "10.112.25.172", "8384");
    }

    private static boolean queryAgent(String baseboardSerial, String softDogServiceIp, String softDogServicePort) {

        int port = 0;
        try {
            port = parseInt(softDogServicePort);
        } catch (NumberFormatException e) {
            lgr.severe(e.getMessage());
            return false;
        }

        Optional<String> snO = Optional.empty();
        try (Socket socket = new Socket(softDogServiceIp, port);
             PrintWriter writer = new PrintWriter(socket.getOutputStream());
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            socket.setSoTimeout(5 * 1000);
            writer.println("SerialNumber");
            writer.flush();
            lgr.info("send req");
            snO = Optional.ofNullable(reader.readLine());
            lgr.info("got response:" + snO.get().trim() + ", length:" + snO.get().trim().length());
        } catch (IOException e) {
            lgr.severe("query sn failure" + e);
            return false;
        }

        lgr.info("pre-set sn:" + baseboardSerial + ", length:" + baseboardSerial.length());

        if (snO.get().trim().equals(baseboardSerial)) {
            lgr.info("check sn, the same");
            return true;
        } else {
            lgr.info("check sn, different");
            return false;
        }
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
