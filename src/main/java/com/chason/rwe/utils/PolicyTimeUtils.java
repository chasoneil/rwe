package com.chason.rwe.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.chason.common.utils.StringUtils;
import com.chason.common.utils.TimeUtils;
import com.chason.rwe.domain.DeviceDO;
import com.chason.rwe.domain.PolicyDO;
import com.chason.rwe.domain.SpaceDO;
import com.chason.rwe.service.DeviceService;

public class PolicyTimeUtils
{
//    /**
//     * @param dictService 数据库访问句柄
//     * @return 当前时间所处的classtime区间
//     * */
//    public static DictDO locClassTime(DictService dictService)
//    {
//        List<DictDO> dicts = dictService.listByType("class_time");
//        for (DictDO theDict : dicts)
//        {
//            Calendar cal = Calendar.getInstance();
//            java.util.Date startDate = TimeUtils.combineDateTime(cal.getTime(), theDict.getRemarks()+ ":00");
//            cal.setTime(startDate);
//            cal.add(Calendar.MINUTE, 40);
//            java.util.Date endDate = cal.getTime();
//
//            //如果当前时间在此区间
//            if (TimeUtils.isBetweenDate(startDate, endDate))
//            {
//                return theDict;
//            }
//        }
//        return null;
//    }

    /**
     * 设备对应的当天应用的所有策略 ,周日为1，周六为7
     * 如果有跨天，自动设置时间
     * */
    public static List<String> findPolicy(List<PolicyDO> policys, SpaceDO theSpace, DeviceDO theDevice, DeviceService deviceService)
    {
        List<String> policyTimes = new ArrayList<String>();

        Calendar cal_today = Calendar.getInstance(); //当日
        int intWeekYesToday = cal_today.get(Calendar.DAY_OF_WEEK);

        Calendar cal_yestoday = Calendar.getInstance();
        cal_yestoday.add(Calendar.DAY_OF_MONTH, -1); //昨日

        for (PolicyDO thePolicy : policys)
        {
//            System.out.println("##policy:" + thePolicy.getPolicyWeek());
            //检查策略是否与空间相对应
            if (thePolicy.getPolicySpaceKeys().indexOf(theSpace.getSpaceId()) == -1)
            {
                continue;
            }

            String strPolicy = "";
            if (thePolicy.getPolicyWeek().indexOf(Integer.toString(intWeekYesToday)) == -1)
            {
                strPolicy = getYestodayPolicy(thePolicy, cal_yestoday); //昨日策略
            }
            else
            {
                strPolicy = getTodayPolicy(thePolicy, cal_today); //今日策略
            }

            if("随机策略".equals(thePolicy.getPolicyType()))
            {
                String random = theDevice.getDevRandom();
                if(random != null && random != "")
                {
                    if(random.indexOf(thePolicy.getPolicyId()) == -1)
                    {
                        random += "," + thePolicy.getPolicyId();
                    }
                }
                else
                {
                    random = thePolicy.getPolicyId();
                }
                theDevice.setDevRandom(random);
                deviceService.update(theDevice);
            }

            if (StringUtils.isNotNull(strPolicy))
            {
//                System.out.println("### strPolicy:" + strPolicy);
                policyTimes.add(strPolicy);
            }
        }

        return policyTimes;
    }

    /**
     * 今日正常策略
     * */
    private static String getTodayPolicy(PolicyDO thePolicy, Calendar cal)
    {
        int intWeekToday = cal.get(Calendar.DAY_OF_WEEK);
        if (thePolicy.getPolicyWeek().indexOf(Integer.toString(intWeekToday)) == -1)
        {
            return "";
        }

        String[] arrTmp = thePolicy.getPolicyStartTime().split(":");
        if (arrTmp.length < 2)
        {
            return "";
        }
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(arrTmp[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(arrTmp[1]));

        String strStartTime  = TimeUtils.getTimeUserFormat(cal.getTime(), "HH:mm");

        cal.add(Calendar.MINUTE, Integer.parseInt(thePolicy.getPolicyDur()));
        String strFinishTime = TimeUtils.getTimeUserFormat(cal.getTime(), "HH:mm");

        if (cal.get(Calendar.DAY_OF_WEEK) != intWeekToday) //如果已经跨过当日，则截至至23:59
        {
            strFinishTime = "23:59";
        }

        return strStartTime + "~" + strFinishTime;
    }

    /**
     * 昨日延续策略
     * */
    private static String getYestodayPolicy(PolicyDO thePolicy, Calendar cal)
    {
        int intWeekYestoday = cal.get(Calendar.DAY_OF_WEEK);
        if (thePolicy.getPolicyWeek().indexOf(Integer.toString(intWeekYestoday)) == -1)
        {
            return "";
        }

        String[] arrTmp = thePolicy.getPolicyStartTime().split(":");
        if (arrTmp.length < 2)
        {
            return "";
        }
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(arrTmp[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(arrTmp[1]));

        String strStartTime  = TimeUtils.getTimeUserFormat(cal.getTime(), "HH:mm");

        cal.add(Calendar.MINUTE, Integer.parseInt(thePolicy.getPolicyDur()));
        String strFinishTime = TimeUtils.getTimeUserFormat(cal.getTime(), "HH:mm");

        if (cal.get(Calendar.DAY_OF_WEEK) != intWeekYestoday) //如果已经跨过当日，则截至至23:59
        {
            strStartTime = "00:01";
        }
        else
        {
            return "";
        }

        return strStartTime + "~" + strFinishTime;
    }

    /**
     * 当前时间在策略期
     * */
    public static boolean isPolicyOpen(List<String> arrPolicy)
    {
        if (arrPolicy == null || arrPolicy.size() == 0)
            return false;

        for(String strPolicy : arrPolicy)
        {
            String[] arrTmp = strPolicy.split("~");
            if (arrTmp.length != 2)
                continue;

            Calendar cal = Calendar.getInstance();
            java.util.Date startTime  = TimeUtils.combineDateTime(cal.getTime(), arrTmp[0]);
            java.util.Date finishTime = TimeUtils.combineDateTime(cal.getTime(), arrTmp[1]);
            if (TimeUtils.isBetweenDate(startTime, finishTime))
            {
                return true;
            }

        }
        return false;
    }
}
