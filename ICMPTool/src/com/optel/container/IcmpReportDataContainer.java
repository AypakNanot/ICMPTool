package com.optel.container;

import java.util.HashMap;
import java.util.List;

import com.optel.bean.ConnectionData;
import com.optel.bean.IcmpPingResponse;
import com.optel.bean.Param;
import com.optel.thread.Logger;

/**
 * 所有报表数据容器
 * @author LiH
 * 2018年1月19日 下午3:27:27
 */
public class IcmpReportDataContainer {

	private static IcmpReportDataContainer container;
	private List<Param> hosts;
	private IcmpReportDataContainer(List<Param> hosts){
		this.hosts = hosts;
		initConnections();
	}
	
	public static IcmpReportDataContainer createReportContainer(List<Param> hosts){
		if(container == null){
			container = new IcmpReportDataContainer(hosts);
		}
		return container;
	}
	
	public static IcmpReportDataContainer getReportContainer(){
		return container;
	}
	/**
	 * 存储每个数据报表
	 */
	private static HashMap<String, ConnectionData> connectionMap = new HashMap<String, ConnectionData>();

	/**
	 * 初始化
	 * @author LiH
	 * 2018年1月19日 下午12:28:41
	 * @param param
	 */
	public void initConnections(){
		for (int i = 0; i < hosts.size(); i++) {
			Param p = hosts.get(i);
			String host = p.getIp();
			ConnectionData connectionData = new ConnectionData(host);
			connectionMap.put(host, connectionData);
		}
	}
	
	public void add(IcmpPingResponse response) {
		ConnectionData connectionData = connectionMap.get(response.getHost());
		//主动记录其他地方运行本程序时的报文数据。
		if(connectionData == null){
			Logger.log("connectionData = null : >> response:"+response);
			connectionData = new ConnectionData(response.getHost());
			connectionMap.put(response.getHost(), connectionData);
		}
		connectionData.add(response);
	}

	public HashMap<String, ConnectionData> getConnections() {
		return connectionMap;
	}
}
