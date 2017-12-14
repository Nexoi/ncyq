package com.seeu.system.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件操作工具类
 * 
 * @author mengbin
 * 
 */
public class FileUtil {
	/**
	 * 获取随机文件名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getUUIDFileName(String filename) {
		String[] tmp = filename.split("\\.");
		if (tmp.length > 1) {
			return RandomUtil.getUUID() + "." + tmp[tmp.length - 1];
		} else {
			return RandomUtil.getUUID();
		}
	}

	/**
	 * 关闭IO
	 * 
	 * @param iss
	 * @param oss
	 */
	public static void closeIO(InputStream[] iss, OutputStream[] oss) {
		if (iss != null && iss.length > 0) {
			for (int i = 0; i < iss.length; i++) {
				if (iss[i] != null) {
					try {
						iss[i].close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (oss != null && oss.length > 0) {
			for (int i = 0; i < oss.length; i++) {
				if (oss[i] != null) {
					try {
						oss[i].close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 保存文件
	 * 
	 * @author: mengbin
	 * 
	 * @param is
	 * @param file
	 * @param bufferSize
	 */
	public static void saveFile(InputStream is, File file, int bufferSize) {
		try {
			OutputStream os = new FileOutputStream(file);
			byte[] buf = new byte[bufferSize];
			int len = 0;
			while ((len = is.read(buf)) > 0) {
				os.write(buf, 0, len);
				os.flush();
			}
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取文件扩展名,包括前面的点号
	 * 
	 * @author: mengbin
	 * 
	 * @param filename
	 * @return
	 */
	public static String getFileExtension(String filename) {
		int pos = filename.indexOf(".");
		if (pos < 0) {
			return "";
		}
		return filename.substring(pos);
	}

	

	/**
	 * 拷贝文件
	 * 
	 * @author mengbin
	 * @date 2013年12月11日 下午2:11:18
	 */
	public static void copyFile(File srcFile, File targetFile) throws Exception {
		byte[] buf = new byte[2048];
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(srcFile);
			os = new FileOutputStream(targetFile);
			int len = 0;
			while ((len = is.read(buf)) > 0) {
				os.write(buf, 0, len);
				os.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeIO(new InputStream[] { is }, new OutputStream[] { os });
		}
	}
	/**
	 * 递归删除目录下所有文件及子目录下所有文件
	 * @param dir 要删除的文件目录
	 * @return
	 * @throws Exception
	 */
	public static boolean deleteDir(File dir)throws Exception{
		if(dir.isDirectory()){
			String[] children = dir.list();
			//递归删除目录中的子目录
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir,children[i]));
				if(!success){
					return false;
				}
			}
		}
		//目录此时为空，可以删除
		return dir.delete();
	}
	
	/**
	 * 递归计算目录下所有文件及子目录下所有文件个数
	 * @param dir 要计算的文件目录
	 * @return
	 * @throws Exception
	 */
	public static int getDirFileCount(File dir,int count)throws Exception{
		if(dir.isDirectory()){
			String[] children = dir.list();
			//递归目录中的子目录
			for (int i = 0; i < children.length; i++) {
				count = getDirFileCount(new File(dir,children[i]),count);
			}
		}else{
			count ++;
		}
		return count;
	}
}
