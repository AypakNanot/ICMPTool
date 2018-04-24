package com.optel.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.optel.constant.InvariantParameters;
import com.optel.thread.Logger;
import com.optel.util.TimeUtil;

/**
 * ÿ����������
 * 
 * @author LiH 2018��1��8�� ����8:47:30
 */
public class ConnectionData {

	private static final Long MAX_FILE_SIZE = InvariantParameters.NDT_FILE_SIZE * 1024 * 1024;

	/**
	 * Զ��ip
	 */
	private String host;
	
	/**
	 * ���Ӵ���ʱ��
	 */
	private Long createTime;
	
	/**
	 * �洢�̶������ڵ����ݵ�����
	 */
	private LinkedBlockingQueue<IcmpPingResponse> responses = new LinkedBlockingQueue<IcmpPingResponse>();
	
	/**
	 * �洢�ѷ��ͱ���
	 */
	private Map<String,IcmpPingRequest> reqs = new HashMap<String,IcmpPingRequest>();
	/**
	 * ����ԭ��
	 */
	private ReportData reportData;
	/**
	 * imcpд���ļ�����
	 */
	private LinkedBlockingQueue<IcmpPingResponse> responsesQueue = new LinkedBlockingQueue<IcmpPingResponse>(10000);
	/**
	 * sdд���ļ�����
	 */
	private LinkedBlockingQueue<ReportData> reportDataQueue = new LinkedBlockingQueue<ReportData>(10000);
	
	/**
	 * icmp�ļ�����
	 */
	private AtomicInteger icmpFileCount = new AtomicInteger(1);
	/**
	 * sd�ļ�����
	 */
	private AtomicInteger sdFileCount = new AtomicInteger(1);
	/**
	 * icmp�ļ���С
	 */
	private AtomicLong icmpFileSize = new AtomicLong();
	
	/**
	 * sd�ļ���С
	 */
	private AtomicLong sdFileSize = new AtomicLong();
	
	public File getIcmpFilePath(){
		String tn = "0000000000"+icmpFileCount.get();
		String n = tn.substring(tn.length()-InvariantParameters.NDT_FILE_NO_LENGTH);
		String fileName = String.format(InvariantParameters.NDT_FILE_NAME_ICMP, TimeUtil.formatDateNo(createTime),host,n);
		File file = new File(InvariantParameters.NDT_FILE_PATH + File.separator+"ICMP"+File.separator + fileName);
		return file;
	}
	
	public ConnectionData(String dstHost) {
		reportData = new ReportData();
		this.host = dstHost;
		createTime = System.currentTimeMillis();
	}

	/**
	 * ����ֽڣ�����������ƣ���Ϊ���ļ�
	 * @author LiH
	 * 2018��1��11�� ����9:31:55
	 * @param delta
	 * @return
	 */
	public long addIcmpFileSize(long delta){
		icmpFileSize.getAndAdd(delta);
		if(getIcmpFileSize() > MAX_FILE_SIZE){
			addIcmpFileCount();
			icmpFileSize.set(delta);
		}
		return getIcmpFileSize();
	}
	
	public Long getIcmpFileSize(){
		return icmpFileSize.get();
	}
	
	public void addIcmpFileCount(){
		icmpFileCount.getAndIncrement();
	}
	
	public long addSdFileSize(long delta){
		sdFileSize.getAndAdd(delta);
		if(getSdFileSize() > MAX_FILE_SIZE){
			addSdFileCount();
			sdFileSize.set(delta);
		}
		return getSdFileSize();
	}
	
	public Long getSdFileSize(){
		return sdFileSize.get();
	}
	
	public void addSdFileCount(){
		sdFileCount.getAndIncrement();
	}
	
	public File getSdFilePath() {
		String tn = "0000000000"+sdFileCount.get();
		String n = tn.substring(tn.length()-InvariantParameters.NDT_FILE_NO_LENGTH);
		String fileName = String.format(InvariantParameters.NDT_FILE_NAME_SD, TimeUtil.formatDateNo(createTime),host,n);
		File file = new File(InvariantParameters.NDT_FILE_PATH + File.separator+"SD"+File.separator + fileName);
		return file;
	}
	
	public String getHost() {
		return host;
	}

	
	public Long getCreateTime() {
		return createTime;
	}

	@Override
	public String toString() {
		return "remoteIp:" + host + ", " + reportData.toStr();
	}

	public ReportData getReportData() {
		return reportData;
	}

	public void setReportData(ReportData reportData) {
		this.reportData = reportData;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	public ArrayList<IcmpPingResponse> getResponses() {
		if(InvariantParameters.NDT_BANDWIDTH_CYCLE < responses.size()){
			ArrayList<IcmpPingResponse> endList = new ArrayList<IcmpPingResponse>(InvariantParameters.NDT_BANDWIDTH_CYCLE);
			for (int i = 0; i < InvariantParameters.NDT_BANDWIDTH_CYCLE; i++) {
				IcmpPingResponse res = responses.poll();
				if(res != null){
					endList.add(res);
				}
			}
			return endList;
		}
		return null;
	}

	/**
	 * ����icmp�ļ�����
	 * @author LiH
	 * 2018��1��10�� ����5:07:39
	 * @param response
	 */
	public void putIcmp(IcmpPingResponse response){
		try {
			responsesQueue.put(response);
		} catch (InterruptedException e) {
			Logger.log(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public IcmpPingResponse pollIcmp(){
		return responsesQueue.poll();
	}
	
	public String getIcmpPingRequestKey(IcmpPingRequest req){
		return req.getHost()+":"+req.seq;
	}
	
	public void put(IcmpPingRequest req){
		String key = getIcmpPingRequestKey(req);
		reqs.put(key, req);
	}
	
	public Map<String,IcmpPingRequest> getReqs(){
		return reqs;
	}
	
	/**
	 * ����sd�ļ�����
	 * @author LiH
	 * 2018��1��10�� ����5:10:37
	 * @param rd
	 */
	public void putSd(ReportData rd){
		try {
			if(InvariantParameters.NDT_DEBUG){
				Logger.log(rd);
			}
			reportDataQueue.put(rd);
		} catch (InterruptedException e) {
			Logger.log(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public ReportData pollSd(){
		return reportDataQueue.poll();
	}
	
	public void add(IcmpPingResponse response){
		try {
			putIcmp(response);
			responses.put(response);
		} catch (InterruptedException e) {
			Logger.log(e.getMessage());
			e.printStackTrace();
		}
		if(InvariantParameters.NDT_DEBUG){
			Logger.log(response);
		}
	}
}
