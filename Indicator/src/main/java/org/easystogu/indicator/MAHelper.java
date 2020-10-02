package org.easystogu.indicator;

import org.springframework.stereotype.Component;

@Component
public class MAHelper extends IND {

	public double[] getMAList(double[] CLOSE, int day) {
		return MA(CLOSE, day);
	}
}
