package com.optel.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.optel.bean.Ip;
import com.optel.bean.Param;
import com.optel.thread.Logger;

/**
 * ��ȡĿ��������ַ������
 * @author LiH
 * 2018��1��19�� ����3:30:56
 */
public class HostUtils {
	private static final String URL = "res/host.txt";
	
	public static List<Param> readHostList() throws IOException{
		//����ip
		Map<String, String> ipAddr = getIpAddr();
		//����·��
		HashMap<String, String> routeList = getRouteList();
		//��·����·�ɵ�ַת�Ƶ�ʵ�ʵ�ip��ַ��
		Set<String> keySet = routeList.keySet();
		Iterator<String> iterator = keySet.iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			String value = routeList.get(key);
			String wdKey = value.substring(0, value.lastIndexOf("."));
			String vs = ipAddr.get(wdKey);
			if(vs == null){
				continue;
			}
			ipAddr.put(key, vs);
		}
		
		FileReader fileReader = null;
		BufferedReader br = null;
		List<Param> hosts = new ArrayList<Param>();
		try{
			fileReader = new FileReader(new File(URL));
			br = new BufferedReader(fileReader);
			String t = null;
			while((t = br.readLine()) != null){
				String dst = t.trim();
				String srcHostKey = dst.substring(0, dst.lastIndexOf("."));
				String v = ipAddr.get(srcHostKey);
				//ip��ַ���ɴ��ȡ·�ɿ��Ƿ�ɴ� 192.168.0.0 -> 213.213.213.213----192.168.213.2
				if(v == null){
					//Ŀ��ip
					Ip dstIp = new Ip(dst);
					Set<String> ipKeySet = ipAddr.keySet();
					Iterator<String> iterator1 = ipKeySet.iterator();
					boolean b = false;
					while(iterator1.hasNext()){
						//·��Ŀ���ַ
						String key = iterator1.next();
						Ip keyIp = new Ip(key);
						if(keyIp.matcher(dstIp)){
							String value = ipAddr.get(key);
							Param param = new Param(dst,value);
							hosts.add(param);
							b = true;
							break;
						}
					}
					if(!b){
						Logger.log("Ŀ�ĵ�ַ��"+dst+",���ɵ���������õ�ַ���������ݡ�");
					}
				}else{
					Param param = new Param(dst,v);
					hosts.add(param);
				}
			}
		}catch(Exception e){
			Logger.log(e.getMessage());
			e.printStackTrace();
		}finally{
			if(br != null){
				br.close();
				if(fileReader != null){
					fileReader.close();
				}
			}
		}
		return hosts;
	}
	
	/**
	 * ��ȡ·��ӳ��
	 * @author LiH
	 * 2018��1��18�� ����2:13:37
	 * @return
	 * @throws IOException
	 */
	private static HashMap<String,String> getRouteList() throws IOException {
		HashMap<String,String> hm = new HashMap<String, String>();
		Process exec = Runtime.getRuntime().exec("route print -4");
		InputStream is = exec.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String str = null;
		boolean b = false;
		while(true){
			str = br.readLine();
			if(str == null){
				break;
			}
			if(str.contains("����Ŀ��") && str.contains("�ӿ�")){
				b = true;
				continue;
			}
			if(b){
				if(str.contains("=========================")){
					b = false;
					break;
				}
				String sline = str.replaceAll(" +", " ").trim();
				String[] arr = sline.split(" ");
				String net1 = arr[0];
				String gateway = arr[2];
				if(gateway.endsWith("0")){
					continue;
				}
				if(gateway.contains(".")){
					String wd = net1.substring(0, net1.lastIndexOf("."));
					hm.put(wd, gateway);
//					Logger.log(net1+":"+gateway);
				}
			}
		}
		return hm;
	}
	
	/**
	 * ��ȡip��ַӳ��
	 * @author LiH
	 * 2018��1��18�� ����2:04:52
	 * @return
	 * @throws SocketException
	 */
	public static Map<String,String> getIpAddr() throws SocketException{
		HashMap<String,String> hm = new HashMap<String, String>();
		Enumeration<NetworkInterface> all = NetworkInterface.getNetworkInterfaces();
		while(all.hasMoreElements()){
			NetworkInterface nextElement = all.nextElement();
			List<InterfaceAddress> interfaceAddresses = nextElement.getInterfaceAddresses();
			for (int i = 0; i < interfaceAddresses.size(); i++) {
				InterfaceAddress interfaceAddress = interfaceAddresses.get(i);
				InetAddress address = interfaceAddress.getAddress();
				if(address instanceof Inet4Address){
					String hostAddress = address.getHostAddress();
					String wd = hostAddress.substring(0, hostAddress.lastIndexOf("."));
					hm.put(wd, hostAddress);
				}
			}
		}
		return hm;
	}
	
}
