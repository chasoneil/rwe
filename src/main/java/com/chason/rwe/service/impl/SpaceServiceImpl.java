package com.chason.rwe.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chason.common.domain.Tree;
import com.chason.common.utils.BuildTree;
import com.chason.common.utils.StringUtils;
import com.chason.rwe.dao.SpaceDao;
import com.chason.rwe.domain.SpaceDO;
import com.chason.rwe.service.SpaceService;
import com.chason.system.domain.UserDO;

@Service
public class SpaceServiceImpl implements SpaceService {

    @Autowired
	private SpaceDao spaceDao;

	@Override
	public SpaceDO get(String spaceId){
		return spaceDao.get(spaceId);
	}

	@Override
	public SpaceDO findByCode(String code) {
	    return spaceDao.findByCode(code);
	}

	@Override
	public List<SpaceDO> list(Map<String, Object> map){
		return spaceDao.list(map);
	}

	/**
	 * 列出属于传入用户管理区域的space
	 */
	@Override
	public List<SpaceDO> listByLikeManager(UserDO user)
	{
	    Map<String, Object> param = new HashMap<>();
        List<SpaceDO> spaces = this.list(param); //所有的space
        String currentUserId = user.getUserId() + "";
        List<SpaceDO> currentSpaces = new ArrayList<>(); //当前用户的space
        for (SpaceDO theSpace : spaces)
        {
            String spaceManagerBy = theSpace.getSpaceManagerBy();
            String[] strIds = spaceManagerBy.split(",");
            List<String> idTmp = new ArrayList<>();
            for (String id : strIds)
            {
                idTmp.add(id);
            }
            if(idTmp.contains(currentUserId))
            {
                currentSpaces.add(theSpace);
            }
        }
	    return currentSpaces;
	}

	@Override
	public int count(Map<String, Object> map){
		return spaceDao.count(map);
	}

	@Override
	public int save(SpaceDO space){
		return spaceDao.save(space);
	}

	@Override
	public int update(SpaceDO space){
		return spaceDao.update(space);
	}

	@Override
	public int remove(String spaceId){
		return spaceDao.remove(spaceId);
	}

	@Override
	public int batchRemove(String[] spaceIds){
		return spaceDao.batchRemove(spaceIds);
	}

	/**
	 * 空间编号是否唯一
	 */
	@Override
	public boolean codeIsUnique(String code)
	{
	    boolean flag = true;
	    Map<String, Object> param = new HashMap<>();
	    List<SpaceDO> spaces = spaceDao.list(param);
	    for (SpaceDO theSpace : spaces)
        {
            if(code.equals(theSpace.getSpaceCode()))
            {
                flag = false;
                break;
            }
        }
	    return flag;
	}

	/**
	 * 得到指定用户权限的空间树
	 *
	 */
	public Tree<SpaceDO> getTreeByUser(UserDO user)
	{
	    Map<String, Object> param = new HashMap<>();
        List<SpaceDO> spaces = this.list(param);
        String currentUserId = user.getUserId() + "";
        List<SpaceDO> currentSpaces = new ArrayList<>();
        for (SpaceDO theSpace : spaces)
        {
            String spaceManagerBy = theSpace.getSpaceManagerBy();
            String[] strIds = spaceManagerBy.split(",");
            List<String> idTmp = new ArrayList<>();
            for (String id : strIds)
            {
                idTmp.add(id);
            }
            if(idTmp.contains(currentUserId))
            {
                currentSpaces.add(theSpace);
            }
        }
        List<Tree<SpaceDO>> trees = new ArrayList<Tree<SpaceDO>>();
        for (SpaceDO theSpace : currentSpaces)
        {
            Tree<SpaceDO> tree = new Tree<SpaceDO>();
            tree.setId      (theSpace.getSpaceId());
            if(!StringUtils.isNotNull(theSpace.getSpaceParentId()))
            {
                tree.setParentId(null);
            }
            else
            {
                tree.setParentId(theSpace.getSpaceParentId());
            }
            tree.setText    (theSpace.getSpaceAddress());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<SpaceDO> t = BuildTree.build(trees);
        return t;
	}

    /**
     * 得到空间的树形结构
     * 手工增加的方法
     * */
    @Override
    public Tree<SpaceDO> getTree(UserDO user)
    {
        List<Tree<SpaceDO>> trees = new ArrayList<Tree<SpaceDO>>();
        List<SpaceDO> spaces;
        if (user != null && StringUtils.isNotNull(user.getName()))
        {
            spaces = listByLikeManager(user);
        }
        else
        {
            spaces = list(new HashMap<String, Object>());
        }

        for (SpaceDO theSpace : spaces)
        {
            Tree<SpaceDO> tree = new Tree<SpaceDO>();
            tree.setId      (theSpace.getSpaceId());
            if(!StringUtils.isNotNull(theSpace.getSpaceParentId()))
            {
                tree.setParentId(null);
            }
            else
            {
                tree.setParentId(theSpace.getSpaceParentId());
            }
            tree.setText    (theSpace.getSpaceAddress());

            Map<String, Object> state = new HashMap<>(16);
            state.put("closed", true);
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<SpaceDO> t = BuildTree.build(trees);
        return t;
    }

    @Override
    public List<String> getTerminal(List<String> spaceIds)
    {
        if(spaceIds.size() == 1)
        {
            return spaceIds;
        }
        else
        {
            for (String string : spaceIds)
            {
                SpaceDO theSpace = spaceDao.get(string);
                if(!spaceIds.contains(theSpace.getSpaceParentId()))
                {
                    spaceIds.remove(string);
                    break;
                }
            }
            return getTerminal(spaceIds);
        }
    }
}
