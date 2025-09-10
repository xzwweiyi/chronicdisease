package com.dchealth.notice.service;

import com.dchealth.notice.noticevo.HisNoticeVo;

import java.util.List;

public interface HisNoticeService {

    int batchInsert(List<HisNoticeVo> hisNoticeVo);
}
