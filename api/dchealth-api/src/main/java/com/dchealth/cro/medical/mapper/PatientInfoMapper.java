package com.dchealth.cro.medical.mapper;

import com.dchealth.cro.medical.domain.PatientInfo;
import com.dchealth.cro.medical.form.FollowWorkListForm;
import com.dchealth.cro.medical.form.PatientSearchForm;
import com.dchealth.cro.medical.vo.FollowWorkListVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 患者信息Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2020-07-29
 */
@Repository
public interface PatientInfoMapper 
{


    /**
     * 查询患者信息
     * 
     * @param patientId 患者信息ID
     * @return 患者信息
     */
    public PatientInfo selectPatientInfoById(Long patientId);
    public PatientInfo selectPatientByResearchIdAndPatietnId(Long researchId,Long patientId);

    public List<PatientInfo> selectPatientInfoByAccountId(Long accountId);
    public PatientInfo selectPatientInfoByOldId(Long patientId);
    public PatientInfo selectExcelPatientInfo(@Param("patientMobile") String patientMobile,@Param("idCardNo") String idCardNo,@Param("patientCode") String patientCode);

    /**
     * 查询患者信息列表
     * 
     * @param patientInfo 患者信息
     * @return 患者信息集合
     */
    public List<PatientInfo> selectPatientInfoList(PatientInfo patientInfo);

    /**
     * 查找新队列
     * @param planResearchId
     * @return
     */
    public List<PatientInfo> selectPatientInfoByPlanResearchId(Long planResearchId);
    public List<PatientInfo> selectPatientInfoByResearchId(Long researchId);

    public List<PatientInfo> selectPatientList(PatientSearchForm patientSearchForm);

    /**
     * 新增患者信息
     * 
     * @param patientInfo 患者信息
     * @return 结果
     */
    public int insertPatientInfo(PatientInfo patientInfo);

    /**
     * 修改患者信息
     * 
     * @param patientInfo 患者信息
     * @return 结果
     */
    public int updatePatientInfo(PatientInfo patientInfo);
    public int updatePatientCode(PatientInfo patientInfo);

    /**
     * 删除患者信息
     * 
     * @param patientId 患者信息ID
     * @return 结果
     */
    public int deletePatientInfoById(Long patientId);

    /**
     * 批量删除患者信息
     * 
     * @param patientIds 需要删除的数据ID
     * @return 结果
     */
    public int deletePatientInfoByIds(String[] patientIds);

	public PatientInfo selectPatientInfoByMobile(String patientMobile);
	public PatientInfo selectPatientInfoByIdCardNo(String idCardNo);
	public PatientInfo selectPatientInfoByPatientCode(String patientCode);
	public PatientInfo selectPatientInfoByOpenId(String openId);

    /**
     * 待分配
     * @param followWorkListForm
     * @return
     */
    public List<FollowWorkListVO> allocatedList(FollowWorkListForm followWorkListForm);
    
    public List<PatientInfo> selectPatientInfoByAccountIdEnrollment(@Param("accountId")Long accountId);

    /**
     * 更新患者手机号
     * @param personId 患者id
     * @param mobile 手机号
     * @return 结果
     */
    Integer updatePatientMobile(@Param("personId") String personId, @Param("mobile") String mobile);

    /**
     * 查询患者信息
     *
     * @param patientId 患者信息主键
     * @return 患者信息
     */
    public PatientInfo selectPatientInfoByPatientId(Long patientId);

    List<PatientInfo> selectPatientInfoByPersonId(@Param("personId")String personId);
}
