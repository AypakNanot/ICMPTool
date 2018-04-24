package com.optel.thread;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;

import com.optel.util.TimeUtil;
import com.optel.util.WriteFileUtils;

/**
 * 处理数据线程
 * @author LiH
 * 2018年1月18日 下午5:21:39
 */
public class Logger extends Thread {

	static{
		new Logger();
	}

	private Logger() {
		setName("pool-Logger-1");
		start();
	}

	private static LinkedBlockingQueue<String> logger = new LinkedBlockingQueue<String>();
	
	@Override
	public void run() {
		File logFile = new File("./log.txt");
		while(true){
			try{
				String log = logger.take();
				log = TimeUtil.formatDateAsFileSystemName()+" " + log + "\r\n";
				WriteFileUtils wf = new WriteFileUtils(log, logFile);
				wf.wirteFile();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void log(Object log){
		try {
			System.out.println(log);
			logger.put(log.toString());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
