package com.dchealth.medical.service.impl;

import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.QuitRecord;
import com.dchealth.medical.domain.form.QuitRecordForm;
import com.dchealth.medical.mapper.QuitRecordMapper;
import com.dchealth.medical.service.IQuitRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * 退出队列服务
 * @author zhangxiao
 */
@Service
public class QuitRecordServiceImpl implements IQuitRecordService {


    private final QuitRecordMapper quitRecordMapper;;

    public QuitRecordServiceImpl(QuitRecordMapper quitRecordMapper) {
        this.quitRecordMapper = quitRecordMapper;
    }

    /**
     * 根据查询条件获取退出申请记录列表
     *
     * @param quitRecordForm 查询条件对象，包含查询所需的参数
     * @return 匹配查询条件的记录列表
     */
    @Override
    public List<QuitRecord> findList(QuitRecordForm quitRecordForm) {
        return quitRecordMapper.findList(quitRecordForm);
    }

    /**
     * 更新退出申请记录
     * 根据同意与否设置相应的时间戳
     *
     * @param quitRecord 退出申请记录对象，包含是否同意退出申请的标志及退出申请记录的其他信息
     * @return 更新操作的影响行数，通常为1表示成功更新，0表示未找到更新的对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateQuitRecord(QuitRecord quitRecord) {
        // 如果同意退会，则设置同意时间
        if(quitRecord.getIsAgree()) {
            quitRecord.setAgreeTime(new Date());
            // 更新患者的退出状态（退出队列patient_status = 6）
            quitRecordMapper.updatePatientStatus(quitRecord.getResearchId(), quitRecord.getPersonId());
            // 根据研究ID和人员ID查询患者记录
            List<PatientRecord> records = quitRecordMapper.findRecord(quitRecord.getResearchId(), quitRecord.getPersonId());
            // 如果查询到的记录列表不为空，则遍历并移除这些记录
            if(!CollectionUtils.isEmpty(records)) {
                for (PatientRecord record : records) {
                    quitRecordMapper.removeRecordOutbound(record);
                    //删除当天生成的未填写的随访记录
                    quitRecordMapper.removeNowDayRecord(record);
                }
            }
        } else {
            // 如果不同意退会，则设置拒绝时间
            quitRecord.setRejectTime(new Date());
            quitRecordMapper.recoverPatientStatus(quitRecord.getResearchId(), quitRecord.getPersonId());
        }
        // 调用Mapper方法更新数据库中的退出随访记录
        return quitRecordMapper.updateQuitRecord(quitRecord);
    }
}
