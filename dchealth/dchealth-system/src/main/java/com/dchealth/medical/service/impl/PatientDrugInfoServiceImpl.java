package com.dchealth.medical.service.impl;

import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.compatibility.domain.MpDrugExposure;
import com.dchealth.compatibility.mapper.MpDrugExposureMapper;
import com.dchealth.medical.domain.PatientDrugInfo;
import com.dchealth.medical.mapper.PatientDrugInfoMapper;
import com.dchealth.medical.service.IPatientDrugInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用药信息Service业务层处理
 * 
 * @author wangpf
 * @date 2025-08-06
 */
@Service
public class PatientDrugInfoServiceImpl implements IPatientDrugInfoService
{
    @Autowired
    private PatientDrugInfoMapper patientDrugInfoMapper;

    @Autowired
    private MpDrugExposureMapper mpDrugExposureMapper;

    /**
     * 查询用药信息
     * 
     * @param id 用药信息主键
     * @return 用药信息
     */
    @Override
    public PatientDrugInfo selectPatientDrugInfoById(Long id)
    {
        return patientDrugInfoMapper.selectPatientDrugInfoById(id);
    }

    /**
     * 查询用药信息列表
     * 
     * @param patientDrugInfo 用药信息
     * @return 用药信息
     */
    @Override
    public AjaxResult selectPatientDrugInfoList(PatientDrugInfo patientDrugInfo)
    {
        List<PatientDrugInfo> list=patientDrugInfoMapper.selectPatientDrugInfoList(patientDrugInfo);
        Map<String,List<PatientDrugInfo>> map=list.stream().collect(Collectors.groupingBy(PatientDrugInfo::getVisitOccurrenceId));
        return AjaxResult.success(map);
    }

    /**
     * 新增用药信息
     * 
     * @param patientDrugInfo 用药信息
     * @return 结果
     */
    @Override
    public int insertPatientDrugInfo(PatientDrugInfo patientDrugInfo)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        patientDrugInfo.setCreateTime(new Date());
        patientDrugInfo.setVisitStartTime(new Date());
        patientDrugInfo.setVisitOccurrenceId(patientDrugInfo.getPersonId() + "_" + format.format(new Date()));
        return patientDrugInfoMapper.insertPatientDrugInfo(patientDrugInfo);
    }

    /**
     * 修改用药信息
     * 
     * @param patientDrugInfo 用药信息
     * @return 结果
     */
    @Override
    public int updatePatientDrugInfo(PatientDrugInfo patientDrugInfo)
    {
        patientDrugInfo.setUpdateTime(new Date());
        return patientDrugInfoMapper.updatePatientDrugInfo(patientDrugInfo);
    }

    /**
     * 批量删除用药信息
     * 
     * @param ids 需要删除的用药信息主键
     * @return 结果
     */
    @Override
    public int deletePatientDrugInfoByIds(Long[] ids)
    {
        return patientDrugInfoMapper.deletePatientDrugInfoByIds(ids);
    }

    /**
     * 删除用药信息信息
     * 
     * @param id 用药信息主键
     * @return 结果
     */
    @Override
    public int deletePatientDrugInfoById(Long id)
    {
        return patientDrugInfoMapper.deletePatientDrugInfoById(id);
    }

    @Override
    public List<PatientDrugInfo> getPatientDrugInfoList(PatientDrugInfo patientDrugInfo) {
        return patientDrugInfoMapper.selectPatientDrugInfoList(patientDrugInfo);
    }

    @Override
    public AjaxResult mpDrugExposureList(MpDrugExposure mpDrugExposure) {

        List<MpDrugExposure> patientDrugInfos = mpDrugExposureMapper.selectPatientDrugInfoList(mpDrugExposure);
        Map<String,List<MpDrugExposure>> map=patientDrugInfos.stream().collect(Collectors.groupingBy(MpDrugExposure::getDepartmentName));
        return AjaxResult.success(map);
    }
}
