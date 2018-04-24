package com.optel.bean;

import java.util.concurrent.atomic.AtomicLong;

import com.optel.constant.InvariantParameters;
import com.optel.thread.Logger;
import com.optel.util.DoubleUtils;
import com.optel.util.TimeUtil;

/**
 * 报表数据
 * @author LiH
 * 2018年1月19日 下午3:26:44
 */
public class ReportData {

	/**
	 * 发送成功次数 
	 */
	public AtomicLong sendSuccTimes = new AtomicLong();
	
	/**
	 * 发送失败次数 
	 */
	public AtomicLong sendFailTimes = new AtomicLong();
	
	/**
	 * 丢包率= sendFailTimes /  (sendSuccTimes+sendFailTimes)
	 */
	public double packetLosRate = 0.00;
	
	/**
	 * 总时延(每次的时延相加,不包含失败的时延) 为了计算准确的平均时延
	 */
	public AtomicLong sumTimedelay = new AtomicLong();
	
	/**
	 * 最大时延
	 */
	public long maxTimedelay = 0;
	/**
	 * 最小时延
	 */
	public long minTimedelay = 0;
	/**
	 * 平均时延  sumTimedelay / sendSuccTimes
	 */
	private double avgTimedelay = 0.00;
	
	/**
	 *  带宽速率kbps
	 */
	public long bandwidthRate;
	
	/**
	 * 当前时间，计算的当前时间。
	 */
	public long currentTime;
	
	/**
	 * 统计周期开始时间
	 */
	public long startTime;
	
	/**
	 * 统计周期结束时间
	 */
	public long endTime;

	/**
	 * 获取平均时延
	 * @author LiH
	 * 2018年1月10日 下午2:23:52
	 * @return
	 */
	public double getAvgTimedelay(){
		avgTimedelay = DoubleUtils.scale(sumTimedelay.get(), sendSuccTimes.get());
		return avgTimedelay;
	}
	
	/**
	 * 获取丢包率
	 * @author LiH
	 * 2018年1月10日 下午2:26:13
	 * @return
	 */
	public double getPacketLosRate() {
		packetLosRate = DoubleUtils.scale(sendFailTimes.get() * 100, sendSuccTimes.get() + sendFailTimes.get());
		return packetLosRate;
	}
	
	@Override
	public String toString() {
		//请在第一行加上title
		if(InvariantParameters.NDT_MACHINE_LANGUAGE){
			return toFileString();
		}
		return toStr();
	}

	public String toStr() {
		return "时间："+TimeUtil.formatDate(currentTime)+",开始时间："+TimeUtil.formatDate(startTime)
				+",结束时间:"+TimeUtil.formatDate(endTime)+",最大时延："+maxTimedelay+"us, 平均时延："+getAvgTimedelay()+"us, 最小时延："+ minTimedelay
				+"us, 丢包率："+getPacketLosRate()+"%, 带宽："+bandwidthRate+"Kbps, 发送包："+sendSuccTimes+"次, "
						+ "失败次数："+sendFailTimes+"次。";
	}
	
	private String toFileString() {
		String[] title = getTitles();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < title.length; i++) {
			String t = title[i].trim();
			sb.append(getStringVlaue(t));
			if(i != title.length - 1){
				sb.append(InvariantParameters.NDT_COLUMN_SEPARATOR);
			}
		}
		return sb.toString();
	}

	/**
	 * 取对应字段值，这样比反射更快。
	 * @author LiH
	 * 2018年1月9日 上午9:13:17
	 * @param filed
	 * @return
	 */
	private String getStringVlaue(String filed) {
		if (filed.contains("currentTime")) {
			return String.valueOf(TimeUtil.formatDate(currentTime));
		}
		if (filed.contains("startTime")) {
			return String.valueOf(TimeUtil.formatDate(startTime));
		}
		if (filed.contains("endTime")) {
			return String.valueOf(TimeUtil.formatDate(endTime));
		}
		if (filed.contains("maxTimedelay")) {
			return String.valueOf(maxTimedelay);
		}
		if (filed.contains("minTimedelay")) {
			return String.valueOf(minTimedelay);
		}
		if (filed.contains("avgTimedelay")) {
			return String.valueOf(getAvgTimedelay());
		}
		if (filed.contains("packetLosRate")) {
			return String.valueOf(getPacketLosRate());
		}
		if (filed.contains("bandwidthRate")) {
			return String.valueOf(bandwidthRate);
		}
		if (filed.contains("sendSuccTimes")) {
			return String.valueOf(sendSuccTimes);
		}
		if (filed.contains("sendFailTimes")) {
			return String.valueOf(sendFailTimes);
		}
		Logger.log("出现错误异常，字段：" + filed + ",未找到。");
		return "";
	}
	/**
	 * title
	 */
	private static String titles[] = null;
	
	/**
	 * 获取用户想要写入文件的字段值
	 * @author LiH
	 * 2018年1月9日 上午9:14:13
	 * @return
	 */
	public static String[] getTitles() {
		if (titles == null) {
			titles = InvariantParameters.NDT_COLUMN_SD.split(",");
		}
		return titles;
	}
	
	public static String getTitle(){
		if(InvariantParameters.NDT_MACHINE_LANGUAGE){
			return InvariantParameters.NDT_COLUMN_SD.replace(",", InvariantParameters.NDT_COLUMN_SEPARATOR)+"\n";
		}
		return "";
	}

}
