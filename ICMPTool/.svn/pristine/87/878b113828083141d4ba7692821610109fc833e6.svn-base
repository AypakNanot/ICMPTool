package com.optel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.optel.thread.Logger;

/**
 * 配置文件缓存器
 * 分类存储
 * @author Lih
 *
 */
public class ProperitesUtils {
	/**
	 * 配置文件路径
	 */
	private static final String URL = "res/resource.properties";
	
	/**
	 * 时间格式化
	 */
	private static SimpleDateFormat sdf = null;
	/**
	 * 存储资源所有数据容器
	 */
	private static Map<String,String> map = new HashMap<String,String>();

	/**
	 * 配置文件路径
	 */
	private static File path = new File(URL);
	
	private ProperitesUtils(){}
	
	/**
	 * 加载数据
	 */
	static {
		init();
	}

	/**
	 * s
	 */
	private static void init() {
		FileInputStream is = null;
		try {
			is = new FileInputStream(path);
			Properties properties = new Properties();
			properties.load(is);
			Set<Object> keySet = properties.keySet();
			Iterator<Object> iterator = keySet.iterator();
			while(iterator.hasNext()){
				String key = (String)iterator.next();
				String value = (String) properties.get(key);
				map.put(key, value);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(is != null){
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 获取时间格式
	 * @author LiH
	 * 2018年1月8日 上午9:17:54
	 * @return
	 */
	public static SimpleDateFormat getSdf(){
		if(sdf == null){
			String sdff = map.get("ndt.dataformat");
			sdf = new SimpleDateFormat(sdff);
		}
		return sdf;
	}
	
	/**
	 * 获取其他资源配置
	 * @param otherName
	 * @return
	 */
	public static String get(String name){
		return map.get(name).trim();
	}
	
	public static String get(String name,String t){
		return get(name+t);
	}
	
	/**
	 * 获取为int类型的数据
	 * @author LiH
	 * 2018年1月5日 下午5:19:05
	 * @param name
	 * @return
	 */
	public static int getInt(String name){
		try{
			return Integer.valueOf(map.get(name));
		}catch(Exception e){
			e.printStackTrace();
			Logger.log("获取类型为Int,字段名称为："+name+"时，出现错误。");
		}
		return 0;
	}
	
	public static int getInt(String name,String t){
		return getInt(name+t);
	}
	
	/**
	 * 取值boolean
	 * @author LiH
	 * 2018年1月8日 上午10:24:51
	 * @param name
	 * @return
	 */
	public static Boolean getBoolean(String name){
		try{
			return Boolean.valueOf(map.get(name));
		}catch(Exception e){
			e.printStackTrace();
			Logger.log("获取类型为Boolean,字段名称为："+name+"时，出现错误。");
		}
		return false;
	}
	
	/**
	 * 取值Long
	 * @author LiH
	 * 2018年1月8日 上午10:26:01
	 * @param name
	 * @return
	 */
	public static Long getLong(String name){
		try{
			return Long.valueOf(map.get(name));
		}catch(Exception e){
			e.printStackTrace();
			Logger.log("获取类型为Long,字段名称为："+name+"时，出现错误。");
		}
		return 0L;
	}
	
}
