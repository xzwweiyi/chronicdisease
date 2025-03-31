package com.dchealth.medical.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dchealth.common.utils.DateUtils;
import com.dchealth.medical.domain.MpPerson;
import com.dchealth.medical.domain.ResearchPatient;
import com.dchealth.medical.mapper.ResearchPatientMapper;
import com.dchealth.medical.service.IResearchPatientService;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2022-06-15
 */
@Service
public class ResearchPatientServiceImpl implements IResearchPatientService
{
    @Autowired
    private ResearchPatientMapper researchPatientMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public ResearchPatient selectResearchPatientById(Long id)
    {
        return researchPatientMapper.selectResearchPatientById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param researchPatient 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<ResearchPatient> selectResearchPatientList(ResearchPatient researchPatient)
    {
        return researchPatientMapper.selectResearchPatientList(researchPatient);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param researchPatient 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertResearchPatient(ResearchPatient researchPatient) {
        ResearchPatient queryResearchPatient = new ResearchPatient();
        queryResearchPatient.setResearchId(researchPatient.getResearchId());
        List<ResearchPatient> existResearchPatients = researchPatientMapper.selectResearchPatientList(queryResearchPatient);
        Set<Long> collect = existResearchPatients.stream().map(ResearchPatient::getBasePatientId).collect(Collectors.toSet());
        List<ResearchPatient> newList = researchPatient.getList().stream().filter(p -> !CollectionUtils.containsAny(collect, p)).map(
                p -> {
                    ResearchPatient researchPatientNew = new ResearchPatient();
                    researchPatientNew.setResearchId(researchPatient.getResearchId());
                    researchPatientNew.setBasePatientId(p);
                    researchPatientNew.setCreateTime(DateUtils.getNowDate());
                    researchPatientNew.setPatientId(p);
                    return researchPatientNew;
                }
        ).collect(Collectors.toList());
        return CollectionUtils.isEmpty(newList)?CollectionUtils.size(newList):researchPatientMapper.batchInsertResearchPatient(newList);
    }

    @Override
    public int batchInsertResearchPatient(List<ResearchPatient> researchPatients) {
        return researchPatientMapper.batchInsertResearchPatient(researchPatients);
    }

    @Override
    public int batchDelResearchPatient(List<ResearchPatient> researchPatients) {
        researchPatients.forEach(s -> researchPatientMapper.delResearchPatient(s));
        return CollectionUtils.size(researchPatients);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param researchPatient 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateResearchPatient(ResearchPatient researchPatient)
    {
        return researchPatientMapper.updateResearchPatient(researchPatient);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteResearchPatientByIds(Long[] ids)
    {
        return researchPatientMapper.deleteResearchPatientByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteResearchPatientById(Long id)
    {
        return researchPatientMapper.deleteResearchPatientById(id);
    }

    @Override
    public List<MpPerson> listByDiseaseId(ResearchPatient researchPatient) {
        return researchPatientMapper.listByDiseaseId(researchPatient);
    }
}

