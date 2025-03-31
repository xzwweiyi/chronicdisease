package com.dchealth.research.service;

import java.util.List;
import com.dchealth.research.domain.ResearchDetail;

/**
 * 队列详情Service接口
 * 
 * @author lchbu
 * @date 2022-08-22
 */
public interface IResearchDetailService 
{
    /**
     * 查询队列详情
     * 
     * @param researchId 队列详情主键
     * @return 队列详情
     */
    public ResearchDetail selectResearchDetailByResearchId(Long researchId);

    /**
     * 查询队列详情列表
     * 
     * @param researchDetail 队列详情
     * @return 队列详情集合
     */
    public List<ResearchDetail> selectResearchDetailList(ResearchDetail researchDetail);

    /**
     * 新增队列详情
     * 
     * @param researchDetail 队列详情
     * @return 结果
     */
    public int insertResearchDetail(ResearchDetail researchDetail);

    /**
     * 修改队列详情
     * 
     * @param researchDetail 队列详情
     * @return 结果
     */
    public int updateResearchDetail(ResearchDetail researchDetail);

    /**
     * 批量删除队列详情
     * 
     * @param researchIds 需要删除的队列详情主键集合
     * @return 结果
     */
    public int deleteResearchDetailByResearchIds(Long[] researchIds);

    /**
     * 删除队列详情信息
     * 
     * @param researchId 队列详情主键
     * @return 结果
     */
    public int deleteResearchDetailByResearchId(Long researchId);
}
