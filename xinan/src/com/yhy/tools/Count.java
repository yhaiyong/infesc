package com.yhy.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: 杨海勇
 **/
public class Count {

    public Count(String s) {
        char[] chars = s.toCharArray();
        Map<Character, Integer> map = new HashMap();
        for (char c : chars) {
            if (map.containsKey(c)) {
                map.put(c, map.get(c) + 1);
            }else{
                map.put(c,1);
            }
        }
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            System.out.println("字符（"+entry.getKey()+"）出现的次数为："+entry.getValue());
        }
    }



}
