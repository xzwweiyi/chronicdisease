package com.dchealth.cro.medical.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dchealth.cro.medical.domain.PatientInfoLogs;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2020-08-06
 */
@Repository
public interface PatientInfoLogsMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param logId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public PatientInfoLogs selectPatientInfoLogsById(Long logId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param patientInfoLogs 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<PatientInfoLogs> selectPatientInfoLogsList(PatientInfoLogs patientInfoLogs);

    /**
     * 新增【请填写功能名称】
     * 
     * @param patientInfoLogs 【请填写功能名称】
     * @return 结果
     */
    public int insertPatientInfoLogs(PatientInfoLogs patientInfoLogs);

    /**
     * 修改【请填写功能名称】
     * 
     * @param patientInfoLogs 【请填写功能名称】
     * @return 结果
     */
    public int updatePatientInfoLogs(PatientInfoLogs patientInfoLogs);

    /**
     * 删除【请填写功能名称】
     * 
     * @param logId 【请填写功能名称】ID
     * @return 结果
     */
    public int deletePatientInfoLogsById(Long logId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param logIds 需要删除的数据ID
     * @return 结果
     */
    public int deletePatientInfoLogsByIds(String[] logIds);
}
