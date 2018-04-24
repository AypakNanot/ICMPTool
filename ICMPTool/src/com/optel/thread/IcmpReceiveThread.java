package com.optel.thread;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.optel.container.DataContainer;
import com.optel.util.IcmpUtil;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.packet.ICMPPacket;
import jpcap.packet.Packet;

/**
 * 接收报文线程
 * @author LiH
 * 2018年1月19日 下午3:29:04
 */
public class IcmpReceiveThread extends Thread {
	private JpcapCaptor captor;
	private NetworkInterface network;
	public IcmpReceiveThread(NetworkInterface network) {
		this.network = network;
	}

	public void getCaptor(){
		try {
			captor = JpcapCaptor.openDevice(network, 20000, false, 1);
			captor.setFilter("icmp", true);
		} catch (IOException e) {
			Logger.log(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		
		while (true) {
			if(captor == null){
				getCaptor();
			}
			Packet pk = captor.getPacket();
			if (pk == null) {
				try {
					TimeUnit.MILLISECONDS.sleep(1);
					continue;
				} catch (InterruptedException e) {
					Logger.log(e.getMessage());
					e.printStackTrace();
				}
			}
			if(pk instanceof ICMPPacket){
				ICMPPacket packet = (ICMPPacket) pk;
				if(IcmpUtil.checkData(packet)){
					//接收
					if (packet.type == ICMPPacket.ICMP_ECHOREPLY) {
						DataContainer.getContainer().putRes(packet);
					}
					//发送
					if(packet.type == ICMPPacket.ICMP_ECHO){
						DataContainer.getContainer().putReq(packet);
					}
				}else{
					if(((ICMPPacket) pk).type == 8 || ((ICMPPacket) pk).type == 0){
						Logger.log("错误的包："+pk);
					}
				}
			}
		}
	}
}
