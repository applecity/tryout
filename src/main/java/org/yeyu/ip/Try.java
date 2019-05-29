package org.yeyu.ip;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Try {
    public static void main(String... args) throws UnknownHostException {
        System.out.println(InetAddress.getByName("fd00::1:7ed3:0aff:feec:38e5").getHostAddress());
    }
}
