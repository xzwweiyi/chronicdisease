package com.dchealth.research.service;

import java.util.List;

import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.domain.TreeSelect;
import com.dchealth.common.core.domain.entity.SysDept;
import com.dchealth.medical.domain.vo.PatientResearchDiseasesTree;
import com.dchealth.research.domain.ResearchDiseases;
import com.dchealth.research.domain.vo.ResearchDiseasesTreeSelect;
import com.dchealth.research.domain.vo.ResearchInfoVo;

/**
 * 病种Service接口
 * 
 * @author lchbu
 * @date 2022-08-24
 */
public interface IResearchDiseasesService 
{
    /**
     * 查询病种
     * 
     * @param diseaseId 病种主键
     * @return 病种
     */
    public ResearchDiseases selectResearchDiseasesByDiseaseId(Long diseaseId);

    /**
     * 查询病种列表
     * 
     * @param researchDiseases 病种
     * @return 病种集合
     */
    public List<ResearchDiseases> selectResearchDiseasesList(ResearchDiseases researchDiseases);

    public List<ResearchDiseasesTreeSelect> selectResearchDiseasesList(List<SysDept> sysDepts, ResearchInfoVo vo);

    /**
     * 队列列表查询
     * @param vo
     * @return
     */
    public List<ResearchDiseasesTreeSelect> selectResearchDiseasesListTree(ResearchInfoVo vo);
    public List<ResearchDiseasesTreeSelect> selectDiseasesAndResearchInfo(ResearchInfoVo vo);

    /**
     * 选择病种下拉包含：科室、病种
     * @return
     */
    public List<ResearchDiseasesTreeSelect> selectList(ResearchInfoVo vo);



    /**
     * 新增病种
     * 
     * @param researchDiseases 病种
     * @return 结果
     */
    public int insertResearchDiseases(ResearchDiseases researchDiseases);

    /**
     * 修改病种
     * 
     * @param researchDiseases 病种
     * @return 结果
     */
    public int updateResearchDiseases(ResearchDiseases researchDiseases);

    /**
     * 批量删除病种
     * 
     * @param diseaseIds 需要删除的病种主键集合
     * @return 结果
     */
    public int deleteResearchDiseasesByDiseaseIds(Long[] diseaseIds);

    /**
     * 删除病种信息
     * 
     * @param diseaseId 病种主键
     * @return 结果
     */
    public AjaxResult deleteResearchDiseasesByDiseaseId(Long diseaseId);

    /**
     * 患者管理：科室-病种-队列树状结构
     * @param vo
     * @return
     */
    List<PatientResearchDiseasesTree> selectPatientResearchDiseasesTree(ResearchInfoVo vo);
}
