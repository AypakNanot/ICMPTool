package com.optel.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.optel.bean.ConnectionData;
import com.optel.bean.IcmpPingResponse;
import com.optel.bean.ReportData;
import com.optel.constant.InvariantParameters;
import com.optel.container.IcmpReportDataContainer;

/**
 * 带宽计算线程
 * @author LiH
 * 2018年1月19日 下午1:38:29
 */
public class BandwidthProcessThread extends Thread {

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
					ArrayList<IcmpPingResponse> responses = threadData.getResponses();
					if(responses == null){
						continue;
					}
					ReportData reportData = new ReportData();
					int size = responses.size();
					// 只存储有效值
					// 存储所有需要计算的包长
					AtomicLong sizes = new AtomicLong();
					// 存储所有时延
					AtomicLong rtts = new AtomicLong();
					
					long startTime = 0;
					long endTime = 0;
					for (int j = 0; j < size; j++) {
						IcmpPingResponse icmp = responses.get(j);
						if (icmp.getSuccessFlag()) {
							sizes.getAndAdd(InvariantParameters.NDT_CONNECTION_DATA);
							rtts.getAndAdd(icmp.getRtt());
						}
						long time = icmp.getTime();
						if(j == 0){
							startTime = time;
						}
						if(j == size - 1){
							endTime = time;
						}
						calculate(icmp, reportData);
					}
					reportData.startTime = startTime;
					reportData.endTime = endTime;

					if (sizes.get() > 0) {
						if(rtts.get() == 0){
							rtts.getAndIncrement();
						}
						long b = sizes.get() * 8 * 2 * 1000 / rtts.get();
						reportData.bandwidthRate = b;
					}
					reportData.currentTime = System.currentTimeMillis();
					threadData.putSd(reportData);
				}
			}catch(Exception e){
				Logger.log(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 把所需要的计算的数据加入报表数据中
	 * @author LiH
	 * 2018年1月10日 下午2:00:39
	 * @param response
	 */
	private void calculate(IcmpPingResponse response,ReportData reportData) {
		if(response.getSuccessFlag()){
			//记录成功次数
			reportData.sendSuccTimes.getAndIncrement();
			//一次时延
			long rtt = response.getRtt();
			reportData.sumTimedelay.addAndGet(rtt);
			//最小时延
			reportData.minTimedelay = reportData.minTimedelay > rtt ? rtt : reportData.minTimedelay;
			if(reportData.minTimedelay == 0 && reportData.sendSuccTimes.get() == 1){
				reportData.minTimedelay = rtt;
			}
			//最大时延
			reportData.maxTimedelay = reportData.maxTimedelay < rtt ? rtt : reportData.maxTimedelay;
		}else{
			//记录失败次数
			reportData.sendFailTimes.getAndIncrement();
		}
	}
}
