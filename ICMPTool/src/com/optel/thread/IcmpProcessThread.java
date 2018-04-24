package com.optel.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.optel.bean.IcmpPingResponse;
import com.optel.constant.InvariantParameters;
import com.optel.container.DataContainer;
import com.optel.container.IcmpReportDataContainer;
import com.optel.util.IcmpUtil;

import jpcap.packet.ICMPPacket;

/**
 * 处理数据线程
 * @author LiH
 * 2018年1月18日 下午5:21:39
 */
public class IcmpProcessThread extends Thread {

	private static IcmpProcessThread process;

	private IcmpProcessThread() {
	}

	public static IcmpProcessThread createIcmpProcessThread() {
		if (process == null) {
			process = new IcmpProcessThread();
		}
		return process;
	}

	@Override
	public void run() {
		DataContainer container = DataContainer.getContainer();
		while(true){
			HashMap<String, HashMap<Long, ICMPPacket>> reqMap = null;
			HashMap<String, HashMap<Long, ICMPPacket>> resMap = null;
			ReentrantLock lock = container.getLock();
			try{
				try{
					lock.lock();
					reqMap = container.copyReqMap();
					resMap = container.copyResMap();
				}catch(Exception e){
					Logger.log(e.getMessage());
					e.printStackTrace();
				}finally{
					lock.unlock();
				}
				TimeUnit.MILLISECONDS.sleep(InvariantParameters.NDT_CONNECTION_TIMEOUT + 1000);
				if(reqMap != null){
					doProcessRes(container,reqMap,resMap);
				}
			}catch(Exception e){
				Logger.log(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * 计算
	 * @author LiH
	 * 2018年1月19日 上午9:54:56
	 * @param container
	 * @param reqMap
	 * @param resMap
	 */
	private void doProcessRes(DataContainer container, HashMap<String, HashMap<Long, ICMPPacket>> reqMap, HashMap<String, HashMap<Long, ICMPPacket>> resMap) {
		if(resMap != null){
			Set<String> hosts = resMap.keySet();
			//迭代hostMap
			Iterator<String> hostIterator = hosts.iterator();
			while(hostIterator.hasNext()){
				String host = hostIterator.next();
				HashMap<Long, ICMPPacket> ress = resMap.get(host);
				Set<Long> seqSet = ress.keySet();
				ArrayList<Long> seqList = new ArrayList<Long>();
				seqList.addAll(seqSet);
				Collections.sort(seqList);
				//迭代seq
				Iterator<Long> seqIterator = seqList.iterator();
				while(seqIterator.hasNext()){
					// seq 为序号，也为当前条数
					Long seq = seqIterator.next();
					//接收报文
					ICMPPacket res = ress.get(seq);
					IcmpPingResponse response = IcmpUtil.getResponse(res);
					HashMap<Long, ICMPPacket> reqs = reqMap.get(host);
					if(reqs != null){
						ICMPPacket req = reqs.remove(seq);
						if(req != null){
							//单位 微妙
							long rtt = IcmpUtil.getRtt(req,res);
							//设置耗时时间
							response.setRtt(rtt);
							//超时
							if(rtt > InvariantParameters.NDT_CONNECTION_TIMEOUT * 1000){
								IcmpUtil.setTimeOutRespone(response);
							}
							IcmpReportDataContainer.getReportContainer().add(response);
						}else{
							container.putRes(res);
						}
					}
				}
			}
		}
		
		//处理未接受到的报文
		Set<String> hosts = reqMap.keySet();
		Iterator<String> hostIterator = hosts.iterator();
		while(hostIterator.hasNext()){
			String host = hostIterator.next();
			HashMap<Long, ICMPPacket> reqs = reqMap.get(host);
			Set<Long> seqSet = reqs.keySet();
			ArrayList<Long> seqList = new ArrayList<Long>();
			seqList.addAll(seqSet);
			Collections.sort(seqList);
			//迭代seq
			Iterator<Long> seqIterator = seqList.iterator();
			while(seqIterator.hasNext()){
				// seq 为序号，也为当前条数
				Long seq = seqIterator.next();
				//接收报文
				ICMPPacket req = reqs.get(seq);
				long rtt = IcmpUtil.getRtt(req);
				//如果超时
				if(rtt > InvariantParameters.NDT_CONNECTION_TIMEOUT * 1000){
					ICMPPacket res = container.getRes(req);
					if(res == null){
						//已经超时
						IcmpPingResponse response = IcmpUtil.createTimeOutRespone(req);
						IcmpReportDataContainer.getReportContainer().add(response);
//						Logger.log("microseconds:"+(currentTimeMillis - startMills)+",res:"+response);
					}else{
						//如果获取到的报文，后续处理的容器中，那么放入容器，下一次处理。
						container.putReq(req);
					}
				}else{
					//如何没有超时-》这个时候说明，本地时间和需要测试的设备时间相差比较大。
					container.putReq(req);
				}
				
			}
		}
	}

}
