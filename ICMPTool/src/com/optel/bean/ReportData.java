package com.optel.bean;

import java.util.concurrent.atomic.AtomicLong;

import com.optel.constant.InvariantParameters;
import com.optel.thread.Logger;
import com.optel.util.DoubleUtils;
import com.optel.util.TimeUtil;

/**
 * ��������
 * @author LiH
 * 2018��1��19�� ����3:26:44
 */
public class ReportData {

	/**
	 * ���ͳɹ����� 
	 */
	public AtomicLong sendSuccTimes = new AtomicLong();
	
	/**
	 * ����ʧ�ܴ��� 
	 */
	public AtomicLong sendFailTimes = new AtomicLong();
	
	/**
	 * ������= sendFailTimes /  (sendSuccTimes+sendFailTimes)
	 */
	public double packetLosRate = 0.00;
	
	/**
	 * ��ʱ��(ÿ�ε�ʱ�����,������ʧ�ܵ�ʱ��) Ϊ�˼���׼ȷ��ƽ��ʱ��
	 */
	public AtomicLong sumTimedelay = new AtomicLong();
	
	/**
	 * ���ʱ��
	 */
	public long maxTimedelay = 0;
	/**
	 * ��Сʱ��
	 */
	public long minTimedelay = 0;
	/**
	 * ƽ��ʱ��  sumTimedelay / sendSuccTimes
	 */
	private double avgTimedelay = 0.00;
	
	/**
	 *  ��������kbps
	 */
	public long bandwidthRate;
	
	/**
	 * ��ǰʱ�䣬����ĵ�ǰʱ�䡣
	 */
	public long currentTime;
	
	/**
	 * ͳ�����ڿ�ʼʱ��
	 */
	public long startTime;
	
	/**
	 * ͳ�����ڽ���ʱ��
	 */
	public long endTime;

	/**
	 * ��ȡƽ��ʱ��
	 * @author LiH
	 * 2018��1��10�� ����2:23:52
	 * @return
	 */
	public double getAvgTimedelay(){
		avgTimedelay = DoubleUtils.scale(sumTimedelay.get(), sendSuccTimes.get());
		return avgTimedelay;
	}
	
	/**
	 * ��ȡ������
	 * @author LiH
	 * 2018��1��10�� ����2:26:13
	 * @return
	 */
	public double getPacketLosRate() {
		packetLosRate = DoubleUtils.scale(sendFailTimes.get() * 100, sendSuccTimes.get() + sendFailTimes.get());
		return packetLosRate;
	}
	
	@Override
	public String toString() {
		//���ڵ�һ�м���title
		if(InvariantParameters.NDT_MACHINE_LANGUAGE){
			return toFileString();
		}
		return toStr();
	}

	public String toStr() {
		return "ʱ�䣺"+TimeUtil.formatDate(currentTime)+",��ʼʱ�䣺"+TimeUtil.formatDate(startTime)
				+",����ʱ��:"+TimeUtil.formatDate(endTime)+",���ʱ�ӣ�"+maxTimedelay+"us, ƽ��ʱ�ӣ�"+getAvgTimedelay()+"us, ��Сʱ�ӣ�"+ minTimedelay
				+"us, �����ʣ�"+getPacketLosRate()+"%, ����"+bandwidthRate+"Kbps, ���Ͱ���"+sendSuccTimes+"��, "
						+ "ʧ�ܴ�����"+sendFailTimes+"�Ρ�";
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
	 * ȡ��Ӧ�ֶ�ֵ�������ȷ�����졣
	 * @author LiH
	 * 2018��1��9�� ����9:13:17
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
		Logger.log("���ִ����쳣���ֶΣ�" + filed + ",δ�ҵ���");
		return "";
	}
	/**
	 * title
	 */
	private static String titles[] = null;
	
	/**
	 * ��ȡ�û���Ҫд���ļ����ֶ�ֵ
	 * @author LiH
	 * 2018��1��9�� ����9:14:13
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
