package com.optel.run;

import java.util.List;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

import com.optel.bean.Param;
import com.optel.constant.InvariantParameters;
import com.optel.container.IcmpReportDataContainer;
import com.optel.database.Connections;
import com.optel.thread.BandwidthProcessThread;
import com.optel.thread.IcmpFileProcessThread;
import com.optel.thread.IcmpProcessThread;
import com.optel.thread.IcmpReceiveThread;
import com.optel.thread.IcmpSenderThread;
import com.optel.thread.Logger;
import com.optel.thread.SdFileProcessThread;
import com.optel.util.HostUtils;

/**
 * 主程序入口
 * 本程序需兼容jdk 1.6 一些高性能写法需摒弃。如：写文件，读文件，多线程计算等等...
 * @author LiH
 * 2018年1月19日 下午3:28:22
 */
public class Ping {

	public static void main(String[] args) throws Exception {
		if(InvariantParameters.NDT_DST_HOST_DATABASE){
			Logger.log("loading database ne ipaddr...");
			readHosts();
		}
		Logger.log("start...");
		ping();
	}

	private static void readHosts() {
		Connections connections = new Connections();
		connections.doDataConfig();
	}

	private static void ping() throws Exception {
		List<Param> hosts = HostUtils.readHostList();
		if(hosts.size() > 0){
			for (int i = 0; i < hosts.size(); i++) {
				Logger.log(hosts.get(i));
			}
			
			//开启接受线程
			startReceiveThread();
			//开启处理数据线程
			statrProcess();
			//开启发送线程
			startSenderThread(hosts);
			//初始化容器
			IcmpReportDataContainer.createReportContainer(hosts);
			//开启写入ICMP文件线程
			startWriteIcmpThread();
			//开启带宽计算线程
			startBandwidthProcessThread();
			//开启写入Sd文件线程
			startWriteSdThread();
		}
	}
	
	/**
	 * 开启带宽计算线程
	 * @author LiH
	 * 2018年1月19日 下午1:37:50
	 */
	private static void startBandwidthProcessThread() {
		BandwidthProcessThread process = new BandwidthProcessThread();
		process.setName("pool-BandwidthProcessThread-1");
		process.start();
	}

	/**
	 * 开启写入每条结果报文线程
	 * @author LiH
	 * 2018年1月19日 下午12:16:29
	 */
	private static void startWriteIcmpThread() {
		IcmpFileProcessThread process = new IcmpFileProcessThread();
		process.setName("pool-IcmpFileProcess-1");
		process.start();
	}
	
	/**
	 * 开启写入Sd文件线程
	 * @author LiH
	 * 2018年1月19日 下午12:19:35
	 */
	private static void startWriteSdThread() {
		SdFileProcessThread process = new SdFileProcessThread();
		process.setName("pool-SdFileProcess-1");
		process.start();
	}

	/**
	 * 开启处理数据线程
	 * @author LiH
	 * 2018年1月19日 下午12:19:51
	 */
	private static void statrProcess() {
		IcmpProcessThread process = IcmpProcessThread.createIcmpProcessThread();
		process.setName("pool-IcmpProcessThread-1");
		process.start();
	}

	/**
	 * 开启发送线程
	 * @author LiH
	 * 2018年1月18日 下午3:11:07
	 * @param hosts
	 * @throws Exception
	 */
	private static void startSenderThread(List<Param> hosts) throws Exception {
		IcmpSenderThread executor = IcmpSenderThread.createSenderThread(hosts);
		executor.start();
	}

	/**
	 * 开启接受线程
	 * @author LiH
	 * 2018年1月18日 下午3:11:14
	 */
	private static void startReceiveThread() {
		NetworkInterface[] deviceList = JpcapCaptor.getDeviceList();
		for (int i = 0; i < deviceList.length; i++) {
			NetworkInterface network = deviceList[i];
			IcmpReceiveThread receiveThread = new IcmpReceiveThread(network);
			receiveThread.setName("pool-receiveThread-"+(i+1));
			receiveThread.start();
		}
	}
}
