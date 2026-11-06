package com.dchealth.system.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.system.mapper.PatientCardInfoViewNullMapper;
import com.dchealth.system.domain.PatientCardInfoViewNull;
import com.dchealth.system.service.IPatientCardInfoViewNullService;

/**
 * 空时患者号Service业务层处理
 * 
 * @author lchbu
 * @date 2024-11-23
 */
@Service
public class PatientCardInfoViewNullServiceImpl implements IPatientCardInfoViewNullService 
{
    @Autowired
    private PatientCardInfoViewNullMapper patientCardInfoViewNullMapper;

    /**
     * 查询空时患者号
     * 
     * @param id 空时患者号主键
     * @return 空时患者号
     */
    @Override
    public PatientCardInfoViewNull selectPatientCardInfoViewNullById(Long id)
    {
        return patientCardInfoViewNullMapper.selectPatientCardInfoViewNullById(id);
    }

    /**
     * 查询空时患者号列表
     * 
     * @param patientCardInfoViewNull 空时患者号
     * @return 空时患者号
     */
    @Override
    public List<PatientCardInfoViewNull> selectPatientCardInfoViewNullList(PatientCardInfoViewNull patientCardInfoViewNull)
    {
        return patientCardInfoViewNullMapper.selectPatientCardInfoViewNullList(patientCardInfoViewNull);
    }

    /**
     * 新增空时患者号
     * 
     * @param patientCardInfoViewNull 空时患者号
     * @return 结果
     */
    @Override
    public int insertPatientCardInfoViewNull(PatientCardInfoViewNull patientCardInfoViewNull)
    {
        patientCardInfoViewNull.setCreateTime(DateUtils.getNowDate());
        return patientCardInfoViewNullMapper.insertPatientCardInfoViewNull(patientCardInfoViewNull);
    }

    /**
     * 修改空时患者号
     * 
     * @param patientCardInfoViewNull 空时患者号
     * @return 结果
     */
    @Override
    public int updatePatientCardInfoViewNull(PatientCardInfoViewNull patientCardInfoViewNull)
    {
        patientCardInfoViewNull.setUpdateTime(DateUtils.getNowDate());
        return patientCardInfoViewNullMapper.updatePatientCardInfoViewNull(patientCardInfoViewNull);
    }

    /**
     * 批量删除空时患者号
     * 
     * @param ids 需要删除的空时患者号主键
     * @return 结果
     */
    @Override
    public int deletePatientCardInfoViewNullByIds(Long[] ids)
    {
        return patientCardInfoViewNullMapper.deletePatientCardInfoViewNullByIds(ids);
    }

    /**
     * 删除空时患者号信息
     * 
     * @param id 空时患者号主键
     * @return 结果
     */
    @Override
    public int deletePatientCardInfoViewNullById(Long id)
    {
        return patientCardInfoViewNullMapper.deletePatientCardInfoViewNullById(id);
    }
}
