package com.dchealth.cro.researchPatient.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dchealth.cro.researchPatient.domain.QuitRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 退出队列mapper
 * @author zhangxiao
 */
@Mapper
public interface QuitRecordMapper extends BaseMapper<QuitRecord> {
    /**
     * 修改患者状态为申请退出队列5
     * @param quitRecord 参数
     * @return 修改记录
     */
    int updatePatientStatus(@Param("quitRecord") QuitRecord quitRecord);
}
