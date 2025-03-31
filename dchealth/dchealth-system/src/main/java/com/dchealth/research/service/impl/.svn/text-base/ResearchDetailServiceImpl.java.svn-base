package com.dchealth.research.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.research.mapper.ResearchDetailMapper;
import com.dchealth.research.domain.ResearchDetail;
import com.dchealth.research.service.IResearchDetailService;

/**
 * 队列详情Service业务层处理
 * 
 * @author lchbu
 * @date 2022-08-22
 */
@Service
public class ResearchDetailServiceImpl implements IResearchDetailService 
{
    @Autowired
    private ResearchDetailMapper researchDetailMapper;

    /**
     * 查询队列详情
     * 
     * @param researchId 队列详情主键
     * @return 队列详情
     */
    @Override
    public ResearchDetail selectResearchDetailByResearchId(Long researchId)
    {
        return researchDetailMapper.selectResearchDetailByResearchId(researchId);
    }

    /**
     * 查询队列详情列表
     * 
     * @param researchDetail 队列详情
     * @return 队列详情
     */
    @Override
    public List<ResearchDetail> selectResearchDetailList(ResearchDetail researchDetail)
    {
        return researchDetailMapper.selectResearchDetailList(researchDetail);
    }

    /**
     * 新增队列详情
     * 
     * @param researchDetail 队列详情
     * @return 结果
     */
    @Override
    public int insertResearchDetail(ResearchDetail researchDetail)
    {
        return researchDetailMapper.insertResearchDetail(researchDetail);
    }

    /**
     * 修改队列详情
     * 
     * @param researchDetail 队列详情
     * @return 结果
     */
    @Override
    public int updateResearchDetail(ResearchDetail researchDetail)
    {
        return researchDetailMapper.updateResearchDetail(researchDetail);
    }

    /**
     * 批量删除队列详情
     * 
     * @param researchIds 需要删除的队列详情主键
     * @return 结果
     */
    @Override
    public int deleteResearchDetailByResearchIds(Long[] researchIds)
    {
        return researchDetailMapper.deleteResearchDetailByResearchIds(researchIds);
    }

    /**
     * 删除队列详情信息
     * 
     * @param researchId 队列详情主键
     * @return 结果
     */
    @Override
    public int deleteResearchDetailByResearchId(Long researchId)
    {
        return researchDetailMapper.deleteResearchDetailByResearchId(researchId);
    }
}
