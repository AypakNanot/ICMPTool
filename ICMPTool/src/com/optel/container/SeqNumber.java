package com.optel.container;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 序号生成器
 * @author LiH
 * 2018年1月19日 下午3:27:57
 */
public class SeqNumber {

	private static SeqNumber seq;
	private SeqNumber(){}
	
	public static SeqNumber getSeqNo(){
		if(seq == null){
			seq = new SeqNumber();
		}
		return seq;
	}
	
	private static Map<String,AtomicLong> seqMap = new HashMap<String, AtomicLong>();
	
	public long getSeq(String host){
		AtomicLong seq = seqMap.get(host);
		if(seq == null){
			seq = new AtomicLong(1);
			seqMap.put(host, seq);
		}
		long seqNo = seq.getAndIncrement();
		return seqNo;
	}
}
