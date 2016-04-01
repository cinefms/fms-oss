package com.openfms.model.utils;

import java.util.List;

public class StatusCombine {

	public static int combine(List<Integer> statuses) {
		int[] x = new int[statuses.size()];
		for(int i=0;i<x.length;i++) {
			x[i] = statuses.get(i);
		}
		return combine(x);
	}

	public static int combine(int... statuses) {
		int out = -1;
		for(int i : statuses) {
			if(i!=-1) {
				if(out==-1 || i<out) {
					out = i;
				}
			}
		}
		return out;
	}
	
}
