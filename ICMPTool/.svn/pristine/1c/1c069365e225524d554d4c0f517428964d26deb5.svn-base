package com.optel;

import java.io.IOException;

import com.optel.bean.IcmpPingRequest;
import com.optel.thread.Logger;

import jpcap.JpcapSender;

/**
 * 统一发送器
 * @author LiH
 * 2018年1月17日 下午4:55:36
 */
public class IcmpSender {

	private static IcmpSender icmpSender;
	
	private static JpcapSender sender;
	
	@SuppressWarnings("deprecation")
	private IcmpSender(){
		try {
			sender = JpcapSender.openRawSocket();
		} catch (IOException e) {
			Logger.log(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static IcmpSender getSender(){
		if(icmpSender == null || sender == null){
			icmpSender = new IcmpSender();
		}
		return icmpSender;
	}
	
	/**
	 * 发送报文
	 * @author LiH
	 * 2018年1月17日 下午4:54:35
	 * @param packet
	 */
	public void send(IcmpPingRequest packet){
		sender.sendPacket(packet);
	}
	
}
