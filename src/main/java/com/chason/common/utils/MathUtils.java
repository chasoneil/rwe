package com.chason.common.utils;

import java.text.*;
import java.math.BigDecimal;

public class MathUtils
{
    private static final int DEF_DIV_SCALE = 10;

    /**
     * 提供精确的平方和运算。
     *
     * @param y
     *            乘数
     * @return 平方和
     */
    public static double square(double y)
    {
        return mul(y, y);
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1
     *            被加数
     * @param v2
     *            加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1
     *            被减数
     * @param v2
     *            减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1
     *            被乘数
     * @param v2
     *            乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1
     *            被除数
     * @param v2
     *            除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2)
    {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1
     *            被除数
     * @param v2
     *            除数
     * @param scale
     *            表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v
     *            需要四舍五入的数字
     * @param scale
     *            小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        try
        {
            BigDecimal b = new BigDecimal(Double.toString(v));
            BigDecimal one = new BigDecimal("1");
            return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        catch (java.lang.NumberFormatException ex)
        {
            System.out.println("@@@@@@@@@@@@@@@:" + v);
        }
        return 0;
    }

    /**
     * 提供把字符串转换为int型数值。 如果字符串为空，返回0
     *
     * @param s
     *            需要转换的字符串
     * @return int型数值
     */
    public static int getIntFromString(String s)
    {
        if (s != null && !s.trim().equals(""))
        {
            return Integer.parseInt(s);
        }
        return 0;
    }

    /**
     * 取结算分
     * @param v 金额（带分）
     * @return 结算分
     * */
    public static double balanceCost(double v)
    {
        long lngTmp = Math.round(v * 100) % 10;
        return round(lngTmp / 100.0, 2);
    }

    /**
     * 返回增幅%
     * @param v1
     *         被除数
     * @param v2
     *         除数
     * @return
     */
    public static double getRiseRate(double v1, double v2, int scale)
    {
        double result = 0;
        if (v2 != 0)
        {
            result = round(v1*100/v2 - 100, scale);
        }
        return result;
    }

    /**
     * 格式化数字.
     * @param cost 需要格式化的数字
     * @param pattern 指定的格式
     *
     * <p>0.00   指定数字的小数后为两位
     * <p>##.##% 将数字转化为百分比输出
     * */
    public static String patternCost(double cost, String pattern)
    {
        DecimalFormat df2=(DecimalFormat) DecimalFormat.getInstance();
        df2.applyPattern(pattern);
        return df2.format(cost);
    }


    /**
     * 格式化double数值，采用逗号分割。
     *
     * @param dblTemp
     *            需要格式化的浮点数。
     * @return 逗号分割的数值字符串
     */
    public static String getDoubleNoGroupingUsed(Double dblTemp)
    {
        NumberFormat theNF = NumberFormat.getIntegerInstance();
        theNF.setGroupingUsed(false);
        return theNF.format(dblTemp);
    }

    /**
     * 格式化数字，逗号分割。
     * */
    public static String formatGroupingUsed(double theNumber)
    {
        NumberFormat currFmt = NumberFormat.getNumberInstance();
        try
        {
            currFmt.setGroupingUsed(true); //用逗号分割数字
            currFmt.setMaximumFractionDigits(2);
            currFmt.setMinimumFractionDigits(2);
            theNumber = Math.round(theNumber * 100) / 100.0;
        }
        catch (Exception ex)
        {
            System.out.println("This error in NumberFormatByUser.format(): "+ex);
        }
        return currFmt.format(theNumber);
    }

    /**
     * 格式化数字，逗号分割。
     * */
    public static String formatNoGroupingUsed(double theNumber)
    {
        NumberFormat currFmt = NumberFormat.getNumberInstance();
        try
        {
            currFmt.setGroupingUsed(false); //用逗号分割数字
            currFmt.setMaximumFractionDigits(2);
            currFmt.setMinimumFractionDigits(2);
            theNumber = Math.round(theNumber * 100) / 100.0;
        }
        catch (Exception ex)
        {
            System.out.println("This error in NumberFormatByUser.format(): "+ex);
        }
        return currFmt.format(theNumber);
    }

    /**
     * 格式化数字，逗号分割。
     * */
    public static String formatGroupingUsed(long theNumber)
    {
        NumberFormat currFmt = NumberFormat.getNumberInstance();
        try
        {
            currFmt.setGroupingUsed(true); //用逗号分割数字
        }
        catch (Exception ex)
        {
            System.out.println("This error in NumberFormatByUser.format(): "+ex);
        }
        return currFmt.format(theNumber);
    }

    /**
     * 格式化数字，逗号分割。
     * */
    public static String formatGroupingUsed(int theNumber)
    {
        NumberFormat currFmt = NumberFormat.getNumberInstance();
        try
        {
            currFmt.setGroupingUsed(true); //用逗号分割数字
        }
        catch (Exception ex)
        {
            System.out.println("This error in NumberFormatByUser.format(): "+ex);
        }
        return currFmt.format(theNumber);
    }

    /**
     * 10进制，转16进制字符串
     * */
    public static String intToHex(int n)
    {
        // StringBuffer s = new StringBuffer();
        StringBuilder sb = new StringBuilder(8);
        String a;
        char[] b = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',
                'C', 'D', 'E', 'F' };
        while (n != 0)
        {
            sb = sb.append(b[n % 16]);
            n = n / 16;
        }
        a = sb.reverse().toString();
        return a;
    }
};
