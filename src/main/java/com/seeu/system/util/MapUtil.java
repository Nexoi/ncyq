package com.seeu.system.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MapUtil {
	
	public static Map sortByValue(Map map) {
		// 将键值对entryset放到链表中
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator() {
			// 将链表按照值得从大到小进行排序
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
						.compareTo(((Map.Entry) (o2)).getValue());
			}
		});
		Map result = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public static void main(String[] args)throws Exception{
		Map<String,Integer>  map = new HashMap<String, Integer>();
		map.put("A", 1);
		map.put("B", 3);
		map.put("C", 2);
		map.put("D", 5);
		map.put("E", 1);
		map = sortByValue(map);
		System.out.println(map);
	}
	
}
