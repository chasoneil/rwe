package com.chason.rwe.policy;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestTerminal
{
    /**
     */
    @Test
    public void setFindPolicy() throws Exception
    {
        List<Integer> number = new ArrayList<>();
        number.add(0);
        number.add(1);
        number.add(2);
        number.add(3);
        number.add(4);

        while(true) {
            Integer i = number.get((int)(Math.random() * number.size()));
            System.out.println("***随机数:" + i);
            Thread.sleep(1000);
        }


    }

}
