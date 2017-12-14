package com.seeu.system.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import com.seeu.system.Constants;

/**
 * 日期操作工具类
 *
 */
public class DateUtil
{
	public static final int SKIP_TYPE_SECOND = 1;//秒
	
	public static final int SKIP_TYPE_MINUTE = 2;//分
	
	public static final int SKIP_TYPE_HOUR = 3;//小时
	
	public static final int SKIP_TYPE_DAY = 4;//天
	
	/**
	 * 获取时间差
	 * @param date1
	 * @param date2
	 * @param skipType
	 * @return 返回时间差,可以返回负数
	 */
	public static int getSkip(Date date1,Date date2,int skipType)throws Exception
	{
		if(date1 == null || date2 == null || skipType < 1 || skipType > 4)
		{
			throw new Exception("参数传递出错！");
		}
		long diff = date1.getTime() - date2.getTime();
		int flag = 1;
		if(diff < 0)
		{
			flag = -1;
		}
		int result = 0;
		switch(skipType)
		{
		case SKIP_TYPE_SECOND:result = (int)diff / 1000;break;
		case SKIP_TYPE_MINUTE:result = (int)diff / 60000;break;
		case SKIP_TYPE_HOUR:result = (int)diff / 3600000;break;
		case SKIP_TYPE_DAY:result = (int)diff / 43200000;break;
		}
		return flag * result;
	}
	
	public static Date parseDate(String dateStr)throws Exception
	{
		return parse(dateStr, Constants.DATE_MASK);
	}
	
	public static Date parse(String dateStr,String format)throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(dateStr);
	}
	
	public static String format(Date date,String format)throws Exception
	{
		return new SimpleDateFormat(format).format(date);
	}
	
	/**
	 * 转换日期到字符串,如果带时分秒则显示时分秒,如果不带则不显示时分秒
	 */
	public static String formatDate2String(Date date)
	{
		String str = null;
		try
		{
			str = DateUtil.format(date, Constants.DATETIME_MASK);
			int pos = str.indexOf(" 00:00:00");
			if(pos >= 0)
			{
				str = str.substring(0,pos);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * 根据字符串获得Calendar对象
	 */
	public static Calendar getCalendarByString(String timeString,String formatString) throws Exception{
		Calendar c=new GregorianCalendar();
		DateFormat format=new SimpleDateFormat(formatString);
		Date d=format.parse(timeString);
		c.setTime(d);
		return c;
	}
	
	public static Date formateDate(Date date,String dateType) throws ParseException{
		SimpleDateFormat sd=new SimpleDateFormat(dateType);
		return sd.parse(sd.format(date));
	}
	
    /** 
     * 判断是否是周末 
     * @return 
     */  
    public static boolean isWeekend(Date date){  
    	Calendar  cal   =   Calendar.getInstance();   
    	cal.setTime(date);
        int week=cal.get(Calendar.DAY_OF_WEEK)-1;  
        if(week ==6 || week==0){//0代表周日，6代表周六  
            return true;  
        }  
        return false;  
    }  
    
    /**
     * 
    * @Description: 两个日期间隔天数
    *  @param startDate
    *  @param endDate
    *  @return
     */
    public static Long getDaysBetween(Date startDate, Date endDate) {  
            Calendar fromCalendar = Calendar.getInstance();  
            fromCalendar.setTime(startDate);  
            fromCalendar.set(Calendar.HOUR_OF_DAY, 0);  
            fromCalendar.set(Calendar.MINUTE, 0);  
            fromCalendar.set(Calendar.SECOND, 0);  
            fromCalendar.set(Calendar.MILLISECOND, 0);  
      
            Calendar toCalendar = Calendar.getInstance();  
            toCalendar.setTime(endDate);  
            toCalendar.set(Calendar.HOUR_OF_DAY, 0);  
            toCalendar.set(Calendar.MINUTE, 0);  
            toCalendar.set(Calendar.SECOND, 0);  
            toCalendar.set(Calendar.MILLISECOND, 0);  
      
            return (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24)+1;  
        }  

	/**
	 * 获取两个日期之间的周末的天数
	 * @param startDate
	 * @param endDate
	 * @return 间隔的天数
	 */
	public static int getDutyDays(Date startDate, Date endDate)
			throws Exception {
		int result = 0;
		startDate = parse(formatDate2String(startDate), "yyyy-MM-dd");
		endDate = parse(formatDate2String(endDate), "yyyy-MM-dd");
		while (startDate.compareTo(endDate) <= 0) {
			if (isWeekend(startDate)) {
				result++;
			}
			startDate = addDay(startDate,1); // 这个时间就是日期往后推一天的结果
		}
		return result;
	}
	
	/**
	 * 获取两个日期之间的工作日的天数
	 * @param startDate
	 * @param endDate
	 * @return 工作日的天数
	 */
	public static int getDutyWeekDays(Date startDate, Date endDate)
			throws Exception {
		int result = 0;
		startDate = parse(formatDate2String(startDate), "yyyy-MM-dd");
		endDate = parse(formatDate2String(endDate), "yyyy-MM-dd");
		Date temp = startDate;
		while (temp.compareTo(endDate) <= 0) {
			if (!isWeekend(temp)) {
				result++;
			}
			temp = addDay(temp,1); // 这个时间就是日期往后推一天的结果
		}
		if(!isWeekend(startDate)){
			result--;
		}
		if(!isWeekend(endDate)){
			result--;
		}
		return result;
	}
	
	/**
	 * 功能描述：返回小时
	 * @param date 日期
	 * @return 返回小时
	 */
	public static int getHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		if(date != null)
	      calendar.setTime(date);
	    return calendar.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 功能描述：返回分钟
	 * @param date 日期
	 * @return 返回分钟
	 */
	public static int getMinute(Date date) {
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    return calendar.get(Calendar.MINUTE);
	}
	
	/**
	 * 功能描述：返回今年第几个星期
	 * @param date 日期
	 * @return 返回分钟
	 */
	public static int getWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    return calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	/**
	 * 功能描述：日期往后移num天
	 * @param date 日期
	 * @return 返回日期
	 */
	public static Date addDay(Date date,int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, num);// 把日期往后增加一天.整数往后推,负数往前移动
		return calendar.getTime(); // 这个时间就是日期往后推一天的结果
	}
	/**
	* @Title: calculAges 
	* @Description: 计算周年
	*  @param birthday
	*  @param nowDate
	*  @return
	 */
	public static int calculAnniversary(Date birthday , Date nowDate){
		int ages = 0;
		Calendar  nowCal   =   Calendar.getInstance();   
		nowCal.setTime(nowDate);
		int nowYear = nowCal.get(Calendar.YEAR);
		int nowMonth = nowCal.get(Calendar.MONTH)+1;
		int nowDay = nowCal.get(Calendar.DAY_OF_MONTH);
		Calendar  birthCal   =   Calendar.getInstance();   
		birthCal.setTime(birthday);
		int birthYear =  birthCal.get(Calendar.YEAR);
		int birthMonth = birthCal.get(Calendar.MONTH)+1;
		int birthDay = birthCal.get(Calendar.DAY_OF_MONTH);
		ages = nowYear - birthYear;
		if(nowMonth<birthMonth){
			ages --;
		}else if(nowMonth==birthMonth){
			if(nowDay<birthDay){
				ages --;
			}
		}
		return ages;
	} 
	
	/**
	 * 获取当前时间
	 * @param dataStr 时间格式字符串
	 * @return
	 */
	public static String getNowDate(String dataStr) {
		Date dt = new Date();
		DateFormat df = new SimpleDateFormat(dataStr);
		return df.format(dt);
	}
	/**
	 * 根据年月日时分秒(yyyy-MM-dd HH:mm:ss)的Date类型，返回这个年月日(零点：如 yyyy-MM-dd)的时间戳，
	 * @author zho
	 * @param timedate
	 * @return 这个年月日(yyyy-MM-dd)的时间戳
	 */
	public static long getZeroTimeFromDate(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(date);
		long time = 0;
		try {
			time = sdf.parse(str).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}
	
}
