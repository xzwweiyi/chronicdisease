package com.dchealth.medical.service;

import com.dchealth.medical.domain.QuitRecord;
import com.dchealth.medical.domain.form.QuitRecordForm;

import java.util.List;

/**
 * 退出队列审核服务
 * @author zhangxiao
 */
public interface IQuitRecordService {
    /**
     *
     * @param quitRecordForm
     * @return
     */
    List<QuitRecord> findList(QuitRecordForm quitRecordForm);

    /**
     * 更新退出申请记录
     * 根据同意与否设置相应的时间戳
     *
     * @param quitRecord 退出申请记录对象，包含是否同意退出申请的标志及退出申请记录的其他信息
     * @return 更新操作的影响行数，通常为1表示成功更新，0表示未找到更新的对象
     */
    Integer updateQuitRecord(QuitRecord quitRecord);
}
