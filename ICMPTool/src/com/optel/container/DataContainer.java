package com.optel.container;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.optel.thread.Logger;
import com.optel.util.IcmpUtil;

import jpcap.packet.ICMPPacket;

/**
 * ��������
 * �洢�����ģ����ձ��ġ�
 * @author LiH
 * 2018��1��19�� ����3:27:02
 */
public final class DataContainer {

	private static DataContainer inst;

	private DataContainer() {
	}

	public static DataContainer getContainer() {
		if (inst == null) {
			inst = new DataContainer();
		}
		return inst;
	}

	/**
	 * �洢���з�������HashMap<host,HashMap<seq,ICMPPacket>>
	 */
	private static HashMap<String, HashMap<Long, ICMPPacket>> reqMap = new HashMap<String, HashMap<Long, ICMPPacket>>();

	/**
	 * �洢���н�������HashMap<host,HashMap<seq,IcmpPingResponse>>
	 */
	private static HashMap<String, HashMap<Long, ICMPPacket>> resMap = new HashMap<String, HashMap<Long, ICMPPacket>>();
	
	private static final ReentrantLock lock = new ReentrantLock();
	
	public ReentrantLock getLock(){
		return lock;
	}
	
	/**
	 * �����ڴ�
	 * 
	 * @author LiH 2018��1��18�� ����3:34:10
	 * @param request
	 */
	public void putReq(ICMPPacket request) {
		//host
		ReentrantLock lock = getLock();
		try{
			lock.lock();
			String key = request.dst_ip.getHostAddress();
			HashMap<Long, ICMPPacket> map = reqMap.get(key);
			if (map == null) {
				map = new HashMap<Long, ICMPPacket>();
				reqMap.put(key, map);
			}
			Long seqNo = IcmpUtil.getSeq(request);
			map.put(seqNo, request);
		}catch(Exception e){
			Logger.log(e.getMessage());
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
	}
	
	/**
	 * ������
	 * 
	 * @author LiH 2018��1��18�� ����3:51:41
	 * @param packet
	 */
	public void putRes(ICMPPacket response) {
		ReentrantLock lock = getLock();
		try{
			lock.lock();
			String key = response.src_ip.getHostAddress();
			HashMap<Long, ICMPPacket> map = resMap.get(key);
			if (map == null) {
				map = new HashMap<Long, ICMPPacket>();
				resMap.put(key, map);
			}
			Long seqNo = IcmpUtil.getSeq(response);
			map.put(seqNo, response);
		}catch(Exception e){
			//���ǳ����͵ı���
		}finally{
			lock.unlock();
		}
	}
	
	/**
	 * ������ȡ����
	 * @author LiH
	 * 2018��1��18�� ����5:29:35
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, HashMap<Long, ICMPPacket>> copyResMap(){
		HashMap<String, HashMap<Long, ICMPPacket>> obj = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(resMap);
			oos.close();
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			obj = (HashMap<String, HashMap<Long, ICMPPacket>>) ois.readObject();
			ois.close();
			bais.close();
		} catch (Exception e) {
			Logger.log(e.getMessage());
			e.printStackTrace();
		}
		resMap.clear();
		return obj;
	}
	
	/**
	 * �������ͱ���
	 * @author LiH
	 * 2018��1��19�� ����9:53:56
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, HashMap<Long, ICMPPacket>> copyReqMap(){
		HashMap<String, HashMap<Long, ICMPPacket>> obj = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(reqMap);
			oos.close();
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			obj = (HashMap<String, HashMap<Long, ICMPPacket>>) ois.readObject();
			ois.close();
			bais.close();
		} catch (Exception e) {
			Logger.log(e.getMessage());
			e.printStackTrace();
		}
		reqMap.clear();
		return obj;
	}

	public boolean hasMore() {
		return resMap.size() > 0;
	}

	/**
	 * ��ȡ������
	 * @author LiH
	 * 2018��1��18�� ����5:43:31
	 * @param res
	 * @return
	 */
//	public ICMPPacket getReq(IcmpPingResponse res) {
//		HashMap<String, ICMPPacket> icmpMap = reqMap.get(res.getHost());
//		String key = res.getTimes()+"";
//		return icmpMap.remove(key);
//	}
	
	/**
	 * ��ȡ���
	 * @author LiH
	 * 2018��1��19�� ����9:09:02
	 * @param req
	 * @return
	 */
	public ICMPPacket getRes(ICMPPacket req) {
		ICMPPacket res = null;
		HashMap<Long, ICMPPacket> ress = resMap.get(req.dst_ip.getHostAddress());
		if(ress != null){
			res = ress.get(IcmpUtil.getSeq(req));
		}
		return res;
	}
	
}
