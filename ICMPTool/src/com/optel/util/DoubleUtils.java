package com.optel.util;

import java.math.BigDecimal;

import com.optel.constant.InvariantParameters;

/**
 * double������
 * @author LiH
 * 2018��1��19�� ����3:30:05
 */
public class DoubleUtils {

	/**
	 * ����double
	 * @author LiH
	 * 2018��1��19�� ����3:30:28
	 * @param denominator
	 * @param numerator
	 * @return
	 */
	public static double scale(long denominator, long numerator) {
		double pow = Math.pow(10, InvariantParameters.NDT_PERCENT_LENGTH);
		double quotient = 0.00;
		if (numerator == 0) {
			return quotient;
		} else {
			quotient = denominator * pow / numerator;
		}
		quotient = new BigDecimal(quotient / pow).setScale(
				InvariantParameters.NDT_PERCENT_LENGTH,
				BigDecimal.ROUND_HALF_UP).doubleValue();
		return quotient;
	}

}
