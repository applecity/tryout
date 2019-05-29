package org.yeyu.json.jackson;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.util.HashMap;
import java.util.Map;

public class Test {
    @org.junit.Test
    public void t1() throws Exception {
        String c0 = "{   \"ElementName\": \"Controller A\",    \"portStatus\": \"linkup\",    \"ip\": \"10.121.137.160\",    \"Cache\": 24576,    \"MemorySize\": 24576,    \"HealthState\": 5,    \"mac\": \"3c:e8:24:d0:b9:68\",    \"OperationalStatus\": 2,    \"netmask\": \"255.255.254.0\",    \"gateway\": \"10.121.137.254\"  }";
        String c1 = "{   \"ElementName\": \"Controller B\",    \"portStatus\": \"linkup\",    \"ip\": \"10.121.137.161\",    \"Cache\": 24576,    \"MemorySize\": 24576,    \"HealthState\": 5,    \"mac\": \"3c:e8:24:d0:b8:ce\",    \"OperationalStatus\": 2,    \"netmask\": \"255.255.254.0\",    \"gateway\": \"10.121.137.254\"  }";
        String c2 = "{   \"ElementName\": \"Controller A\",    \"portStatus\": \"linkup\",    \"ip\": \"\",    \"Cache\": 24576,    \"MemorySize\": 24576,    \"HealthState\": 5,    \"mac\": \"3c:e8:24:d0:b9:68\",    \"OperationalStatus\": 2,    \"netmask\": \"\",    \"gateway\": \"\"  }";
        String c3 = "{   \"ElementName\": \"Controller B\",    \"portStatus\": \"linkup\",    \"ip\": \"fd00::1:1:2:3:4\",    \"Cache\": 24576,    \"MemorySize\": 24576,    \"HealthState\": 5,    \"mac\": \"3c:e8:24:d0:b8:ce\",    \"OperationalStatus\": 2,    \"netmask\": \"64\",    \"gateway\": \"\"  }";
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, String> ss = objectMapper.readValue(c0, HashMap.class);
        Map<String, String> ss2 = objectMapper.readValue(c0, new TypeReference<HashMap<String, Object>>(){});

        System.out.println("");
    }
}
