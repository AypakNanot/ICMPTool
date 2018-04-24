package com.optel.bean;

/**
 * 参数地址
 * @author LiH
 * 2018年1月19日 下午3:26:32
 */
public class Param {

	private String ip;
	private String srcHost;
	
	public String getSrcHost() {
		return srcHost;
	}

	public void setSrcHost(String srcHost) {
		this.srcHost = srcHost;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Param(String ip,String srcHost) {
		super();
		this.ip = ip;
		this.srcHost = srcHost;
	}

	@Override
	public String toString() {
		return "src:"+srcHost+"->dst:"+ip;
	}
}
