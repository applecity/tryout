package org.yeyu.function;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class F2 {
    public static void main(String[] args) {
        new F2().f1();
    }

    void f1() {
        String[] queries = "uploadfilename=Lenovo.htm&originalfilename=Lenovoager用户手册.htm".split("&");
        queries = "aaa".split("&");
        Map<String, String> kvPairs = Stream.of(queries).collect(Collectors.toMap(seg -> seg.split("=")[0], seg -> seg.split("=")[1]));
        kvPairs.forEach((k, v) -> {
                System.out.println(k + " = " + v);
        });
    }
}
