package com.chason.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chason.common.dao.DictDao;
import com.chason.common.domain.DictDO;
import com.chason.common.domain.Tree;
import com.chason.common.service.DictService;
import com.chason.common.utils.BuildTree;
import com.chason.common.utils.StringUtils;
import com.chason.system.domain.UserDO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class DictServiceImpl implements DictService {
    @Autowired
    private DictDao dictDao;

    @Override
    public DictDO get(Long id) {
        return dictDao.get(id);
    }

    @Override
    public List<DictDO> list(Map<String, Object> map) {
        return dictDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return dictDao.count(map);
    }

    @Override
    public int save(DictDO sysDict) {
        return dictDao.save(sysDict);
    }

    @Override
    public int update(DictDO sysDict) {
        return dictDao.update(sysDict);
    }

    @Override
    public int remove(Long id) {
        return dictDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return dictDao.batchRemove(ids);
    }

    @Override

    public List<DictDO> listType() {
        return dictDao.listType();
    }

    @Override
    public String getName(String type, String value) {
        Map<String, Object> param = new HashMap<String, Object>(16);
        param.put("type", type);
        param.put("value", value);
        String rString = dictDao.list(param).get(0).getName();
        return rString;
    }

    @Override
    public DictDO getDictDoByTypeAndValue(String type,String value)
    {
        Map<String, Object> param = new HashMap<String, Object>(16);
        param.put("type", type);
        param.put("value", value);
        return dictDao.list(param).get(0);
    }

    @Override
    public DictDO getDictDoByTypeAndName(String type,String name)
    {
        Map<String, Object> param = new HashMap<String, Object>(16);
        param.put("type", type);
        param.put("name", name);
        return dictDao.list(param).get(0);
    }

    @Override
    public List<DictDO> getHobbyList(UserDO userDO) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("type", "hobby");
        List<DictDO> hobbyList = dictDao.list(param);

        if (StringUtils.isNotEmpty(userDO.getHobby())) {
            String userHobbys[] = userDO.getHobby().split(";");
            for (String userHobby : userHobbys) {
                for (DictDO hobby : hobbyList) {
                    if (!Objects.equals(userHobby, hobby.getId().toString())) {
                        continue;
                    }
                    hobby.setRemarks("true");
                    break;
                }
            }
        }

        return hobbyList;
    }

    @Override
    public List<DictDO> getSexList() {
        Map<String, Object> param = new HashMap<>(16);
        param.put("type", "sex");
        return dictDao.list(param);
    }

    @Override
    public List<DictDO> listByType(String type) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("type", type);
        return dictDao.list(param);
    }

    /**
     * 得到班级的树形结构
     * 手工增加的方法
     * */
    @Override
    public Tree<DictDO> getTree(String type)
    {
        List<Tree<DictDO>> trees = new ArrayList<Tree<DictDO>>();
        List<DictDO> dicts = listByType(type);
        for (DictDO theDict : dicts)
        {
            Tree<DictDO> tree = new Tree<DictDO>();
            tree.setId      (theDict.getValue());
            tree.setParentId(null);
            tree.setText    (theDict.getName());

            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<DictDO> t = BuildTree.build(trees);
        return t;
    }

    /**
     * 得到班级的树形结构
     * 手工增加的方法
     * */
    @Override
    public Tree<DictDO> getDeviceGroupTree(String type)
    {
        List<Tree<DictDO>> trees = new ArrayList<Tree<DictDO>>();
        List<DictDO> dicts = listByType(type);
        for (DictDO theDict : dicts)
        {
            Tree<DictDO> tree = new Tree<DictDO>();
            tree.setId      (theDict.getValue());
            tree.setParentId(null);
            tree.setText    (theDict.getName());

            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            tree.setState(state);
            trees.add(tree);
        }
        Tree<DictDO> tree = new Tree<DictDO>();
        tree.setId("noGroup");
        tree.setParentId(null);
        tree.setText    ("未分组设备");
        Map<String, Object> state = new HashMap<>(16);
        state.put("opened", true);
        tree.setState(state);
        trees.add(tree);

        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<DictDO> t = BuildTree.build(trees);
        return t;
    }

}
