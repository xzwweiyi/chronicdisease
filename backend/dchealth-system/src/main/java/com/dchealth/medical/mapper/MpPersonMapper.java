package com.dchealth.medical.mapper;

import com.dchealth.medical.domain.MpPerson;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.vo.PatientEnrollmentVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 患者信息-对应omopMapper接口
 * 
 * @date 2023-11-08
 */
public interface MpPersonMapper{
    /**
     * 查询患者信息-对应omop
     * 
     * @param id 患者信息-对应omop主键
     * @return 患者信息-对应omop
     */
    public MpPerson selectMpPersonById(Long id);

    public List<MpPerson> selectMpPersonByPersonIdList(List<String> personIdList);

    /**
     * 查询患者信息-对应omop列表
     * 
     * @param mpPerson 患者信息-对应omop
     * @return 患者信息-对应omop集合
     */
    public List<MpPerson> selectMpPersonList(MpPerson mpPerson);

    public List<PatientEnrollmentVo> selectMpPersonList2(PatientInfo patientInfo);

    /**
     * 新增患者信息-对应omop
     * 
     * @param mpPerson 患者信息-对应omop
     * @return 结果
     */
    public int insertMpPerson(MpPerson mpPerson);

    /**
     * 修改患者信息-对应omop
     * 
     * @param mpPerson 患者信息-对应omop
     * @return 结果
     */
    public int updateMpPerson(MpPerson mpPerson);

    /**
     * 删除患者信息-对应omop
     * 
     * @param id 患者信息-对应omop主键
     * @return 结果
     */
    public int deleteMpPersonById(Long id);

    /**
     * 批量删除患者信息-对应omop
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMpPersonByIds(Long[] ids);


    public MpPerson selectMpPersonByPatientId(Long patientId);

	 /**
     * 查询患者信息-对应omop列表
     * 
     * @param mpPerson 患者信息-对应omop
     * @return 患者信息-对应omop集合
     */
    public MpPerson selectMpPersonByPersonIdCardId(@Param("personId")Long personId ,@Param("cardId")String cardId);

    public MpPerson   selectPersonByAccountId(@Param("accountId")Long accountId);
    public MpPerson   selectPersonByPersonId(@Param("personId")String personId);

    public MpPerson getPersonByPersonId(@Param("personId")String personId);

    /**
     * 根据患者id更新手机号
     * @param personId 患者id
     * @param mobile     手机号
     * @return 更新结果
     */
    Integer updateMobile(@Param("personId") String personId, @Param("mobile") String mobile);

    MpPerson selectMpPersonByCardId(@Param("cardId")String cardId, @Param("mobile")String mobile);

    List<MpPerson> selectMpPersonByCardIdAndMobileBatch(@Param("cardIdList")List<String> cardIdList,@Param("mobileList") List<String> mobileList);
}
