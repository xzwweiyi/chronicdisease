package com.dchealth.resident.mapper;

import java.util.List;
import com.dchealth.resident.domain.ResidentGradingRecord;

/**
 * 分级评估Mapper接口
 */
public interface ResidentGradingMapper {
    List<ResidentGradingRecord> selectGradingRecordList(ResidentGradingRecord record);
    List<ResidentGradingRecord> selectGradingRecordByPersonId(String personId);
    ResidentGradingRecord selectGradingRecordById(Long id);
    int insertGradingRecord(ResidentGradingRecord record);
    int updateGradingRecord(ResidentGradingRecord record);
    int deleteGradingRecordById(Long id);
    int deleteGradingRecordByIds(Long[] ids);
}
