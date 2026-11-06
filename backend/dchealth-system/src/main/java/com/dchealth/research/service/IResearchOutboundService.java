package com.dchealth.research.service;

import com.dchealth.medical.vo.xunfei.SpeechListRes;
import com.dchealth.medical.vo.xunfei.Var;
import com.dchealth.research.domain.ResearchOutbound;
import com.dchealth.research.domain.vo.ResearchOutboundVo;

import java.util.List;


public interface IResearchOutboundService{
    /**
     * 查询队列智能外呼设置
     * 
     * @param id 队列智能外呼设置主键
     * @return 队列智能外呼设置
     */
    public ResearchOutbound selectResearchOutboundById(Long id);

    /**
     * 查询队列智能外呼设置列表
     * 
     * @param researchOutbound 队列智能外呼设置
     * @return 队列智能外呼设置集合
     */
    public List<ResearchOutbound> selectResearchOutboundList(ResearchOutbound researchOutbound);

    public List<SpeechListRes> speechList();

    public List<Var> speechDetailBySpeechId(String speechId);


    public String speechTemplateImage(String speechId);

    /**
     * 新增队列智能外呼设置
     * 
     * @param researchOutbound 队列智能外呼设置
     * @return 结果
     */
    public int insertResearchOutbound(ResearchOutbound researchOutbound);

    public int add(ResearchOutboundVo researchOutboundVo);

    /**
     * 修改队列智能外呼设置
     * 
     * @param researchOutbound 队列智能外呼设置
     * @return 结果
     */
    public int updateResearchOutbound(ResearchOutbound researchOutbound);

    /**
     * 批量删除队列智能外呼设置
     * 
     * @param ids 需要删除的队列智能外呼设置主键集合
     * @return 结果
     */
    public int deleteResearchOutboundByIds(Long[] ids);

    /**
     * 删除队列智能外呼设置信息
     * 
     * @param id 队列智能外呼设置主键
     * @return 结果
     */
    public int deleteResearchOutboundById(Long id);
}
