package com.dchealth.base.service.impl;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.dchealth.base.domain.AppResearch;
import com.dchealth.base.mapper.AppResearchMapper;
import com.dchealth.base.service.IAppResearchService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * APP 与队列关系Service业务层处理
 *
 * @author ruoyi
 * @date 2022-06-23
 */
@Service
public class AppResearchServiceImpl implements IAppResearchService
{
    @Resource
    private AppResearchMapper appResearchMapper;

    /**
     * 查询APP 与队列关系
     *
     * @param id APP 与队列关系主键
     * @return APP 与队列关系
     */
    @Override
    public AppResearch selectAppResearchById(Long id)
    {
        return appResearchMapper.selectAppResearchById(id);
    }

    /**
     * 查询APP 与队列关系列表
     *
     * @param appResearch APP 与队列关系
     * @return APP 与队列关系
     */
    @Override
    public List<AppResearch> selectAppResearchList(AppResearch appResearch)
    {
        return appResearchMapper.selectAppResearchList(appResearch);
    }

    /**
     * 新增APP 与队列关系
     *
     * @param appResearchVo 与队列关系
     * @return 结果
     */
    @Override
    public int insertAppResearch(AppResearch appResearchVo) {
        List<Long> researchList = appResearchVo.getList();
        AppResearch queryAppResearch = new AppResearch();
        queryAppResearch.setAppId(appResearchVo.getAppId());
        List<AppResearch> appResearches = appResearchMapper.selectAppResearchList(queryAppResearch);
        Set<Long> collect = appResearches.stream().map(AppResearch::getResearchId).collect(Collectors.toSet());
        AtomicInteger insert = new AtomicInteger(0);
        researchList.stream().filter(s-> !CollectionUtils.containsAny(collect,s)).forEach(s ->
                {
                    AppResearch appreach = new AppResearch();
                    appreach.setAppId(appResearchVo.getAppId());
                    appreach.setResearchId(s);
                    appResearchMapper.insertAppResearch(appreach);
                    insert.incrementAndGet();
                }
        );
        return insert.get();
    }

    /**
     * 修改APP 与队列关系
     *
     * @param appResearch APP 与队列关系
     * @return 结果
     */
    @Override
    public int updateAppResearch(AppResearch appResearch)
    {
        return appResearchMapper.updateAppResearch(appResearch);
    }

    /**
     * 批量删除APP 与队列关系
     *
     * @param ids 需要删除的APP 与队列关系主键
     * @return 结果
     */
    @Override
    public int deleteAppResearchByIds(Long[] ids)
    {
        return appResearchMapper.deleteAppResearchByIds(ids);
    }

    /**
     * 删除APP 与队列关系信息
     *
     * @param id APP 与队列关系主键
     * @return 结果
     */
    @Override
    public int deleteAppResearchById(Long id)
    {
        return appResearchMapper.deleteAppResearchById(id);
    }
}

