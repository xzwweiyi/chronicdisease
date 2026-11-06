package com.dchealth.warn.service;

import com.dchealth.warn.domain.form.WarnGuideMsgForm;
import com.dchealth.warn.domain.vo.WarnGuideMsgListVO;

import java.util.List;

/**
 * 预警指导
 */
public interface IWarnGuideService {


    /**
     * 指导聊天列表
     * @param recordId
     * @return
     */
    public List<WarnGuideMsgListVO> guideList(Long recordId);

    /**
     * 发送预警指导消息
     * @param warnGuideMsgForm
     * @return
     */
    public Long sendMsg(WarnGuideMsgForm warnGuideMsgForm);

    /**
     * 结束预警指导
     * @param recordId
     * @return
     */
    public int end(Long recordId,Long userId);

}
