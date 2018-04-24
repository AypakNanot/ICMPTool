package com.optel.thread;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.optel.bean.ConnectionData;
import com.optel.bean.IcmpPingResponse;
import com.optel.constant.InvariantParameters;
import com.optel.container.IcmpReportDataContainer;
import com.optel.util.WriteFileUtils;

/**
 * 报文结果写入文件线程
 * @author LiH
 * 2018年1月19日 下午3:28:37
 */
public class IcmpFileProcessThread extends Thread {

	@Override
	public void run() {
		while(true){
			try{
				TimeUnit.SECONDS.sleep(InvariantParameters.NDT_THREAD_PROCESS_INTERVAL);
				IcmpReportDataContainer container = IcmpReportDataContainer.getReportContainer();
				HashMap<String, ConnectionData> connections = container.getConnections();
				Set<String> hosts = connections.keySet();
				Iterator<String> hostIterator = hosts.iterator();
				while(hostIterator.hasNext()){
					String host = hostIterator.next();
					ConnectionData threadData = connections.get(host);
					IcmpPingResponse pollIcmp = threadData.pollIcmp();
					if(pollIcmp == null){
						continue;
					}
					StringBuffer sb = new StringBuffer();
					File path = threadData.getIcmpFilePath();
					if(threadData.getIcmpFileSize() == 0){
						while(path.exists()){
							threadData.addIcmpFileCount();
							path = threadData.getIcmpFilePath();
						}
						String t = IcmpPingResponse.getTitle();
						sb.append(t.toCharArray(),0,t.getBytes().length);
					}
					do{
						if(pollIcmp == null){
							break;
						}
						sb.append(pollIcmp).append("\n");
					}while((pollIcmp = threadData.pollIcmp()) != null);
					threadData.addIcmpFileSize(sb.toString().getBytes().length);
					WriteFileUtils writeFileUtils = new WriteFileUtils(sb.toString(), path);
					writeFileUtils.wirteFile();
				}
			}catch(Exception e){
				Logger.log(e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
