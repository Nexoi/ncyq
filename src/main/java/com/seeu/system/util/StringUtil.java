package com.seeu.system.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;

/**
 * 字符串操作工具类
 * 
 * @author mengbin
 * @date 2014年1月8日 上午9:40:10
 */
public class StringUtil {
	private static final Log log = LogFactory.getLog(StringUtil.class);

	private static Policy policy = null;

	static {
		try {
			/*String filename = "antisamy.xml";
			policy = Policy.getInstance(StringUtil.class.getClassLoader()
					.getResource(filename));
			log.info("加载AntiSamy配置文件:" + filename);*/
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("初始化AntiSamy失败[" + e.toString() + "]");
			}
		}
	}

	/**
	 * 获取驼峰命名
	 * 
	 * @param name
	 *            只能使用下划线分割
	 * @return 首字母小写,第二单词首字母大写,去掉下划线
	 */
	public static String getCamel(String name) {
		name = name.toLowerCase();
		String[] tmps = name.split("_");
		if (tmps.length == 1) {
			return name;
		}
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < tmps.length; i++) {
			if (i == 0) {
				buf.append(tmps[i]);
			} else {
				String tmp = tmps[i];
				buf.append(tmp.substring(0, 1).toUpperCase()).append(
						tmp.substring(1));
			}
		}
		return buf.toString();
	}

	public static boolean isEmptyString(String str) {
		return str == null || "".equals(str);
	}

	/**
	 * 字符串左边打补丁
	 * 
	 * @author mengbin
	 * @param str
	 *            : 需要打补丁的字符串
	 * @param padChar
	 *            : 补丁字符
	 * @param len
	 *            : 打补丁后的长度
	 * @date 2013-12-16 下午07:33:25
	 */
	public static String paddingLeft(String str, String padChar, int len) {
		if (str == null) {
			return null;
		}
		if (str.length() >= len) {
			return str;
		}
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < (len - str.length()); i++) {
			buf.append(padChar);
		}
		buf.append(str);
		return buf.toString();
	}

	/**
	 * 获取安全的HTML片段
	 * 
	 * @author mengbin
	 * @date 2014年1月8日 上午9:42:13
	 */
	public static String getCleanHTML(String html) throws Exception {
		if (StringUtil.isEmptyString(html)) {
			return html;
		}
		AntiSamy as = new AntiSamy();
		CleanResults cr = as.scan(html, policy);
		return cr.getCleanHTML();
	}

	/**
	 * 去除html片段中标签
	 * 
	 * @author mengbin
	 * @date 2014年1月14日 上午11:09:49
	 */
	public static String trimHtml(String html) {
		String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
		String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

		Pattern p_script = Pattern.compile(regEx_script,
				Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(html);
		html = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern
				.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(html);
		html = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(html);
		html = m_html.replaceAll(""); // 过滤html标签

		return html.trim(); // 返回文本字符串
	}

	/**
	 * 去除字符串中标签
	 * 
	 * @author fengjj
	 * @date 2014年1月14日 上午11:09:49
	 */
	public static String removeHtml(String html) {
		String regEx_left = "<[^>]*>"; // 定义HTML标签的正则表达式
		String regEx_centre = "\\&[a-zA-Z]{1,10}";
		String regEx_right = "[(/>)<]";

		Pattern p_centre = Pattern.compile(regEx_centre,
				Pattern.CASE_INSENSITIVE);
		Matcher m_centre = p_centre.matcher(html);
		html = m_centre.replaceAll(""); // 替换特殊字符

		Pattern p_left = Pattern.compile(regEx_left, Pattern.CASE_INSENSITIVE);
		Matcher m_left = p_left.matcher(html);
		html = m_left.replaceAll(""); // 替换左标签

		Pattern p_right = Pattern
				.compile(regEx_right, Pattern.CASE_INSENSITIVE);
		Matcher m_right = p_right.matcher(html);
		html = m_right.replaceAll(""); // 替换右标签

		return html.trim(); // 返回文本字符串
	}

	/**
	 * 链接数组返回SQL字符串
	 * 
	 * @author mengbin
	 * @date 2014-1-21 下午07:04:11
	 */
	public static String joinSql(String[] arr) {
		if (arr == null || arr.length < 1) {
			return null;
		}
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			buf.append((i < arr.length - 1) ? "'" + arr[i] + "'," : "'"
					+ arr[i] + "'");
		}
		return buf.toString();
	}

	/**
	 * 返回SQL字符串
	 * 
	 * @author mengbin
	 * @date Feb 22, 20143:33:41 PM
	 */
	public static String joinSql(long[] arr) {
		if (arr == null || arr.length < 1) {
			return null;
		}
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			buf.append((i < arr.length - 1) ? "'" + arr[i] + "'," : "'"
					+ arr[i] + "'");
		}
		return buf.toString();
	}

	/**
	 * 返回SQL字符串
	 * 
	 * @author mengbin
	 * @date Jan 25, 20145:38:27 PM
	 */
	public static String joinSql(Object[] objs) {
		if (objs == null || objs.length < 1) {
			return null;
		}
		String[] arr = new String[objs.length];
		for (int i = 0; i < objs.length; i++) {
			arr[i] = String.valueOf(objs[i]);
		}
		return joinSql(arr);
	}

	/**
	 * 转换成SQL查询语句
	 * 
	 * @author mengbin
	 * @date Jan 25, 20142:19:07 PM
	 */
	public static String joinSql(List<?> list) {
		if (list == null || list.size() < 1) {
			return null;
		}
		String[] arr = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = String.valueOf(list.get(i));
		}
		return joinSql(arr);
	}

	/**
	 * 判断字符串是否在字符串数组内
	 * 
	 * @author yizuchao
	 * @date March 24, 20142:19:07 PM
	 */

	public static boolean strIsInArray(String str, String[] strArray)
			throws Exception {
		if (str == null || strArray == null || strArray.length <= 0) {
			throw new Exception("the params is invalid");
		}
		for (String s : strArray) {
			if (s.equals(str))
				return true;
		}
		return false;
	}

	/*
	 * 由于Java是基于Unicode编码的，因此，一个汉字的长度为1，而不是2。
	 * 但有时需要以字节单位获得字符串的长度。例如，“123abc长城”按字节长度计算是10，而按Unicode计算长度是8。
	 * 为了获得10，需要从头扫描根据字符的Ascii来获得具体的长度
	 * 。如果是标准的字符，Ascii的范围是0至255，如果是汉字或其他全角字符，Ascii会大于255。
	 * 因此，可以编写如下的方法来获得以字节为单位的字符串长度。
	 */
	public static int getWordCount(String s) {
		int length = 0;
		for (int i = 0; i < s.length(); i++) {
			int ascii = Character.codePointAt(s, i);
			if (ascii >= 0 && ascii <= 255) {
				length++;
			} else {
				length += 2;
			}
		}
		return length;
	}

	/**
	 * 用特定字符填充字符串
	 * 
	 * @param sSrc 要填充的字符串
	 * @param ch 用于填充的特定字符
	 * @param nLen 要填充到的长度
	 * @param bLeft  要填充的方向：true:左边；false:右边
	 * @return 填充好的字符串
	 */
	public static String fill(String sSrc, char ch, int nLen, boolean bLeft) {
		if (sSrc == null || sSrc.equals("")) {
			StringBuffer sbRet = new StringBuffer();
			for (int i = 0; i < nLen; i++)
				sbRet.append(ch);

			return sbRet.toString();
		}
		byte[] bySrc = sSrc.getBytes();
		int nSrcLen = bySrc.length;
		if (nSrcLen >= nLen) {
			return sSrc;
		}
		byte[] byRet = new byte[nLen];
		if (bLeft) {
			for (int i = 0, n = nLen - nSrcLen; i < n; i++)
				byRet[i] = (byte) ch;
			for (int i = nLen - nSrcLen, n = nLen; i < n; i++)
				byRet[i] = bySrc[i - nLen + nSrcLen];
		} else {
			for (int i = 0, n = nSrcLen; i < n; i++)
				byRet[i] = bySrc[i];
			for (int i = nSrcLen, n = nLen; i < n; i++)
				byRet[i] = (byte) ch;
		}
		return new String(byRet);
	}

	/**
	 * 去字符串中的数字
	 * @param str
	 * @return
	 */
	public static String getNumber(String str) {
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		String result = m.replaceAll("").trim();
		return result;
	}

	public static String getTagVaule(String str , String tag){
		String result = "";
		String regEx = "<" + tag + "[^>]*?>[\\s\\S]*?<\\/" + tag +">";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if(m.find()){
			result = m.group().replace("<" + tag + ">", "").replace("</" + tag + ">", "");
		}
		return result;
	}
	
	/**
	 * 判断字符串是否有效
	 * @param str
	 * @return
	 */
	public static boolean effectiveStr(String str) {
		if(str == null || "".equals(str)){
			return false;
		}
		return true;
	}
	
	
}
