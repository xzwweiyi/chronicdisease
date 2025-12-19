package com.dchealth.cro.health.mapper;

import com.dchealth.cro.health.domain.MpPerson;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 患者信息-对应omopMapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Mapper
public interface MpPersonMapper 
{

    /**
     * 查询患者信息-对应omop列表
     * 
     * @param mpPerson 患者信息-对应omop
     * @return 患者信息-对应omop集合
     */
    public MpPerson selectMpPersonByPersonIdCardId(@Param("personId")String personId ,@Param("cardId")String cardId);

    public MpPerson selectMpPersonByCardId(@Param("cardId")String cardId, @Param("mobile")String mobile);

    public MpPerson   selectPersonByAccountId(@Param("accountId")Long accountId);
    public MpPerson   selectPersonByPersonId(@Param("personId")String personId);
    /**
     * 新增患者信息-对应omop
     * 
     * @param mpPerson 患者信息-对应omop
     * @return 结果
     */
    public int insertMpPerson(MpPerson mpPerson);

    public MpPerson getPersonByPersonId(@Param("personId")String personId);

    /**
     * 修改患者手机号
     * @param personId 患者id
     * @param mobile 手机号
     * @return 结果
     */
    Integer updateMobile(@Param("personId") String personId, @Param("mobile") String mobile);
}
