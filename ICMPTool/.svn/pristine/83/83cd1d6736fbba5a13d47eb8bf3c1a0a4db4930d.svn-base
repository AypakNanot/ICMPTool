package com.optel.thread;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.optel.bean.ConnectionData;
import com.optel.bean.ReportData;
import com.optel.constant.InvariantParameters;
import com.optel.container.IcmpReportDataContainer;
import com.optel.util.WriteFileUtils;

/**
 * 汇总报表写入文件线程
 * @author LiH
 * 2018年1月19日 下午3:29:48
 */
public class SdFileProcessThread extends Thread {

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
					ReportData pollSd = threadData.pollSd();
					if(pollSd == null){
						continue;
					}
					StringBuffer sb = new StringBuffer();
					File path = threadData.getSdFilePath();
					if(threadData.getSdFileSize() == 0){
						while(path.exists()){
							threadData.addSdFileCount();
							path = threadData.getSdFilePath();
						}
						String t = ReportData.getTitle();
						sb.append(t,0,t.getBytes().length);
					}
					do{
						if(pollSd == null){
							break;
						}
						sb.append(pollSd).append("\n");
					}while((pollSd = threadData.pollSd()) != null);
					if(InvariantParameters.NDT_DEBUG){
//						Logger.log(threadData);
					}
					threadData.addSdFileSize(sb.toString().getBytes().length);
					
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
