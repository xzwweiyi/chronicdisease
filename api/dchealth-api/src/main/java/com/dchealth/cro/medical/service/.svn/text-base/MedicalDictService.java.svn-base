package com.dchealth.cro.medical.service;

import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.core.domain.SysDept;
import com.dchealth.cro.core.mapper.SysDeptMapper;
import com.dchealth.cro.research.domain.ResearchDiseases;
import com.dchealth.cro.research.domain.ResearchInfo;
import com.dchealth.cro.research.mapper.ResearchDiseasesMapper;
import com.dchealth.cro.research.mapper.ResearchInfoMapper;
import com.dchealth.cro.research.vo.ResearchApplyDictVO;
import com.dchealth.cro.research.vo.ResearchDiseasesDictVO;
import com.dchealth.cro.research.vo.ResearchInfoDictVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *字典
 */
@Service
public class MedicalDictService {
    private static final Logger logger = LoggerFactory.getLogger(MedicalDictService.class);

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private ResearchDiseasesMapper researchDiseasesMapper;
    @Autowired
    private ResearchInfoMapper researchInfoMapper;

    /**
     * 队列申请字典
     * @param deptId
     * @param diseasesId
     * @param researchId
     * @return
     */
    public DchealthCloudResponseEntity<ResearchApplyDictVO> researchApply(Long deptId, Long diseasesId, Long researchId){
        DchealthCloudResponseEntity<ResearchApplyDictVO> result = new DchealthCloudResponseEntity<>();
        if (deptId == null && diseasesId != null){
            ResearchDiseases researchDiseases = researchDiseasesMapper.selectResearchDiseasesByDiseaseId(diseasesId);
            deptId = researchDiseases != null ? researchDiseases.getDeptId() : null;
        }
        if (deptId == null && researchId != null){
            ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(researchId);
            deptId = researchInfo != null ? researchInfo.getDeptId() : null;
        }
        if (deptId == null){//参数异常
            result.setCode(CroApiCodeConstant.ERROR_PARAM.getCode());
            return result;
        }
        SysDept sysDept = sysDeptMapper.selectSysDeptByDeptId(deptId);
        if (sysDept == null){//数据异常
            logger.error("数据异常,部门ID:{},病种ID:{},队列ID:{}", deptId, diseasesId,researchId);
            result.setCode(CroApiCodeConstant.ERROR_PARAM.getCode());
            return result;
        }
        ResearchApplyDictVO researchApplyDictVO = new ResearchApplyDictVO();
        researchApplyDictVO.setDeptName(sysDept.getDeptName());
        List<ResearchDiseases> researchDiseasesList = researchDiseasesMapper.selectResearchDiseasesByDeptId(deptId);
        List<ResearchInfo> researchInfos = researchInfoMapper.selectResearchInfoByDeptId(deptId);
        Map<Long, List<ResearchInfoDictVO>> researchInfoMap = null;
        if (researchInfos != null && !researchInfos.isEmpty()){
            researchInfoMap = researchInfos.stream()
                                    .map(ResearchInfoDictVO :: new)
                                    .collect(Collectors.groupingBy(ResearchInfoDictVO :: getDiseaseId));
        }
        if (researchDiseasesList != null && !researchDiseasesList.isEmpty()){
            final Map<Long, List<ResearchInfoDictVO>> finalResearchInfoMap = researchInfoMap;
            List<ResearchDiseasesDictVO> researchDiseasesDictVOS = researchDiseasesList.stream()
                    .map(x->new ResearchDiseasesDictVO(x, finalResearchInfoMap != null ? finalResearchInfoMap.get(x.getDiseaseId()) : null)).collect(Collectors.toList());
            researchApplyDictVO.setResearchDiseasesDictVOS(researchDiseasesDictVOS);
        }
        result.setData(researchApplyDictVO);
        return result;
    }

}
