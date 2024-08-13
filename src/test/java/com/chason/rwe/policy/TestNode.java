package com.chason.rwe.policy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.chason.RweApplication;
import com.chason.rwe.domain.SpaceDO;
import com.chason.rwe.service.DeviceService;
import com.chason.rwe.service.SpaceService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= RweApplication.class)
public class TestNode
{
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SpaceService spaceService;
    /**
     */
    @Test
    public void testNode() throws Exception
    {
        String spaceKeys = "f59665b5c0a805346f17d9e4ceb64468,f596be10c0a805343a08d98f0f998e99,-1,e26f4451c0a805347cdb8819b3e51f3a,e27f7e17c0a8053404c0c0d2d69f6849,";
        List<String> keys = new ArrayList<String>();
        String[] split = spaceKeys.split(",");
        for (String string : split)
        {
            if(string != "" && string != null)
            {
                keys.add(string);
            }
        }
        System.err.println("***keys:" + keys.size());
        List<String> terminalNodes = terminalNodes(keys);

        for (String string : terminalNodes)
        {
            System.out.println("***末梢节点:" + string);
        }
    }

    private List<String> terminalNodes(List<String> keys)
    {
        HashSet<String> removeSet = new HashSet<>();

        for (String strKey : keys)
        {
            SpaceDO space = spaceService.get(strKey);
            if("-1".equals(strKey))
            {
                continue;
            }
            if(keys.contains(space.getSpaceParentId())) {
                removeSet.add(space.getSpaceParentId());
            }
        }
        List<String> tmp = remove(keys, removeSet);
        return tmp;
    }

    private List<String> remove(List<String> oldlist, HashSet<String> removeSet){
        List<String> newList = new ArrayList<>();
        for (String str : oldlist)
        {
            if(removeSet.contains(str))
            {
                continue;
            }
            newList.add(str);
        }
        return newList;
    }

}
