package com.dchealth.warn.mapper;

import com.dchealth.statistics.domain.StatisticsParam;
import com.dchealth.warn.domain.WarnEarlyRecord;
import com.dchealth.warn.domain.vo.WarnConditionValueVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 周期预警记录Mapper接口
 * 
 * @author lchbu
 * @date 2023-03-03
 */
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


    public List<WarnEarlyRecord> selectListByRecordId(WarnEarlyRecord warnEarlyRecord);

    /**
     * 新增周期预警记录
     * 
     * @param warnEarlyRecord 周期预警记录
     * @return 结果
     */
    public int insertWarnEarlyRecord(WarnEarlyRecord warnEarlyRecord);



    public int batchInsert(@Param("list") List<WarnEarlyRecord> warnEarlyRecordList);

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

    List<WarnConditionValueVO> selectConditionAndValueByRecordId(Long recordId);

    List<WarnEarlyRecord> selectWarnEarlyRecordByIdList(@Param("untreatedWarnIds") List<Long> untreatedWarnIds,@Param("warnCreateDay") Date warnCreateDay);


    public WarnEarlyRecord selectOldOneByRecordId(Long id);

    /**
     * 查询预警数量
     * @param statisticsParam 统计参数
     * @return 预警数量
     */
    Integer selectWarnCount(StatisticsParam statisticsParam);
}
