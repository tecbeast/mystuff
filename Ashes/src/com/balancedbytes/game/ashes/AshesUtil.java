package com.balancedbytes.game.ashes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class AshesUtil {

	public static String toString(Object obj) {
		return (obj != null) ? obj.toString() : null;
	}
	
	public static boolean isProvided(String str) {
		return (str != null) && (str.length() > 0);
	}
	
	public static List<Integer> sortHighestFirstRemoveDuplicates(List<Integer> values) {
		List<Integer> result = new ArrayList<Integer>();
		Collections.sort(values, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				if (o1 > o2) {
					return -1;
				} else if (o2 > o1) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		int lastValue = 0;
		for (int i = 0; i < values.size(); i++) {
			if ((i == 0) || (lastValue != values.get(i))) {
				lastValue = values.get(i);
				result.add(lastValue);
			}
		}
		return result;
	}
	
}
