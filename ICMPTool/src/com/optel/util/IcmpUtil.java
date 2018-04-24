package com.optel.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.optel.bean.IcmpPingRequest;
import com.optel.bean.IcmpPingResponse;
import com.optel.bean.Param;
import com.optel.constant.InvariantParameters;
import com.optel.container.SeqNumber;

import jpcap.packet.ICMPPacket;

/**
 * 报文工具类
 * @author LiH
 * 2018年1月19日 下午3:31:20
 */
public class IcmpUtil {

	final static String DATA_32 = "abcdefghijklmnopqrstuvwabcdefghi";
	final static Pattern P = Pattern.compile("[0-9]*");
	
	/**
	 * 获取指定长度报文
	 * 把当前发送时间和发送的次数跟随着报文发出去，接收的时候，在报文中获取。
	 * @author LiH
	 * 2018年1月17日 下午4:39:11
	 * @param len 发送报文长度，注意报文长度必须是 16 的倍数。
	 * @param seq 
	 * @return
	 */
	public static byte[] getData(int len, long seq){
		String millis = System.currentTimeMillis()+"@"+seq+"@";
		StringBuffer sb = new StringBuffer(millis);
		int l = len / 32 + 1;
		for (int i = 0; i < l; i++) {
			sb.append(DATA_32);
		}
		byte[] data = sb.substring(0, len).getBytes();
		return data;
	}
	
	/**
	 * 根据报文获取结果
	 * @author LiH
	 * 2018年1月18日 下午4:21:33
	 * @param packet
	 * @return
	 */
	public static IcmpPingResponse getResponse(ICMPPacket packet){
		final IcmpPingResponse response = new IcmpPingResponse();
		response.setHost(packet.src_ip.getHostAddress());
		response.setErrorMessage("SUCCESS");
		response.setSuccessFlag(true);
		response.setTime(IcmpUtil.getTime(packet));
		response.setSec(packet.sec);
		response.setUsec(packet.usec);
		response.setSeq(String.valueOf(IcmpUtil.getSeq(packet)));
		return response;
	}
	
	/**
	 * 根据报文获取报文里面序号
	 * @author LiH
	 * 2018年1月18日 下午4:30:51
	 * @param packet
	 * @return
	 */
	public static Long getSeq(ICMPPacket packet){
		String data = new String(packet.data);
		String[] ds = data.split("@");
		String seq = ds[1];
		Long seqNo = Long.valueOf(seq);
		return seqNo;
	}
	
	/**
	 * 
	 * @author LiH
	 * 2018年1月19日 下午1:32:51
	 * @param packet
	 * @return
	 */
	public static Long getTime(ICMPPacket packet){
		String data = new String(packet.data);
		String[] ds = data.split("@");
		String time = ds[0];
		return Long.valueOf(time);
	}

	/**
	 * 创建发送报文
	 * @author LiH
	 * 2018年1月18日 下午6:33:59
	 * @param param
	 * @return
	 * @throws UnknownHostException
	 */
	public static IcmpPingRequest createReqest(Param param) throws UnknownHostException {
		IcmpPingRequest request = new IcmpPingRequest ();
		request.type = ICMPPacket.ICMP_ECHO;
		request.setHost(param.getIp());
		long seq = SeqNumber.getSeqNo().getSeq(param.getIp());
		request.data = IcmpUtil.getData(InvariantParameters.NDT_CONNECTION_DATA,seq);
		request.setTimes(seq);
		request.setTimeout(InvariantParameters.NDT_CONNECTION_TIMEOUT);
		InetAddress dst = InetAddress.getByName(param.getIp());
		InetAddress src = InetAddress.getByName(param.getSrcHost());
		request.setIpV4Paramter(src, dst);
		return request;
	}

	/**
	 * 自检不能到达
	 * @author LiH
	 * 2018年1月18日 下午6:34:09
	 * @param req 
	 * @return
	 */
	public static IcmpPingResponse createTimeOutRespone(ICMPPacket req) {
		final IcmpPingResponse response = new IcmpPingResponse ();
		final String errorMessage = "Last error: timeout or unreachable" ;
        response.setErrorMessage (errorMessage);
        response.setSuccessFlag (false);
        response.setTime(IcmpUtil.getTime(req));
        response.setHost (req.dst_ip.getHostAddress());
        response.setSeq(String.valueOf(IcmpUtil.getSeq(req)));
		return response;
	}

	/**
	 * 设置超时报文
	 * @author LiH
	 * 2018年1月22日 下午2:00:39
	 * @param response
	 */
	public static void setTimeOutRespone(IcmpPingResponse response) {
		response.setErrorMessage ("Last error: timeout");
        response.setSuccessFlag (false);
	}
	
	/**
	 * 验证是否是本程序的报文数据
	 * @author LiH
	 * 2018年1月19日 下午3:04:09
	 * @param packet
	 * @return
	 */
	public static boolean checkData(ICMPPacket packet) {
		String data = new String(packet.data);
		if(!data.contains("@")){
			return false;
		}
		String[] arr = data.split("@");
		if(arr.length != 3){
			return false;
		}
		if(!isNumber(arr[0])){
			return false;
		}
		if(!isNumber(arr[1])){
			return false;
		}
		return true;
	}
	
	/**
	 * 判断是否为数字 number
	 * @author LiH
	 * 2018年1月19日 下午3:10:33
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str){
		if(str == null){
			return false;
		}
		Matcher matcher = P.matcher(str);
		return matcher.matches();
	}

	/**
	 * 获取来回耗时 单位微妙
	 * @author LiH
	 * 2018年1月25日 上午9:24:08
	 * @param req
	 * @return
	 */
	public static long getRtt(ICMPPacket req) {
		setPacketTime(req);
		//当前时间 微妙
		long currentTimeUs = System.currentTimeMillis() * 1000;
		//相差微妙数
		long us = currentTimeUs - req.sec * 1000 * 1000  - req.usec;
		us = (us < 0) ? -us : us;
		return us;
	}

	/**
	 * 获取来回耗时 单位微妙
	 * @author LiH
	 * 2018年1月25日 上午9:28:31
	 * @param req
	 * @param res
	 * @return
	 */
	public static long getRtt(ICMPPacket req, ICMPPacket res) {
		setPacketTime(req);
		long startReq = req.sec * 1000 * 1000 + req.usec ;
		long endRes = res.sec * 1000 * 1000 + res.usec ;
		long us = endRes - startReq;
		us = (us < 0) ? -us : us;
		return us;
	}
	
	/**
	 * 取出报文中的时间放入字段值
	 * @author LiH
	 * 2018年1月25日 上午9:54:35
	 * @param packet
	 */
	public static void setPacketTime(ICMPPacket packet){
		if(packet.sec < 10000){
			//毫秒数
			Long millis = getTime(packet);
			//秒
			long sec = millis / 1000;
			//微妙
			long usec = (millis - sec * 1000) * 1000;
			packet.sec = sec;
			packet.usec = usec;
		}
	}
	
}
