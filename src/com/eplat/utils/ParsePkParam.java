package com.eplat.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @项目名称：eplat
 * @类名称：ParsePkParam
 * @类描述：解析主键Key
 * @创建人：高洋
 * @创建时间：2010-3-24 下午01:04:45
 * @修改人：高洋
 * @修改时间：2010-3-24 下午01:04:45
 * @修改备注：
 * @version
 */
public class ParsePkParam {
	public static ArrayList<Map<String,String>> getPkList(String pks[]) {
		ArrayList<Map<String,String>> pkList = new ArrayList<Map<String,String>>();
		for (int j = 0; j < pks.length; j++) {
			HashMap<String, String> map = new HashMap<String, String>();
			String keyValues[] = pks[j].split("&");
			for (int i = 0; i < keyValues.length; i++) {
				int k = keyValues[i].indexOf("=");
				String key = keyValues[i].substring(0, k);
				String value = keyValues[i].substring(k + 1);
				map.put(key, value);
			}
			pkList.add(map);
		}
		return pkList;
	}
	public static Map<String,String> parseParam(String pks){
		Map<String,String> pkMap=new HashMap<String,String>();
		String keyValues[] = pks.split("&");
		for (int i = 0; i < keyValues.length; i++) {
			int k = keyValues[i].indexOf("=");
			String key = keyValues[i].substring(0, k);
			String value = keyValues[i].substring(k + 1);
			pkMap.put(key, value);
		}
		return pkMap;
		
	}
	public static ArrayList<Map<String,String>> getPkList(List<String> pks) {
		ArrayList<Map<String,String>> pkList = new ArrayList<Map<String,String>>();
		for (int j = 0; j < pks.size(); j++) {
			HashMap<String, String> map = new HashMap<String, String>();
			String keyValues[] = pks.get(j).split("&");
			for (int i = 0; i < keyValues.length; i++) {
				int k = keyValues[i].indexOf("=");
				String key = keyValues[i].substring(0, k);
				String value = keyValues[i].substring(k + 1);
				map.put(key, value);
			}
			pkList.add(map);
		}
		return pkList;
	}
}
