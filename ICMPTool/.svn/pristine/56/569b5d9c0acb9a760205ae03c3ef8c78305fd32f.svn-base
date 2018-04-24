package com.optel.bean;

import java.net.InetAddress;

import jpcap.packet.ICMPPacket;
import jpcap.packet.IPPacket;

/**
 * 发送报文
 * @author LiH
 * 2018年1月18日 下午3:45:48
 */
public class IcmpPingRequest extends ICMPPacket {

	/**
	 * @author LiH 2018年1月17日 下午5:11:52
	 */
	private static final long serialVersionUID = 5714799293129016156L;
	// my attributes
	private String host;
	private long timeout;
	private long times;

	public void setIpV4Paramter(InetAddress src, InetAddress dst) {
		setIPv4Parameter(0, false, false, false, 0, false, false, false, 0,
				1010101, 100, IPPacket.IPPROTO_ICMP, src, dst);
	}

	// my attributes
	public void setHost(final String host) {
		this.host = host;
	}

	public String getHost() {
		return host;
	}

	public void setTimeout(final long timeout) {
		this.timeout = timeout;
	}

	public long getTimeout() {
		return timeout;
	}

	public long getTimes() {
		return times;
	}

	public void setTimes(long times) {
		this.times = times;
	}
}