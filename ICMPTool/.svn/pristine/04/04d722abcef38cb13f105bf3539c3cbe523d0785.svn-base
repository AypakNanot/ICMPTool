package com.optel.bean;


public class Ip {
	public int ipn1 = 0;
	public int ipn2 = 0;
	public int ipn3 = 0;
	public int ipn4 = 0;

	public String ip = "0.0.0.0";

	public Ip() {
	}

	public Ip(String ipAddr) {
		this.ip = ipAddr;
		String[] iparr = ipAddr.split("\\.");
		int l = iparr.length;
		this.toIp(ipAddr, l);
	}
	
	public String get3Ip() {
		return ipn1 + "." + ipn2 + "." + ipn3;
	}

	public void toIp(String ipAddr, int wz) {
		this.ip = ipAddr;
		String[] iparr = ipAddr.split("\\.");
		if (wz >= 1) {
			ipn1 = Integer.valueOf(iparr[0]);
		}
		if (wz >= 2) {
			ipn2 = Integer.valueOf(iparr[1]);
		}
		if (wz >= 3) {
			ipn3 = Integer.valueOf(iparr[2]);
		}
		if (wz >= 4) {
			ipn4 = Integer.valueOf(iparr[3]);
		}
	}

	@Override
	public String toString() {
		return ip;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public boolean matcher(Ip match) {
		if (match.ipn1 == 0) {
			return true;
		}
		if (match.ipn1 != ipn1 && ipn1 != 0) {
			return false;
		}
		if (match.ipn2 == 0) {
			return true;
		}
		if (match.ipn2 != ipn2 && ipn2 != 0) {
			return false;
		}
		if (match.ipn3 == 0) {
			return true;
		}
		if (match.ipn3 != ipn3 && ipn3 != 0) {
			return false;
		}
		if (ipn4 == 0) {
			return true;
		}
		if (match.ipn4 != ipn4 && ipn4 != 0) {
			return false;
		}
		return false;
	}

}
