package com.optel.container;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ���������
 * @author LiH
 * 2018��1��19�� ����3:27:57
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
