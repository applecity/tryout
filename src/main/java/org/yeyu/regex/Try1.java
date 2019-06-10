package org.yeyu.regex;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Try1 {

    protected static Optional<String> d1_extractAddress(String line) throws UnknownHostException {
        // (?:X)	X, as a non-capturing group
        String ps = "([^/]*//)((?:\\[[a-f0-9\\:]+\\])|(?:[^,:]*))(?:\\:\\d+)?(,.*)";

        ps = "([^/]*//)(?:\\[[a-f0-9\\:]+\\])(?:\\:\\d+)?(,.*)";
        ps = "([^/]*//)(?:\\[[a-f0-9\\:]+\\])(?:\\:\\d+)(,.*)";
        ps = "([^/]*//)(\\[[a-f0-9\\:]+\\])(\\:\\d+)(,.*)";
        ps = "(.*//)(\\[[a-f0-9\\:]+\\])(\\:\\d+)(,.*)";


        Pattern pattern = Pattern.compile(ps, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            String hn = matcher.group(2);
//            InetAddress ia = InetAddress.getByName(hn);
//            return Optional.ofNullable(ia.toString());
            return Optional.ofNullable(hn);
        }
        return Optional.empty();
    }

    static void d1() throws UnknownHostException {
        System.out.println(d1_extractAddress("service:wbem:https://YEYU1-R305QQJ2:5989,65535").orElse("empty"));
        System.out.println();
        System.out.println(d1_extractAddress("service:wbem:https://[fe80::a55b:73a7:6ac7:5af7]:5989,65535").orElse("empty"));
    }

    static void d2() throws UnknownHostException {
        String temp = "<meta-data android:name=\"appid\" android:value=\"joy\"></meta-data>";
        Pattern pattern = Pattern.compile("android:(name|value)=\"(.+?)\"");
        Matcher matcher = pattern.matcher(temp);
//输出: name appid value joy
        while(matcher.find()) {
            System.out.print(matcher.group(1));
            System.out.print(" ");
            System.out.print(matcher.group(2));
            System.out.print(" ");
        }
        System.out.println();

        pattern = Pattern.compile("android:(name|value)=\"(.+)\"");
        matcher = pattern.matcher(temp);
//输出: name appid" android:value="joy
        while(matcher.find()) {
            System.out.print(matcher.group(1));
            System.out.print(" ");
            System.out.print(matcher.group(2));
            System.out.print(" ");
        }
        System.out.println();
    }

    static void d3() throws UnknownHostException {
        String out = "snmp-agent target-host trap address udp-domain 10.121.133.51 params securityname LXCA-10.121.133.16 v3 privacy" +
                "snmp-agent target-host trap address udp-domain ipv6 FD00::1:5054:FF:FE14:C06E params securityname SYSMGR_6AIFPFRM v3 privacy\r\n" +
                "snmp-agent target-host trap address udp-domain ipv6 fd00::1:7625:8aff:fe6d:3027 params securityname SYSMGR_6AIFPFRM v3 privacy\r\n" +
                "snmp-agent target-host trap address udp-domain 10.121.133.43 params securityname SYSMGR_0TTWUXTZ v3 privacy";
        String psWant = "ipv6 (.+?) ";
        // psNotWant 开启了贪婪模式
        String psNotWant = "ipv6 (.+) ";
        Pattern pattern = Pattern.compile(psWant);
        Matcher matcher = pattern.matcher(out);
        while(matcher.find()) {
            System.out.print(matcher.group(1));
            boolean f = InetAddress.getByName(matcher.group(1)).equals(InetAddress.getByName("fd00:0:0:1:5054:ff:fe14:c06e"));
            System.out.println(" " + f);
        }
    }

    static void d4() {
        String ps = "(?<=\")([a-zA-Z0-9]+)(?:[ ]+[a-zA-Z0-9]+)?";
        String target = "SNMPv2-SMI::enterprises.232.11.2.14.1.1.4.0 = STRING: \"iLO 5\"";
        Matcher m = Pattern.compile(ps).matcher(target);
        if(m.find()) {
            System.out.println(m.groupCount());
            System.out.println(m.group(1));
        }
    }

    static void d5() {
        System.out.println("abc d.json".replaceAll ("(?i)\\.json", ""));
        System.out.println("tHinkk  j".replaceAll("(?i)think|server|lenovo|\\s+", ""));
    }

    protected static InetAddress extractAddress(String line) throws UnknownHostException {
        Pattern pattern = Pattern.compile("([^/]*//)((?:\\[[a-f0-9\\:]+\\])|(?:[^,:]*))(?:\\:\\d+)?(,.*)",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            return InetAddress.getByName(matcher.group(2));
        }
        return null;
    }

    public static void main(String... a) throws UnknownHostException {
        String xx = "service:SMASHOverSSH://10.111.70.252:22,65535";
        xx = "service:SMASHOverSSH://1234567123456712345671234567:22,65535";
        InetAddress ipaddr = extractAddress(xx);
        System.out.println(ipaddr.getHostAddress());
    }

}
