package com.eplat.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by liulongyun on 2017/12/6.
 */
public class ConfigUtils {

   private static Map<String,String> configMap =new HashMap<>();

    static {
       try {
           Properties pro = new Properties();
           InputStream in= ConfigUtils.class.getClassLoader().getResourceAsStream("config.properties");
           pro.load(in);
           Set<Object> keySet = pro.keySet();
           Iterator it = keySet.iterator();
           while (it.hasNext()){
              String key = (String)it.next();
              configMap.put(key,(String)pro.get(key));
           }
           in.close();
       } catch (IOException e) {
           e.printStackTrace();
       }

   }

   public static String getValue(String key){
        return configMap.get(key);
   }





}
