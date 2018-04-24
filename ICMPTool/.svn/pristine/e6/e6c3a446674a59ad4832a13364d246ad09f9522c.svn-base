package com.optel.util;

import java.math.BigDecimal;

import com.optel.constant.InvariantParameters;

/**
 * double工具类
 * @author LiH
 * 2018年1月19日 下午3:30:05
 */
public class DoubleUtils {

	/**
	 * 计算double
	 * @author LiH
	 * 2018年1月19日 下午3:30:28
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
