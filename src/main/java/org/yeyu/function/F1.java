package org.yeyu.function;

import org.apache.commons.collections.ArrayStack;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class F1 {
    public static void main(String... args) throws IOException {
            new F1().test1();


    }

    void test1() throws IOException {
        List<InventoryNodeItem> controllerNodes = test1Setup();
        Map<String, List<InventoryNodeItem>> mm = controllerNodes.stream().collect(Collectors.groupingBy(F1::getControllerLabel));
//        mm.forEach((key, item) -> item.stream().reduce(F1::mergeInventoryNodeItem));
//        Map<String, InventoryNodeItem> got = mm.entrySet().stream().reduce((e1, e2) -> {e1.get})

//        for(List<InventoryNodeItem> items : mm.values()) {
//            items.stream().reduce(F1::mergeInventoryNodeItem)
//        }

        Map<String, InventoryNodeItem> ready = new HashMap<>();
        for(Map.Entry<String, List<InventoryNodeItem>> entry: mm.entrySet()) {
            entry.getValue().stream().reduce(F1::mergeInventoryNodeItem).ifPresent(nodeItem -> ready.put(entry.getKey(), nodeItem));
        }

        System.out.println("b");
    }

    private static InventoryNodeItem mergeInventoryNodeItem(InventoryNodeItem it1, InventoryNodeItem it2) {
        String ip1 = it1.getInformation().get("ip");
        String ip2 = it2.getInformation().get("ip");
        if( null == ip1 ) return it2;
        if( null == ip2 ) return it1;



        return it1;
    }

    private static String getControllerLabel(InventoryNodeItem controller) {
        String eleName = controller.getInformation().get("ElementName");
        return (null != eleName && eleName.length()>0) ? eleName.substring(eleName.length() - 1) : "";
    }

    List<InventoryNodeItem> test1Setup() {
//        Map<String, String> m = new HashMap<>();
//        m.put("ElementName", "Controller A");
//        m.put("portStatus", "linkup");
//        m.put("ip", "10.121.137.160");
//        m.put("Cache", "24576");
//        m.put("MemorySize", "24576");
//        m.put("HealthState", "5");
//        m.put("mac", "3c:e8:24:d0:b9:68");
//        m.put("OperationalStatus", "2");
//        m.put("netmask", "255.255.254.0");
//        m.put("gateway", "10.121.137.254");
//        Arrays.asList(new InventoryNodeItem("Controller 0", m));

        String c0 = "{   \"ElementName\": \"Controller A\",    \"portStatus\": \"linkup\",    \"ip\": \"10.121.137.160\",    \"Cache\": 24576,    \"MemorySize\": 24576,    \"HealthState\": 5,    \"mac\": \"3c:e8:24:d0:b9:68\",    \"OperationalStatus\": 2,    \"netmask\": \"255.255.254.0\",    \"gateway\": \"10.121.137.254\"  }";
        String c1 = "{   \"ElementName\": \"Controller B\",    \"portStatus\": \"linkup\",    \"ip\": \"10.121.137.161\",    \"Cache\": 24576,    \"MemorySize\": 24576,    \"HealthState\": 5,    \"mac\": \"3c:e8:24:d0:b8:ce\",    \"OperationalStatus\": 2,    \"netmask\": \"255.255.254.0\",    \"gateway\": \"10.121.137.254\"  }";
        String c2 = "{   \"ElementName\": \"Controller A\",    \"portStatus\": \"linkup\",    \"ip\": \"\",    \"Cache\": 24576,    \"MemorySize\": 24576,    \"HealthState\": 5,    \"mac\": \"3c:e8:24:d0:b9:68\",    \"OperationalStatus\": 2,    \"netmask\": \"\",    \"gateway\": \"\"  }";
        String c3 = "{   \"ElementName\": \"Controller B\",    \"portStatus\": \"linkup\",    \"ip\": \"fd00::1:1:2:3:4\",    \"Cache\": 24576,    \"MemorySize\": 24576,    \"HealthState\": 5,    \"mac\": \"3c:e8:24:d0:b8:ce\",    \"OperationalStatus\": 2,    \"netmask\": \"64\",    \"gateway\": \"\"  }";
        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, String> ss = objectMapper.readValue(c0, HashMap.class);
//        Map<String, String> ss2 = objectMapper.readValue(c0, new TypeReference<HashMap<String, Object>>(){});

        List<InventoryNodeItem> controllerNodes = Stream.of(c0, c1, c2, c3).map(s -> {
            InventoryNodeItem ret1 = null;
            try {
                ret1 = new InventoryNodeItem("Controller ", objectMapper.readValue(s, HashMap.class));
            } catch (IOException e) {
                e.printStackTrace();
//                throw new RuntimeException();
            }
            return ret1;
        }).collect(Collectors.toList());

        for(InventoryNodeItem item : controllerNodes) {
            item.setName(item.getName() + controllerNodes.indexOf(item));
        }

        System.out.println("aa");
        return controllerNodes;
    }

    class InventoryNodeItem {
        private Map<String, String> information;

        public Map<String, String> getInformation() {
            return information;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private String name;

        public InventoryNodeItem(String name, Map<String, String> information) {
            this.name = name;
            this.information = information;
        }
    }
}
