package com.dchealth.medical.mapper;

import com.dchealth.medical.domain.PatientRecordOutbound;
import com.dchealth.medical.domain.vo.OutboundWorkListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface PatientRecordOutboundMapper{
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

    public List<PatientRecordOutbound> selectGroupResearchIdList(PatientRecordOutbound patientRecordOutbound);

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
     * 删除患者参与队列信息
     * 
     * @param id 患者参与队列信息主键
     * @return 结果
     */
    public int deletePatientRecordOutboundById(Long id);

    /**
     * 批量删除患者参与队列信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePatientRecordOutboundByIds(Long[] ids);


    public int batchInsertList(List<PatientRecordOutbound> list);


    public PatientRecordOutbound selectByOutboundAndRemindType(@Param("recordId") Long recordId, @Param("outboundType") String outboundType, @Param("remindType") String remindType);


    public PatientRecordOutbound selectOne(PatientRecordOutbound patientRecordOutbound);
    public PatientRecordOutbound selectOneId(PatientRecordOutbound patientRecordOutbound);


    public int updateChangeOutboundStatusByRecordId(@Param("oldStatus") String oldStatus,@Param("newStatus") String newStatus,@Param("recordId") Long recordId);
}
