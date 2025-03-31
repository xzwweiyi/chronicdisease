package com.dchealth.medical.service;

import com.dchealth.medical.domain.vo.EdcWorkTree;
import com.dchealth.medical.domain.vo.FollowMedicalFromTree;
import com.dchealth.medical.domain.vo.PatientRecordVo;
import com.dchealth.research.domain.vo.ResearchInfoVo;
import com.dchealth.template.domain.vo.*;

import java.util.List;

public interface IEdcWorkService {

    /**
     * Edc任务树
     * @return
     */
    EdcWorkTree edcTree(ResearchInfoVo vo);

    /**
     * 查询得出任务列表
     * @param vo
     * @return
     */
    List<PatientRecordVo> selectEcdPatientRecordVoList(PatientRecordVo vo);

    /**
     * 根据随访id查询详细信息
     * (弃用)
     * @param vo
     * @return
     */
    MedicalDetailInfoVo edcRecordDetailInfo(FMedicalInfoReq vo);

    MedicalDetailInfoVo edcPatientRecordInfo(FMedicalInfoReq vo);

    /**
     * 模板数据入库
     *
     * @param proVo
     */
    void edcInsert(FMedicalPropertyVo proVo);

    FollowMedicalFromTree followMedicalFromTree(FMedicalInfoReq vo);

    FromDetailInfoVo edcPatientRecordDetailInfo(FMedicalInfoReq vo);

    int lock(FMedicalInfoReq vo);
}
