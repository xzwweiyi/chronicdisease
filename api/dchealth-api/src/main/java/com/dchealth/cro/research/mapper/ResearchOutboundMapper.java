package com.dchealth.cro.research.mapper;

import com.dchealth.cro.research.domain.ResearchOutbound;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResearchOutboundMapper {
    /**
     * 查询队列智能外呼设置
     * 
     * @param id 队列智能外呼设置主键
     * @return 队列智能外呼设置
     */
    public ResearchOutbound selectResearchOutboundById(Long id);

    public ResearchOutbound selectByResearchIdAndType(@Param("researchId") Long researchId, @Param("type") String type);

    /**
     * 查询队列智能外呼设置列表
     * 
     * @param researchOutbound 队列智能外呼设置
     * @return 队列智能外呼设置集合
     */
    public List<ResearchOutbound> selectResearchOutboundList(ResearchOutbound researchOutbound);

    /**
     * 新增队列智能外呼设置
     * 
     * @param researchOutbound 队列智能外呼设置
     * @return 结果
     */
    public int insertResearchOutbound(ResearchOutbound researchOutbound);

    /**
     * 修改队列智能外呼设置
     * 
     * @param researchOutbound 队列智能外呼设置
     * @return 结果
     */
    public int updateResearchOutbound(ResearchOutbound researchOutbound);

    public int updateResearchOutbound2(ResearchOutbound researchOutbound);

    /**
     * 删除队列智能外呼设置
     * 
     * @param id 队列智能外呼设置主键
     * @return 结果
     */
    public int deleteResearchOutboundById(Long id);

    /**
     * 批量删除队列智能外呼设置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResearchOutboundByIds(Long[] ids);

    public int updateSetDelByResearchId(Long researchId);


}
