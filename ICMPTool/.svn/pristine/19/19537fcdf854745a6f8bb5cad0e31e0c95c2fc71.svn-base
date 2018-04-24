package com.optel.thread;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.optel.IcmpSender;
import com.optel.bean.IcmpPingRequest;
import com.optel.bean.Param;
import com.optel.constant.InvariantParameters;
import com.optel.container.DataContainer;
import com.optel.util.IcmpUtil;

/**
 * 发送报文线程 
 * @author LiH 2018年1月10日 上午9:07:00
 */
public class IcmpSenderThread extends Thread {

	private static IcmpSenderThread executor;
	private List<Param> params;

	private IcmpSenderThread(List<Param> params) throws Exception {
		executor = this;
		this.params = params;
		setName("pool-IcmpSenderThread-1");
	}

	public static IcmpSenderThread createSenderThread(List<Param> params)
			throws Exception {
		if (executor == null) {
			executor = new IcmpSenderThread(params);
		}
		return executor;
	}

	@Override
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(1);
			final int interval = InvariantParameters.NDT_CONNECTION_INTERVAL;
			final int times = InvariantParameters.NDT_CONNECTION_TIMES;
			CountDownLatch count = new CountDownLatch(times);
			while (true) {
				if(count.getCount() == 0 && times > 0){
					break;
				}
				long startTime = System.nanoTime();
					for (int i = 0; i < params.size(); i++) {
						Param param = params.get(i);
						try {
							IcmpPingRequest req = IcmpUtil.createReqest(param);
							if(InvariantParameters.NDT_RECORD_UNREACHABLE){
								DataContainer.getContainer().putReq(req);
							}
							IcmpSender.getSender().send(req);
						} catch (Exception e) {
							Logger.log(e.getMessage());
							e.printStackTrace();
							Logger.log("param:"+param);
						}
					}
				// delegate
				long endTime = System.nanoTime();
				// rest
				long sleepTime = interval
						- (Math.abs(endTime - startTime) / 1000 / 1000);
				try {
					if(times > 0){
						count.countDown();
					}
					TimeUnit.MILLISECONDS.sleep(sleepTime);
				} catch (InterruptedException e) {
					Logger.log(e.getMessage());
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			Logger.log(e.getMessage());
			e.printStackTrace();
		}
	}
}
