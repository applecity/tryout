package org.yeyu.json.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Optional;

public class Test {
//    @org.junit.Test
    public void testGo1() {
        JSONObject map=new JSONObject(true);//关键要设置为true，否则乱序
        map.put("请假类型","1");
        map.put("单据状态","2");
        map.put("开始时间","3");
        map.put("结束时间","4");
        map.put("请假原因","5");
        System.out.println(map.toJSONString());
        System.out.println(map.toString());

        LinkedHashMap<String, Object> root= JSON.parseObject(map.toString(),new TypeReference<LinkedHashMap<String, Object>>(){} );//关键的地方，转化为有序map
        System.out.println(JSON.toJSONString(root));

        Collection<Object> lists=root.values();
        lists.stream().forEach( v -> System.out.println(v));

        // {"Format":"json","Name":"wwqqzz","Para":{"myuser":333,"mynum":"222", "mylist":[11,55,"33"]},"Reserved":{},"Type":"request","Version":"1.0"}
        String mmStr = "{\"Format\":\"json\",\"Name\":\"wwqqzz\",\"Para\":{\"myuser\":333,\"mynum\":\"222\", \"mylist\":[11,55,\"33\"]},\"Reserved\":{},\"Type\":\"request\",\"Version\":\"1.0\"}";
        // Feature.OrderedField 解决嵌套多层map 的时候序列排序问题
        LinkedHashMap<String, Object> mm=JSON.parseObject(mmStr, new TypeReference<LinkedHashMap<String, Object>>(){} , Feature.OrderedField);
        System.out.println(JSON.toJSONString(mm, true));
    }

    @org.junit.Test
    public void testGo2() {
        JSONObject m = new JSONObject();
        m.put("请假原因","5");
        m.put("a", null);
        System.out.println(m.size());
        System.out.println(m.toString());
        System.out.println(Optional.ofNullable(m.toString()));
    }
}
