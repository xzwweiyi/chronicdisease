package com.dchealth.cro.research.mapper;

import com.dchealth.cro.medical.dto.DiseaseResearchPatientDto;
import com.dchealth.cro.research.domain.ResearchInfo;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * 队列基础信息Mapper接口
 * 
 * @author lchbu
 * @date 2022-09-14
 */
@Repository
public interface ResearchInfoMapper 
{
    /**
     * 查询队列基础信息
     * 
     * @param id 队列基础信息主键
     * @return 队列基础信息
     */
    public ResearchInfo selectResearchInfoById(Long id);
    public ResearchInfo selectResearchOwnerById(Long id);
    public ResearchInfo selectByIdAndStatus(ResearchInfo researchInfo);

    /**
     * 通过患者ID查询对应的病种和队列信息
     * @param patientId
     * @return
     */

    public DiseaseResearchPatientDto selectDiseaseResearchPatientByPatientId(Long patientId);
    /**
     * 根据病种查询队列列表
     * @param id
     * @return
     */
    public List<ResearchInfo> selectResearchInfoByDiseaseId(Long id);

    /**
     * 查询队列基础信息列表
     * 
     * @param researchInfo 队列基础信息
     * @return 队列基础信息集合
     */
    public List<ResearchInfo> selectResearchInfoList(ResearchInfo researchInfo);

    /**
     * 随访工作站使用需完善
     * @return
     */
    public List<ResearchInfo> selectResearchInfoByAuth();

    public List<ResearchInfo> selectResearchInfoByUserId(Long userId);

    /**
     * 科室id查询
     * @param deptId
     * @return
     */
    public List<ResearchInfo> selectResearchInfoByDeptId(Long deptId);

    /**
     * 新增队列基础信息
     * 
     * @param researchInfo 队列基础信息
     * @return 结果
     */
    public int insertResearchInfo(ResearchInfo researchInfo);

    /**
     * 修改队列基础信息
     * 
     * @param researchInfo 队列基础信息
     * @return 结果
     */
    public int updateResearchInfo(ResearchInfo researchInfo);

    /**
     * 删除队列基础信息
     * 
     * @param id 队列基础信息主键
     * @return 结果
     */
    public int deleteResearchInfoById(Long id);

    /**
     * 批量删除队列基础信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResearchInfoByIds(Long[] ids);

    /**
     * 根据医生id查询相应的队列
     * @param doctorId
     * @return
     */
    public List<ResearchInfo> selectResearchInfoByDoctorId(Long doctorId);
}
