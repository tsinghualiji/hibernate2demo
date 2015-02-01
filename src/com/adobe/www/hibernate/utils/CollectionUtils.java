package com.adobe.www.hibernate.utils;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;

public class CollectionUtils {
	@Test
	public void test1(){
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("a");
		arrayList.add("a");
		HashSet<String> sets = new HashSet<String>(arrayList);
		System.out.println(sets.size());
	}
}
