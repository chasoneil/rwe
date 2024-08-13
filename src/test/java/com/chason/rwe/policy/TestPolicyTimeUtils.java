package com.chason.rwe.policy;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.chason.RweApplication;
import com.chason.rwe.domain.PolicyDO;
import com.chason.rwe.domain.SpaceDO;
import com.chason.rwe.service.PolicyService;
import com.chason.rwe.service.SpaceService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= RweApplication.class)
public class TestPolicyTimeUtils
{
    @Autowired
    private SpaceService spaceService;

    @Autowired
    private PolicyService policyService;

    /**
     */
    @Test
    public void setFindPolicy() throws Exception
    {
        List<PolicyDO> policys = this.policyService.list(new HashMap<String, Object>());
        List<SpaceDO> spaces = this.spaceService.list(new HashMap<String, Object>());
        for (SpaceDO theSpace : spaces)
        {
            /*System.out.println("###########space:" + theSpace.getSpaceCode());
            List<String> times =  PolicyTimeUtils.findPolicy(policys, theSpace, new DeviceDO);
            for (String theTime : times)
            {
                System.out.println("-------->time:" + theTime);
            }*/
        }

        System.out.println("....success");
    }
}
