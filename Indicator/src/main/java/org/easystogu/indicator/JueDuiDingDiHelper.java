package org.easystogu.indicator;

//绝对顶底
public class JueDuiDingDiHelper {

	public static double hhv(double[] list, int before) {
		int startIndex = (list.length - before) > 0 ? (list.length - 360) : 0;
		double max = 0;
		for (int i = startIndex; i < list.length; i++) {
			if (max < list[i]) {
				max = list[i];
			}
		}
		return max;
	}
}
