package com.optel.bean;

import java.io.Serializable;
import java.util.Date;

import com.optel.constant.InvariantParameters;
import com.optel.thread.Logger;
import com.optel.util.TimeUtil;

/**
 * �������
 * @author LiH
 * 2018��1��19�� ����3:26:15
 */
public class IcmpPingResponse implements Serializable{
	/**
	 * @author LiH
	 * 2018��1��18�� ����5:29:28
	 */
	private static final long serialVersionUID = 9160215720189898422L;
	// my attributes
	private boolean successFlag;
	private String errorMessage;
	private String host;
	private String seq;
	private long rtt;
	private long time;
	//����
	private long sec;
	//΢����
	private long usec;
	
	public IcmpPingResponse() {
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	// my attributes
	public void setSuccessFlag(final boolean successFlag) {
		this.successFlag = successFlag;
	}

	public boolean getSuccessFlag() {
		return successFlag;
	}

	public void setErrorMessage(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setHost(final String host) {
		this.host = host;
	}

	public String getHost() {
		return host;
	}

	public long getRtt() {
		return rtt;
	}

	public void setRtt(long rtt) {
		this.rtt = rtt;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	public long getSec() {
		return sec;
	}

	public void setSec(long sec) {
		this.sec = sec;
	}

	public long getUsec() {
		return usec;
	}

	public void setUsec(long usec) {
		this.usec = usec;
	}

	/**
	 * The Object interface
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		//���ڵ�һ�м���title
		if(InvariantParameters.NDT_MACHINE_LANGUAGE){
			return toFileString();
		}
		return toStr();
	}

	public String toStr() {
		return "[" + "time: " + TimeUtil.formatDate(new Date(time)) + ", "
				+ "host: " + host + ", " + "seq: " + seq + ", "
				+ ", " + "size: " + InvariantParameters.NDT_CONNECTION_DATA 
				+ ", " + "rtt: " + rtt +"us"
				+ ", " + "successFlag: "+ successFlag + ", "
				+ "errorMessage: " + errorMessage;
				
	}

	/**
	 * �������ƻ�ȡget����
	 * @author LiH
	 * 2018��1��9�� ����9:10:42
	 * @param fieldName
	 * @return
	 */
	public static String parGetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		int startIndex = 0;
		if (fieldName.charAt(0) == '_') {
			startIndex = 1;
		}
		return "get"
				+ fieldName.substring(startIndex, startIndex + 1).toUpperCase()
				+ fieldName.substring(startIndex + 1);
	}  
    
	/**
	 * ȡ��Ӧ�ֶ�ֵ�������ȷ�����졣
	 * @author LiH
	 * 2018��1��9�� ����9:13:17
	 * @param filed
	 * @return
	 */
	private String getStringVlaue(String filed) {
		if (filed.equals("host")) {
			return getHost();
		}
		if(filed.equals("size")){
			return String.valueOf(InvariantParameters.NDT_CONNECTION_DATA);
		}
		if (filed.equals("seq")) {
			return getSeq();
		}
		if (filed.contains("rtt")) {
			return String.valueOf(getRtt());
		}
		if (filed.equals("time")) {
			return TimeUtil.formatDate(new Date(getTime()));
		}
		if (filed.equals("successFlag")) {
			return Boolean.toString(getSuccessFlag());
		}
		if (filed.equals("errorMessage")) {
			return getErrorMessage();
		}
		Logger.log("���ִ����쳣���ֶΣ�" + filed + ",δ�ҵ���");
		return "";
	}

	/**
	 * title
	 */
	public static String titles[] = null;

	/**
	 * ��ȡ�ֶ�ֵ
	 * @author LiH
	 * 2018��1��9�� ����9:13:04
	 * @return
	 */
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
	 * ��ȡ�û���Ҫд���ļ����ֶ�ֵ
	 * @author LiH
	 * 2018��1��9�� ����9:14:13
	 * @return
	 */
	public static String[] getTitles() {
		if (titles == null) {
			titles = InvariantParameters.NDT_COLUMN.split(",");
		}
		return titles;
	}

	public static String getTitle(){
		if(InvariantParameters.NDT_MACHINE_LANGUAGE){
			return InvariantParameters.NDT_COLUMN.replace(",", InvariantParameters.NDT_COLUMN_SEPARATOR)+"\n";
		}
		return "";
	}
}