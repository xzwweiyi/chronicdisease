package com.dchealth.medical.service;

import com.dchealth.medical.domain.PatientRecordOutbound;
import com.dchealth.medical.domain.vo.OutboundTree;
import com.dchealth.medical.domain.vo.OutboundWorkListVO;

import java.util.List;

public interface IPatientRecordOutboundService{
    /**
     * 查询患者参与队列信息
     * 
     * @param id 患者参与队列信息主键
     * @return 患者参与队列信息
     */
    public PatientRecordOutbound selectPatientRecordOutboundById(Long id);

    /**
     * 查询患者参与队列信息列表
     * 
     * @param patientRecordOutbound 患者参与队列信息
     * @return 患者参与队列信息集合
     */
    public List<PatientRecordOutbound> selectPatientRecordOutboundList(PatientRecordOutbound patientRecordOutbound);


    public List<OutboundWorkListVO> selectOutboundList(PatientRecordOutbound patientRecordOutbound);

    /**
     * 新增患者参与队列信息
     * 
     * @param patientRecordOutbound 患者参与队列信息
     * @return 结果
     */
    public int insertPatientRecordOutbound(PatientRecordOutbound patientRecordOutbound);

    /**
     * 修改患者参与队列信息
     * 
     * @param patientRecordOutbound 患者参与队列信息
     * @return 结果
     */
    public int updatePatientRecordOutbound(PatientRecordOutbound patientRecordOutbound);

    /**
     * 批量删除患者参与队列信息
     * 
     * @param ids 需要删除的患者参与队列信息主键集合
     * @return 结果
     */
    public int deletePatientRecordOutboundByIds(Long[] ids);

    /**
     * 删除患者参与队列信息信息
     * 
     * @param id 患者参与队列信息主键
     * @return 结果
     */
    public int deletePatientRecordOutboundById(Long id);


    public String followRecordByResearchId(Long researchId);

    public String appNoticeByResearch(Long researchId);


    public OutboundTree outboundTree(Long userId);

    //单次外呼任务
    public String callbackByResearchId(Long researchId);

    //获取外呼结果
    public String getResult(Long researchId);

    public String callByRecordId(Long recordId);

    public String downloadWav(Long id);

    public String selectSevenDayList(Long researchId);
}
