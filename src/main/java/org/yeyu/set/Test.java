package org.yeyu.set;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
    @org.junit.Test
    public void testGo1() {
        Map<String, String> m = new HashMap();
        m.put("a", "a1");
        System.out.println(m);

        List<String> l = new ArrayList<>();
        l.add("b");
        System.out.println(l);
    }
}
