package com.dchealth.medical.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.medical.domain.PatientRecordEdcStatus;
import com.dchealth.medical.mapper.PatientRecordEdcStatusMapper;
import com.dchealth.medical.service.IPatientRecordEdcStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 随访edc模板状态Service业务层处理
 * 
 * @author lchbu
 * @date 2023-11-16
 */
@Service
public class PatientRecordEdcStatusServiceImpl implements IPatientRecordEdcStatusService {
    @Autowired
    private PatientRecordEdcStatusMapper patientRecordEdcStatusMapper;

    /**
     * 查询随访edc模板状态
     * 
     * @param id 随访edc模板状态主键
     * @return 随访edc模板状态
     */
    @Override
    public PatientRecordEdcStatus selectPatientRecordEdcStatusById(Long id)
    {
        return patientRecordEdcStatusMapper.selectPatientRecordEdcStatusById(id);
    }

    /**
     * 查询随访edc模板状态列表
     * 
     * @param patientRecordEdcStatus 随访edc模板状态
     * @return 随访edc模板状态
     */
    @Override
    public List<PatientRecordEdcStatus> selectPatientRecordEdcStatusList(PatientRecordEdcStatus patientRecordEdcStatus)
    {
        return patientRecordEdcStatusMapper.selectPatientRecordEdcStatusList(patientRecordEdcStatus);
    }

    /**
     * 新增随访edc模板状态
     * 
     * @param patientRecordEdcStatus 随访edc模板状态
     * @return 结果
     */
    @Override
    public int insertPatientRecordEdcStatus(PatientRecordEdcStatus patientRecordEdcStatus)
    {
        patientRecordEdcStatus.setCreateTime(DateUtils.getNowDate());
        return patientRecordEdcStatusMapper.insertPatientRecordEdcStatus(patientRecordEdcStatus);
    }

    /**
     * 修改随访edc模板状态
     * 
     * @param patientRecordEdcStatus 随访edc模板状态
     * @return 结果
     */
    @Override
    public int updatePatientRecordEdcStatus(PatientRecordEdcStatus patientRecordEdcStatus)
    {
        return patientRecordEdcStatusMapper.updatePatientRecordEdcStatus(patientRecordEdcStatus);
    }

    /**
     * 批量删除随访edc模板状态
     * 
     * @param ids 需要删除的随访edc模板状态主键
     * @return 结果
     */
    @Override
    public int deletePatientRecordEdcStatusByIds(Long[] ids)
    {
        return patientRecordEdcStatusMapper.deletePatientRecordEdcStatusByIds(ids);
    }

    /**
     * 删除随访edc模板状态信息
     * 
     * @param id 随访edc模板状态主键
     * @return 结果
     */
    @Override
    public int deletePatientRecordEdcStatusById(Long id)
    {
        return patientRecordEdcStatusMapper.deletePatientRecordEdcStatusById(id);
    }
}
