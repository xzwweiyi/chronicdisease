package com.dchealth.system.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.system.mapper.PatientCardInfoViewNoneMapper;
import com.dchealth.system.domain.PatientCardInfoViewNone;
import com.dchealth.system.service.IPatientCardInfoViewNoneService;

/**
 * 增量患者号Service业务层处理
 * 
 * @author lchbu
 * @date 2024-11-23
 */
@Service
public class PatientCardInfoViewNoneServiceImpl implements IPatientCardInfoViewNoneService 
{
    @Autowired
    private PatientCardInfoViewNoneMapper patientCardInfoViewNoneMapper;

    /**
     * 查询增量患者号
     * 
     * @param id 增量患者号主键
     * @return 增量患者号
     */
    @Override
    public PatientCardInfoViewNone selectPatientCardInfoViewNoneById(Long id)
    {
        return patientCardInfoViewNoneMapper.selectPatientCardInfoViewNoneById(id);
    }

    /**
     * 查询增量患者号列表
     * 
     * @param patientCardInfoViewNone 增量患者号
     * @return 增量患者号
     */
    @Override
    public List<PatientCardInfoViewNone> selectPatientCardInfoViewNoneList(PatientCardInfoViewNone patientCardInfoViewNone)
    {
        return patientCardInfoViewNoneMapper.selectPatientCardInfoViewNoneList(patientCardInfoViewNone);
    }

    /**
     * 新增增量患者号
     * 
     * @param patientCardInfoViewNone 增量患者号
     * @return 结果
     */
    @Override
    public int insertPatientCardInfoViewNone(PatientCardInfoViewNone patientCardInfoViewNone)
    {
        patientCardInfoViewNone.setCreateTime(DateUtils.getNowDate());
        return patientCardInfoViewNoneMapper.insertPatientCardInfoViewNone(patientCardInfoViewNone);
    }

    /**
     * 修改增量患者号
     * 
     * @param patientCardInfoViewNone 增量患者号
     * @return 结果
     */
    @Override
    public int updatePatientCardInfoViewNone(PatientCardInfoViewNone patientCardInfoViewNone)
    {
        patientCardInfoViewNone.setUpdateTime(DateUtils.getNowDate());
        return patientCardInfoViewNoneMapper.updatePatientCardInfoViewNone(patientCardInfoViewNone);
    }

    /**
     * 批量删除增量患者号
     * 
     * @param ids 需要删除的增量患者号主键
     * @return 结果
     */
    @Override
    public int deletePatientCardInfoViewNoneByIds(Long[] ids)
    {
        return patientCardInfoViewNoneMapper.deletePatientCardInfoViewNoneByIds(ids);
    }

    /**
     * 删除增量患者号信息
     * 
     * @param id 增量患者号主键
     * @return 结果
     */
    @Override
    public int deletePatientCardInfoViewNoneById(Long id)
    {
        return patientCardInfoViewNoneMapper.deletePatientCardInfoViewNoneById(id);
    }
}
