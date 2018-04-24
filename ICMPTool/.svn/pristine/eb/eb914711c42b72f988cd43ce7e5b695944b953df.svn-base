package com.optel.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.optel.constant.InvariantParameters;

/**
 * 时间工具类
 * @author LiH
 * 2018年1月19日 下午3:31:39
 */
public class TimeUtil {

	private final static SimpleDateFormat SDF = new SimpleDateFormat(InvariantParameters.NDT_DATAFORMAT);
	private final static SimpleDateFormat SDF1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	/**
	 * Creates an offset version of the current time
	 * 
	 * @return Date
	 */
	public static Date createDate() {

		// return new Date (getCurrentTimeMillis ());
		return new Date();
	}

	/**
	 * Returns a date formatted in the standard syncvoice format
	 * 
	 * @return String
	 */
	public static String formatDateAsFileSystemName () {
	  
    final Date date = createDate ();
    return formatDate (date);
  }

	/**
	 * Returns a date formatted in the given format
	 * 
	 * @param date
	 * @param format
	 * @return String
	 */
	public static String formatDate(final Date date) {
		return SDF.format(date);
	}
	
	/**
	 * 
	 * @author LiH
	 * 2018年1月10日 下午5:14:11
	 * @param date
	 * @return
	 */
	public static String formatDate(final Long dateTime) {
		return SDF.format(new Date(dateTime));
	}
	
	public static String formatDateNo(final Long dateTime){
		return SDF1.format(new Date(dateTime));
	}
	
	
}