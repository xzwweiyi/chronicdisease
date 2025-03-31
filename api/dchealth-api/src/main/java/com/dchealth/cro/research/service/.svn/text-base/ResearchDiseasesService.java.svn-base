package com.dchealth.cro.research.service;

import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.research.domain.ResearchDiseases;
import com.dchealth.cro.research.mapper.ResearchDiseasesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 病种Service业务层处理
 * 
 * @author lchbu
 * @date 2022-09-13
 */
@Service
public class ResearchDiseasesService
{
    @Autowired
    private ResearchDiseasesMapper researchDiseasesMapper;

    /**
     * 查询病种
     * 
     * @param diseaseId 病种主键
     * @return 病种
     */
    public ResearchDiseases selectResearchDiseasesByDiseaseId(Long diseaseId)
    {
        return researchDiseasesMapper.selectResearchDiseasesByDiseaseId(diseaseId);
    }

    /**
     * 查询病种列表
     * 
     * @param researchDiseases 病种
     * @return 病种
     */
    public List<ResearchDiseases> selectResearchDiseasesList(ResearchDiseases researchDiseases)
    {
        return researchDiseasesMapper.selectResearchDiseasesList(researchDiseases);
    }

    /**
     * 新增病种
     * 
     * @param researchDiseases 病种
     * @return 结果
     */
    public int insertResearchDiseases(ResearchDiseases researchDiseases)
    {
        researchDiseases.setCreateTime(DateUtils.getNowDate());
        return researchDiseasesMapper.insertResearchDiseases(researchDiseases);
    }

    /**
     * 修改病种
     * 
     * @param researchDiseases 病种
     * @return 结果
     */
    public int updateResearchDiseases(ResearchDiseases researchDiseases)
    {
        researchDiseases.setUpdateTime(DateUtils.getNowDate());
        return researchDiseasesMapper.updateResearchDiseases(researchDiseases);
    }

    /**
     * 批量删除病种
     * 
     * @param diseaseIds 需要删除的病种主键
     * @return 结果
     */
    public int deleteResearchDiseasesByDiseaseIds(Long[] diseaseIds)
    {
        return researchDiseasesMapper.deleteResearchDiseasesByDiseaseIds(diseaseIds);
    }

    /**
     * 删除病种信息
     * 
     * @param diseaseId 病种主键
     * @return 结果
     */
    public int deleteResearchDiseasesByDiseaseId(Long diseaseId)
    {
        return researchDiseasesMapper.deleteResearchDiseasesByDiseaseId(diseaseId);
    }
}
