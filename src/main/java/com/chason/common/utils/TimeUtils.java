/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.chason.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间计算工具类

 */
public class TimeUtils {

	public static String toTimeString(long time) {
		TimeUtils t = new TimeUtils(time);
		int day = t.get(TimeUtils.DAY);
		int hour = t.get(TimeUtils.HOUR);
		int minute = t.get(TimeUtils.MINUTE);
		int second = t.get(TimeUtils.SECOND);
		StringBuilder sb = new StringBuilder();
		if (day > 0){
			sb.append(day).append("天");
		}
		if (hour > 0){
			sb.append(hour).append("时");
		}
		if (minute > 0){
			sb.append(minute).append("分");
		}
		if (second > 0){
			sb.append(second).append("秒");
		}
		return sb.toString();
	}

    /**
     * 时间字段常量，表示“秒”
     */
    public final static int SECOND = 0;

    /**
     * 时间字段常量，表示“分”
     */
    public final static int MINUTE = 1;

    /**
     * 时间字段常量，表示“时”
     */
    public final static int HOUR = 2;

    /**
     * 时间字段常量，表示“天”
     */
    public final static int DAY = 3;

    /**
     * 各常量允许的最大值
     */
    private final int[] maxFields = { 59, 59, 23, Integer.MAX_VALUE - 1 };

    /**
     * 各常量允许的最小值
     */
    private final int[] minFields = { 0, 0, 0, Integer.MIN_VALUE };

    /**
     * 默认的字符串格式时间分隔符
     */
    private String timeSeparator = ":";

    /**
     * 时间数据容器
     */
    private int[] fields = new int[4];

    /**
     * 无参构造，将各字段置为 0
     */
    public TimeUtils() {
        this(0, 0, 0, 0);
    }

    /**
     * 使用时、分构造一个时间
     * @param hour      小时
     * @param minute    分钟
     */
    public TimeUtils(int hour, int minute) {
        this(0, hour, minute, 0);
    }

    /**
     * 使用时、分、秒构造一个时间
     * @param hour      小时
     * @param minute    分钟
     * @param second    秒
     */
    public TimeUtils(int hour, int minute, int second) {
        this(0, hour, minute, second);
    }

    /**
     * 使用一个字符串构造时间<br/>
     * Time time = new Time("14:22:23");
     * @param time      字符串格式的时间，默认采用“:”作为分隔符
     */
    public TimeUtils(String time) {
        this(time, null);
    }

    /**
     * 使用时间毫秒构建时间
     * @param time
     */
    public TimeUtils(long time){
    	this(new Date(time));
    }

    /**
     * 使用日期对象构造时间
     * @param date
     */
    public TimeUtils(Date date){
    	this(DateFormatUtils.formatUTC(date, "HH:mm:ss"));
    }

    /**
     * 使用天、时、分、秒构造时间，进行全字符的构造
     * @param day       天
     * @param hour      时
     * @param minute    分
     * @param second    秒
     */
    public TimeUtils(int day, int hour, int minute, int second) {
        initialize(day, hour, minute, second);
    }

    /**
     * 使用一个字符串构造时间，指定分隔符<br/>
     * Time time = new Time("14-22-23", "-");
     * @param time      字符串格式的时间
     */
    public TimeUtils(String time, String timeSeparator) {
        if(timeSeparator != null) {
            setTimeSeparator(timeSeparator);
        }
        parseTime(time);
    }

    /**
     * 设置时间字段的值
     * @param field     时间字段常量
     * @param value     时间字段的值
     */
    public void set(int field, int value) {
        if(value < minFields[field]) {
            throw new IllegalArgumentException(value + ", time value must be positive.");
        }
        fields[field] = value % (maxFields[field] + 1);
        // 进行进位计算
        int carry = value / (maxFields[field] + 1);
        if(carry > 0) {
            int upFieldValue = get(field + 1);
            set(field + 1, upFieldValue + carry);
        }
    }

    /**
     * 获得时间字段的值
     * @param field     时间字段常量
     * @return          该时间字段的值
     */
    public int get(int field) {
        if(field < 0 || field > fields.length - 1) {
            throw new IllegalArgumentException(field + ", field value is error.");
        }
        return fields[field];
    }

    /**
     * 将时间进行“加”运算，即加上一个时间
     * @param time      需要加的时间
     * @return          运算后的时间
     */
    public TimeUtils addTime(TimeUtils time) {
    	TimeUtils result = new TimeUtils();
        int up = 0;     // 进位标志
        for (int i = 0; i < fields.length; i++) {
            int sum = fields[i] + time.fields[i] + up;
            up = sum / (maxFields[i] + 1);
            result.fields[i] = sum % (maxFields[i] + 1);
        }
        return result;
    }

    /**
     * 将时间进行“减”运算，即减去一个时间
     * @param time      需要减的时间
     * @return          运算后的时间
     */
    public TimeUtils subtractTime(TimeUtils time) {
    	TimeUtils result = new TimeUtils();
        int down = 0;       // 退位标志
        for (int i = 0, k = fields.length - 1; i < k; i++) {
            int difference = fields[i] + down;
            if (difference >= time.fields[i]) {
                difference -= time.fields[i];
                down = 0;
            } else {
                difference += maxFields[i] + 1 - time.fields[i];
                down = -1;
            }
            result.fields[i] = difference;
        }
        result.fields[DAY] = fields[DAY] - time.fields[DAY] + down;
        return result;
    }

    /**
     * 获得时间字段的分隔符
     * @return
     */
    public String getTimeSeparator() {
        return timeSeparator;
    }

    /**
     * 设置时间字段的分隔符（用于字符串格式的时间）
     * @param timeSeparator     分隔符字符串
     */
    public void setTimeSeparator(String timeSeparator) {
        this.timeSeparator = timeSeparator;
    }

    private void initialize(int day, int hour, int minute, int second) {
        set(DAY, day);
        set(HOUR, hour);
        set(MINUTE, minute);
        set(SECOND, second);
    }

    private void parseTime(String time) {
        if(time == null) {
            initialize(0, 0, 0, 0);
            return;
        }
        String t = time;
        int field = DAY;
        set(field--, 0);
        int p = -1;
        while((p = t.indexOf(timeSeparator)) > -1) {
            parseTimeField(time, t.substring(0, p), field--);
            t = t.substring(p + timeSeparator.length());
        }
        parseTimeField(time, t, field--);
    }

    private void parseTimeField(String time, String t, int field) {
        if(field < SECOND || t.length() < 1) {
            parseTimeException(time);
        }
        char[] chs = t.toCharArray();
        int n = 0;
        for(int i = 0; i < chs.length; i++) {
            if(chs[i] <= ' ') {
                continue;
            }
            if(chs[i] >= '0' && chs[i] <= '9') {
                n = n * 10 + chs[i] - '0';
                continue;
            }
            parseTimeException(time);
        }
        set(field, n);
    }

    private void parseTimeException(String time) {
        throw new IllegalArgumentException(time + ", time format error, HH"
                + this.timeSeparator + "mm" + this.timeSeparator + "ss");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(16);
        sb.append(fields[DAY]).append(',').append(' ');
        buildString(sb, HOUR).append(timeSeparator);
        buildString(sb, MINUTE).append(timeSeparator);
        buildString(sb, SECOND);
        return sb.toString();
    }

    private StringBuilder buildString(StringBuilder sb, int field) {
        if(fields[field] < 10) {
            sb.append('0');
        }
        return sb.append(fields[field]);
    }

    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + Arrays.hashCode(fields);
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final TimeUtils other = (TimeUtils) obj;
        if (!Arrays.equals(fields, other.fields)) {
            return false;
        }
        return true;
    }

    /**
     * 格式化的时间表达方式
     * @param date 需要格式化的时间
     * @param format 用户指定格式
     *
     * @return 格式后的字符串
     * */
    public static String getTimeUserFormat(java.util.Date date, String format)
    {
        try
        {
            if (date != null)
            {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                return dateFormat.format(date);
            }
        }
        catch (Exception e)
        {
            System.out.println("error in TimeUtils.getDateFormat()");
        }
        return "";
    }

    /**
     * 从字符串时间翻译得到sql所用的时间
     *
     * @param strDate The string of date will be converted to sql.date
     *
     * @return 备注信息
     * */
    public static java.util.Date getFormatDate(String strDate, String format)
    {
        try
        {
            if (StringUtils.isNotNull(strDate))
            {
                SimpleDateFormat bartDateFormat = new SimpleDateFormat(format);
                return bartDateFormat.parse(strDate);
            }
        }
        catch (Exception e)
        {
            System.out.println("error in TimeUtils.getSqlDate()");
        }
        return null;
    }

    /**
     * 判断当前日期是否在区间中（包括该区间）.
     *
     * @param startDate 区间的前部
     * @param endDate   区间的后部
     * */
    public static boolean isBetweenDate(java.util.Date startDate,
            java.util.Date endDate)
    {
        Calendar startTime = Calendar.getInstance();
        startTime.setTime(startDate);
        //startTime.add(Calendar.DAY_OF_MONTH, -1); //一天前的日期

        Calendar endTime = Calendar.getInstance();
        endTime.setTime(endDate);
        //endTime.add(Calendar.DAY_OF_MONTH, 1); //一天后的日期

        Calendar now = Calendar.getInstance();
        if (startTime.before(now) && endTime.after(now))
        {
            return true;
        }
        return false;
    }

    /**
     * 判断传入的日期是否在区间中（包括该区间）.
     * @param date 传入的时间
     * @param startDate 区间的前部
     * @param endDate   区间的后部
     * */
    public static boolean isBetweenDate(java.util.Date date,
            java.util.Date startDate, java.util.Date endDate)
    {
        Calendar startTime = Calendar.getInstance();

        startTime.setTime(startDate);
        startTime.add(Calendar.DAY_OF_MONTH, -1);

        Calendar endTime = Calendar.getInstance();
        endTime.setTime(endDate);
        endTime.add(Calendar.DAY_OF_MONTH, 1);

        Calendar now = Calendar.getInstance();
        now.setTime(date);

        if (startTime.before(now) && endTime.after(now))
        {
            return true;
        }
        return false;
    }

    /**
     * 合并时间
     * @param date 基础时间纪录年月日
     * @param time 时间纪录 小时分秒 毫秒 类似11:13:45
     * */
    public static java.util.Date combineDateTime(java.util.Date date, String time)
    {
        if (time == null)
        {
            return date;
        }

        try
        {
            String strTimeEnd = "";
            if (time.length() > 24)
            {
                strTimeEnd = time.substring(time.indexOf("T"), time
                        .length());
            }
            else if (time.length() == 5)
            {
                strTimeEnd = "T" + time + ":00+00:00";
            }
            else if (time.length() == 8)
            {
                strTimeEnd = "T" + time + "+00:00";
            }

            SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDateFormat = bartDateFormat.format(date);

            String strTime = strDateFormat + strTimeEnd;

            SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+'SS:SS");
            return myFmt.parse(strTime);
        }
        catch (Exception pe)
        {
            pe.printStackTrace();
            System.out.println("时间解析错误：" + time);
        }
        return date;
    }

    /**
     * 获取两个日期相隔的分钟  dataEd - dataSt
     * @param dataEd 被减的日期
     * @param dataSt 减的日期
     * @return int 相隔天数
     */
    public static int getMinuteBetween(Date dataSt, Date dataEd)
    {
        java.util.Calendar calst = java.util.Calendar.getInstance();
        java.util.Calendar caled = java.util.Calendar.getInstance();
        calst.setTime(dataSt);
        caled.setTime(dataEd);

        //得到两个日期相差的天数
        int intVal = ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 60;
        return intVal;
    }
}
