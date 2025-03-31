package com.dchealth.base.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.base.mapper.AppSetMapper;
import com.dchealth.base.domain.AppSet;
import com.dchealth.base.service.IAppSetService;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author lchbu
 * @date 2023-10-08
 */
@Service
public class AppSetServiceImpl implements IAppSetService{
    @Autowired
    private AppSetMapper appSetMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public AppSet selectAppSetById(Long id)
    {
        return appSetMapper.selectAppSetById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param appSet 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<AppSet> selectAppSetList(AppSet appSet)
    {
        return appSetMapper.selectAppSetList(appSet);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param appSet 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertAppSet(AppSet appSet){
        appSet.setCreateTime(DateUtils.getNowDate());
        return appSetMapper.insertAppSet(appSet);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param appSet 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateAppSet(AppSet appSet){
        appSet.setUpdateTime(DateUtils.getNowDate());
        return appSetMapper.updateAppSet(appSet);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteAppSetByIds(Long[] ids)
    {
        return appSetMapper.deleteAppSetByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteAppSetById(Long id)
    {
        return appSetMapper.deleteAppSetById(id);
    }
}
