package org.yeyu.string;

public class Try1 {

    static void d1() {
        System.out.println(String.join(" ", new String[]{"/bin/sh", "-c", "some cmd"}));
        System.out.println(String.format("http://%s/lasf/army/recognise%%", "sssss"));
//        "iLO".substring(0,4);
//        "iLO".substring(0,3);
//        System.out.println("iBMC".substring(0,4));
//        System.out.println("iBMC".substring(4));
        System.out.println(String.format("boolean %s", true));
    }

    public static void main(String... a) {
        d1();
    }
}
