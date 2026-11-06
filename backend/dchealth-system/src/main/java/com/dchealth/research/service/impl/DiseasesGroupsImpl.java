package com.dchealth.research.service.impl;

import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.research.domain.DiseasesGroups;
import com.dchealth.research.domain.ResearchDiseases;
import com.dchealth.research.mapper.DiseasesGroupsMapper;
import com.dchealth.research.mapper.ResearchDiseasesMapper;
import com.dchealth.research.service.IDiseasesGroupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DiseasesGroupsImpl implements IDiseasesGroupsService {


    @Autowired
    private DiseasesGroupsMapper diseasesGroupsMapper;

    @Autowired
    private ResearchDiseasesMapper researchDiseasesMapper;

    @Override
    public List<DiseasesGroups> selectDiseasesGroupsList(DiseasesGroups groups) {
        return diseasesGroupsMapper.selectDiseasesGroupsList(groups);
    }

    @Override
    public int insertDiseasesGroups(DiseasesGroups groups) {
        groups.setCreateTime(DateUtils.getNowDate());
        return diseasesGroupsMapper.insertDiseasesGroups(groups);
    }

    @Override
    @Transactional
    public int updateDiseasesGroups(DiseasesGroups groups) {
        groups.setUpdateTime(DateUtils.getNowDate());
        //修改队列中病重名称
        ResearchDiseases researchDiseases = new ResearchDiseases();
        researchDiseases.setDiseasesGroupsId(String.valueOf(groups.getDiseasesGroupsId()));
        researchDiseases.setDiseaseName(groups.getDiseasesGroupsName());
        researchDiseasesMapper.updateResearchDiseases(researchDiseases);

        return diseasesGroupsMapper.updateDiseasesGroups(groups);
    }

    @Override
    @Transactional
    public AjaxResult deleteDiseasesGroupsByDiseasesGroupsId(DiseasesGroups groups) {
        Long diseasesGroupsId = groups.getDiseasesGroupsId();
        int rows = diseasesGroupsMapper.deleteDiseasesGroupsByDiseasesGroupsId(diseasesGroupsId);
        //同步删除research_diseases
        ResearchDiseases researchDiseases = new ResearchDiseases();
        researchDiseases.setIsDeleted("1");
        researchDiseases.setDiseasesGroupsId(String.valueOf(diseasesGroupsId));
        researchDiseasesMapper.updateResearchDiseases(researchDiseases);

        return rows > 0 ? AjaxResult.success() : AjaxResult.error();

    }

    @Override
    public List<DiseasesGroups> selectDiseasesGroupsListPage(DiseasesGroups groups) {
        return diseasesGroupsMapper.selectDiseasesGroupsListPage(groups);
    }

    @Override
    public DiseasesGroups selectBydiseasesGroupsName(String diseasesGroupsName) {
        return diseasesGroupsMapper.selectBydiseasesGroupsName(diseasesGroupsName);
    }
}
