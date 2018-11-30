package org.yeyu.simpletest;

import org.apache.commons.validator.routines.InetAddressValidator;

public class Try1 {
    public static void main(String... args) {
        boolean f;
//        f = new Try1().isValidInet6Address("172.16.100.10");
        f = new InetAddressValidator().isValidInet6Address("172.16.100.10");
        System.out.println(f);
        f = new InetAddressValidator().isValidInet4Address("172.16.100.10");
        System.out.println(f);
    }

    /**
     * 本函数取自ebg-util jar的InetAddressValidator的isValidInet6Address方法。
     * @param inet6Address
     * @return
     */
    public boolean isValidInet6Address(String inet6Address) {
        String[] groups = inet6Address.split(":", 8);
        if (groups != null && groups.length != 0) {
            int invalidGroupCount = 0;
            int nullGroupCount = 0;

            for(int i = 0; i < groups.length; ++i) {
                String ipSegment = groups[i];
                if (ipSegment != null && ipSegment.length() != 0) {
                    boolean var7 = false;

                    try {
                        int iIpSegment = Integer.parseInt(ipSegment, 16);
                        if (iIpSegment > 65535) {
                            return false;
                        }
                    } catch (NumberFormatException var9) {
                        ++invalidGroupCount;
                    }
                } else {
                    ++nullGroupCount;
                }
            }

            switch(nullGroupCount) {
                case 0:
                    break;
                case 1:
                    if ("".equals(groups[0])) {
                        return false;
                    }

                    if ("".equals(groups[groups.length - 1])) {
                        return false;
                    }
                    break;
                case 2:
                    if ("".equals(groups[0]) && "".equals(groups[1])) {
                        break;
                    }

                    return false;
                default:
                    return false;
            }

            switch(invalidGroupCount) {
                case 1:
                    if (!this.isValidInet4Address(groups[groups.length - 1])) {
                        return false;
                    }
                case 0:
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public boolean isValidInet4Address(String inet4Address) {
        return new InetAddressValidator().isValidInet4Address(inet4Address);
    }

}
