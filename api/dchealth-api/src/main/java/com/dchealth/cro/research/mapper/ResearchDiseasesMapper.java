package com.dchealth.cro.research.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dchealth.cro.research.domain.ResearchDiseases;

/**
 * 病种Mapper接口
 * 
 * @author lchbu
 * @date 2022-09-13
 */
@Repository
public interface ResearchDiseasesMapper 
{
    /**
     * 查询病种
     * 
     * @param diseaseId 病种主键
     * @return 病种
     */
    public ResearchDiseases selectResearchDiseasesByDiseaseId(Long diseaseId);

    /**
     * 部门id查询病种
     * @param deptId
     * @return
     */
    public List<ResearchDiseases> selectResearchDiseasesByDeptId(Long deptId);

    /**
     * 查询病种列表
     * 
     * @param researchDiseases 病种
     * @return 病种集合
     */
    public List<ResearchDiseases> selectResearchDiseasesList(ResearchDiseases researchDiseases);

    /**
     * 随访工作站使用需完善
     * @return
     */
    public List<ResearchDiseases> selectResearchDiseasesByAuth();

    public List<ResearchDiseases> selectResearchDiseasesByUserId(@Param("userId") Long userId);

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
     * 删除病种
     * 
     * @param diseaseId 病种主键
     * @return 结果
     */
    public int deleteResearchDiseasesByDiseaseId(Long diseaseId);

    /**
     * 批量删除病种
     * 
     * @param diseaseIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResearchDiseasesByDiseaseIds(Long[] diseaseIds);
}
