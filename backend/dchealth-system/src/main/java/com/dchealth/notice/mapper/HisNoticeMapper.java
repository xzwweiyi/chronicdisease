package com.dchealth.notice.mapper;

import com.dchealth.notice.noticevo.HisNoticeVo;

import java.util.List;

public interface HisNoticeMapper {

    int batchInsert(List<HisNoticeVo> hisNoticeVo);

}
