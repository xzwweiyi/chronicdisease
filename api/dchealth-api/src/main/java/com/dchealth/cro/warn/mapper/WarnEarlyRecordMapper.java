package com.dchealth.cro.warn.mapper;

import com.dchealth.cro.warn.domain.WarnEarlyRecord;
import com.dchealth.cro.warn.vo.WarnConditionValueVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 周期预警记录Mapper接口
 * 
 * @author lchbu
 * @date 2023-03-03
 */
@Repository
public interface WarnEarlyRecordMapper 
{
    /**
     * 查询周期预警记录
     * 
     * @param id 周期预警记录主键
     * @return 周期预警记录
     */
    public WarnEarlyRecord selectWarnEarlyRecordById(Long id);

    /**
     * 查询周期预警记录列表
     * 
     * @param warnEarlyRecord 周期预警记录
     * @return 周期预警记录集合
     */
    public List<WarnEarlyRecord> selectWarnEarlyRecordList(WarnEarlyRecord warnEarlyRecord);

    public List<WarnEarlyRecord> selectWarnLeaveMessageByRecordIds(@Param("recordIds") List<Long> recordIds);



    public List<WarnEarlyRecord> selectListByRecordId(WarnEarlyRecord warnEarlyRecord);
    public List<WarnEarlyRecord> selectWarnEarlyRecordListByRecordId( Long recordId);

    /**
     * 新增周期预警记录
     * 
     * @param warnEarlyRecord 周期预警记录
     * @return 结果
     */
    public int insertWarnEarlyRecord(WarnEarlyRecord warnEarlyRecord);

    /**
     * 修改周期预警记录
     * 
     * @param warnEarlyRecord 周期预警记录
     * @return 结果
     */
    public int updateWarnEarlyRecord(WarnEarlyRecord warnEarlyRecord);

    /**
     * 删除周期预警记录
     * 
     * @param id 周期预警记录主键
     * @return 结果
     */
    public int deleteWarnEarlyRecordById(Long id);

    public int deleteByRecordId(Long recordId);

    /**
     * 批量删除周期预警记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWarnEarlyRecordByIds(Long[] ids);


    public int batchInsert(List<WarnEarlyRecord> list);

    List<WarnConditionValueVO> selectConditionAndValueByRecordId(Long recordId);
}
