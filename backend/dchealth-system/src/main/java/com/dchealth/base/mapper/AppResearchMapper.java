package com.dchealth.base.mapper;

import com.dchealth.base.domain.AppResearch;

import java.util.List;

/**
 * APP 与队列关系Mapper接口
 *
 * @author ruoyi
 * @date 2022-06-23
 */
public interface AppResearchMapper
{
    /**
     * 查询APP 与队列关系
     *
     * @param id APP 与队列关系主键
     * @return APP 与队列关系
     */
    public AppResearch selectAppResearchById(Long id);

    /**
     * 查询APP 与队列关系列表
     *
     * @param appResearch APP 与队列关系
     * @return APP 与队列关系集合
     */
    public List<AppResearch> selectAppResearchList(AppResearch appResearch);

    /**
     * 新增APP 与队列关系
     *
     * @param appResearch APP 与队列关系
     * @return 结果
     */
    public int insertAppResearch(AppResearch appResearch);

    /**
     * 修改APP 与队列关系
     *
     * @param appResearch APP 与队列关系
     * @return 结果
     */
    public int updateAppResearch(AppResearch appResearch);

    /**
     * 删除APP 与队列关系
     *
     * @param id APP 与队列关系主键
     * @return 结果
     */
    public int deleteAppResearchById(Long id);

    /**
     * 批量删除APP 与队列关系
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAppResearchByIds(Long[] ids);
}

