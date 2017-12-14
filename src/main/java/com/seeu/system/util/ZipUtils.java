/**
 * 
 *
 * @project LoginService
 * @file_name ZipUtils.java
 * @author Kevin See
 * @create_time 2014年7月29日 下午4:11:08
 * @last_modified_date 2014年7月29日
 * @version 1.0
 */
package com.seeu.system.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 
 * @author Kevin See
 * @create_time 2014年7月29日 下午4:11:08
 * 
 *              TODO
 */
public class ZipUtils {
	private static final String TAG = "ZipUtils";
	private static final int FILE_BUFFER_SIZE = 1024;

	/**
	 * 
	 */
	public ZipUtils() {
		// TODO Auto-generated constructor stub
	}

	public static boolean cut(String sourceFilePath, String targetDir) {
		boolean completed;
		File sourceFile = new File(sourceFilePath);
		if (!sourceFile.exists())
			return false;

		File targetFile = new File(targetDir);
		if (!targetFile.exists())
			targetFile.mkdirs();
		if (sourceFile.isDirectory()) {
			completed = cutDir(sourceFile.getAbsolutePath(),
					targetFile.getAbsolutePath());
		} else {
			completed = cutFile(sourceFile.getAbsolutePath(),
					targetFile.getAbsolutePath());
		}

		return completed;
	}

	public static boolean cutFile(String sourceFilePath, String targetDir) {

		boolean completed = false;
		File sourceFile = new File(sourceFilePath);
		if (!sourceFile.exists())
			return false;

		File targetFile = new File(targetDir);
		if (!targetFile.exists())
			targetFile.mkdirs();

		try {
			String targetFilePath = targetFile.getAbsolutePath() + "/"
					+ sourceFile.getName();
			FileOutputStream fos = new FileOutputStream(targetFilePath);
			FileInputStream fis = new FileInputStream(sourceFile);
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, count);
			}
			fos.flush();
			fos.close();
			fis.close();

			sourceFile.delete();
			completed = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return completed;
	}

	/**
	 * 把目录sourceFilePath剪切到targetDir目录下
	 * 
	 * @param sourceFilePath
	 * @param targetDir
	 * @return
	 */
	public static boolean cutDir(String sourceFilePath, String targetDir) {
		boolean completed = true;

		File sourceFile = new File(sourceFilePath);
		if (!sourceFile.exists())
			return false;

		File targetFile = new File(targetDir);
		if (!targetFile.exists())
			targetFile.mkdirs();

		String targetFolder = targetFile.getAbsolutePath() + "/"
				+ sourceFile.getName();
		File tf = new File(targetFolder);
		if (tf.exists()) {
			tf.delete();
		}
		tf.mkdirs();

		File[] files = sourceFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				if (!cutDir(file.getAbsolutePath(), targetFolder)) {
					completed = false;
					break;
				}
			} else {
				if (!cutFile(file.getAbsolutePath(), targetFolder)) {
					completed = false;
					break;
				}
			}
		}

		if (completed)
			sourceFile.delete();

		return completed;
	}

	/**
	 * @param baseDirName
	 * @param fileName
	 * @param targerFileName
	 * @return
	 * @throws IOException
	 * 
	 *             将文件或目录压缩成zip格式
	 */
	public static boolean zipFile(String baseDirName, String fileName,
			String targerFileName) throws IOException {
		if (baseDirName == null || "".equals(baseDirName)) {
			return false;
		}
		File baseDir = new File(baseDirName);
		if (!baseDir.exists() || !baseDir.isDirectory()) {
			return false;
		}

		String baseDirPath = baseDir.getAbsolutePath();
		File targerFile = new File(targerFileName);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				targerFile));
		File file = new File(baseDir, fileName);

		boolean zipResult = false;
		if (file.isFile()) {
			zipResult = fileToZip(baseDirPath, file, out);
		} else {
			zipResult = dirToZip(baseDirPath, file, out);
		}
		out.close();
		return zipResult;
	}

	/**
	 * @param fileName
	 * @param unZipDir
	 * @return
	 * @throws Exception
	 * 
	 *             TODO
	 */
	public static boolean unZipFile(String fileName, String unZipDir)
			throws Exception {
		File sourceFile = new File(fileName), desFile = new File(unZipDir);
		/*
		 * if( !sourceFile.exists() ) return false; if( !desFile.exists() ) {
		 * desFile.mkdirs(); } BufferedInputStream is = null; ZipEntry entry;
		 * ZipFile zipfile = new ZipFile( sourceFile ); Enumeration<?>
		 * enumeration = zipfile.entries(); byte data[] = new
		 * byte[FILE_BUFFER_SIZE];
		 * 
		 * while(enumeration.hasMoreElements()) { entry = (ZipEntry)
		 * enumeration.nextElement(); if( entry.isDirectory() ) { File f1 = new
		 * File( unZipDir + "/" + entry.getName() ); if( !f1.exists() ) {
		 * f1.mkdirs(); } } else { is = new BufferedInputStream(
		 * zipfile.getInputStream( entry ) ); int count; String name = unZipDir
		 * + "/" + entry.getName().split("/")[1]; RandomAccessFile m_randFile =
		 * null; File file = new File( name ); if( file.exists() ) {
		 * file.delete(); }
		 * 
		 * file.createNewFile(); m_randFile = new RandomAccessFile( file , "rw"
		 * ); int begin = 0;
		 * 
		 * while((count = is.read( data , 0 , FILE_BUFFER_SIZE )) != -1) { try {
		 * m_randFile.seek( begin ); } catch(Exception ex) { }
		 * 
		 * m_randFile.write( data , 0 , count ); begin = begin + count; }
		 * 
		 * // file.delete(); m_randFile.close(); is.close(); } }
		 * 
		 * sourceFile.delete();
		 */

		return unZipFile(sourceFile, desFile);
	}

	/**
	 * @param fileName
	 * @param unZipDir
	 * @return
	 * @throws Exception
	 * 
	 */
	public static boolean unZipFile(File sourceFile, File targetFile)
			throws Exception {
		File pathFile = targetFile;
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}

		System.out.println(sourceFile.getName());
		ZipFile zip = new ZipFile(sourceFile);
		for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			InputStream in = zip.getInputStream(entry);
			String outPath = (targetFile.getAbsolutePath() + "/" + zipEntryName)
					.replaceAll("\\*", "/");
			;
			// 判断路径是否存在,不存在则创建文件路径
			File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
			if (!file.exists()) {
				file.mkdirs();
			}
			// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if (new File(outPath).isDirectory()) {
				continue;
			}
			// 输出文件路径信息
			System.out.println(targetFile.getAbsolutePath());

			OutputStream out = new FileOutputStream(outPath);
			byte[] buf1 = new byte[1024];
			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			in.close();
			out.close();
		}
		System.out.println("******************解压完毕********************");

		return true;
	}

	

	public static boolean unZipFiles(File sourceFile, File targetFile)
			throws Exception {
		if (!sourceFile.exists())
			return false;
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		BufferedInputStream is = null;
		ZipEntry entry;
		ZipFile zipfile = new ZipFile(sourceFile);
		Enumeration<?> enumeration = zipfile.entries();
		byte data[] = new byte[FILE_BUFFER_SIZE];

		while (enumeration.hasMoreElements()) {
			entry = (ZipEntry) enumeration.nextElement();
			if (entry.isDirectory()) {
				File f1 = new File(targetFile, entry.getName());
				if (!f1.exists()) {
					f1.mkdirs();
				}
			} else {
				is = new BufferedInputStream(zipfile.getInputStream(entry));
				int count;
				String name = targetFile.getAbsolutePath() + "/"
						+ entry.getName().split("/")[1];
				RandomAccessFile m_randFile = null;
				File file = new File(name);
				if (file.exists()) {
					file.delete();
				}

				file.createNewFile();
				m_randFile = new RandomAccessFile(file, "rw");
				int begin = 0;

				while ((count = is.read(data, 0, FILE_BUFFER_SIZE)) != -1) {
					try {
						m_randFile.seek(begin);
					} catch (Exception ex) {
					}

					m_randFile.write(data, 0, count);
					begin = begin + count;
				}

				// file.delete();
				m_randFile.close();
				is.close();
			}
		}

		sourceFile.delete();

		return true;
	}

	/**
	 * @param baseDirPath
	 * @param file
	 * @param out
	 * @return
	 * @throws IOException
	 * 
	 *             文件压缩成zip格式
	 */
	private static boolean fileToZip(String baseDirPath, File file,
			ZipOutputStream out) throws IOException {
		FileInputStream in = null;
		ZipEntry entry = null;

		byte[] buffer = new byte[FILE_BUFFER_SIZE];
		int bytes_read;
		try {
			in = new FileInputStream(file);
			entry = new ZipEntry(getEntryName(baseDirPath, file));
			out.putNextEntry(entry);

			while ((bytes_read = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytes_read);
			}
			out.closeEntry();
			in.close();
		} catch (IOException e) {
			return false;
		} finally {
			if (out != null) {
				out.closeEntry();
			}

			if (in != null) {
				in.close();
			}
		}
		return true;
	}

	/**
	 * @param baseDirPath
	 * @param dir
	 * @param out
	 * @return
	 * @throws IOException
	 * 
	 *             目录压缩成zip格式
	 */
	private static boolean dirToZip(String baseDirPath, File dir,
			ZipOutputStream out) throws IOException {
		if (!dir.isDirectory()) {
			return false;
		}

		File[] files = dir.listFiles();
		if (files.length == 0) {
			ZipEntry entry = new ZipEntry(getEntryName(baseDirPath, dir));

			try {
				out.putNextEntry(entry);
				out.closeEntry();
			} catch (IOException e) {
			}
		}

		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				fileToZip(baseDirPath, files[i], out);
			} else {
				dirToZip(baseDirPath, files[i], out);
			}
		}
		return true;
	}

	private static String getEntryName(String baseDirPath, File file) {
		if (!baseDirPath.endsWith(File.separator)) {
			baseDirPath = baseDirPath + File.separator;
		}

		String filePath = file.getAbsolutePath();
		if (file.isDirectory()) {
			filePath = filePath + "/";
		}

		int index = filePath.indexOf(baseDirPath);
		return filePath.substring(index + baseDirPath.length());
	}

}
