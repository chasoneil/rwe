package com.chason.rwe.policy;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.chason.common.utils.MathUtils;
import com.chason.common.utils.StringUtils;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestHEX
{

    /**
     */
    @Test
    public void testStrTo16() throws Exception
    {
        String strTime = "190405103440";

        if (strTime.length()%2 != 0)
        {
            Assert.assertFalse(false);
        }

        System.out.println("###str1:" + strTime);

        String[] arrTime = StringUtils.stringSpilt(strTime, 2);
        System.out.println("###length:" + arrTime.length);
        String strTime2 = "";
        for (String str : arrTime)
        {
            System.out.println("### str:" + str);
            str = MathUtils.intToHex(Integer.parseInt(str));
            strTime2 += StringUtils.formatByLength_LeftFill(str, 2, "0");
        }

        Assert.assertEquals("1304050A2228", strTime2);
    }




}
