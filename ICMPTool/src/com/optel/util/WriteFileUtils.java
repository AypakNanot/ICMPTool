package com.optel.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.optel.thread.Logger;

/**
 * 写入文件工具类
 * 
 * @author LiH 2018年1月10日 下午5:40:19
 */
public class WriteFileUtils {

	private File filePath;
	private String data;

	public WriteFileUtils(String data, File path) {
		this.data = data;
		this.filePath = path;
	}

	/**
	 * 写入文件，需兼容JDK 1.6
	 * @author LiH
	 * 2018年1月11日 上午9:07:18
	 */
	public void wirteFile() {
		String parent = filePath.getParent();
		File file = new File(parent);
		if(!file.exists()){
			file.mkdirs();
		}
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(filePath, "rw");
			raf.seek(raf.length());
			raf.write(data.getBytes());
		} catch (IOException e) {
			Logger.log(e.getMessage());
			e.printStackTrace();
		}finally{
			if(raf != null){
				try {
					raf.close();
				} catch (IOException e) {
					Logger.log(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
}
