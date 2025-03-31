package com.dchealth.cro.researchPatient.service.impl;

import com.dchealth.cro.researchPatient.domain.MyQueue;
import com.dchealth.cro.researchPatient.mapper.MyQueueMapper;
import com.dchealth.cro.researchPatient.service.MyQueueService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 我的队列服务
 * @author zhangxiao
 */
@Service
public class MyQueueServiceImpl implements MyQueueService {


    private final MyQueueMapper myQueueMapper;

    public MyQueueServiceImpl(MyQueueMapper myQueueMapper) {
        this.myQueueMapper = myQueueMapper;
    }

    /**
     * 队列状态 已关闭
     */
    private static final Integer QUEUE_FINISH = 2;
    /**
     * 队列状态 进行中
     */
    private static final Integer QUEUE_DOING = 1;
    /**
     * 患者状态 已完成
     */
    private static final Integer PATIENT_FINISH = 3;
    /**
     * 患者状态 失访
     */
    private static final Integer PATIENT_LOSS_FOLLOW = 4;
    /**
     * 患者状态 申请退出队列
     */
    private static final Integer PATIENT_QUIT_APPLY_FOLLOW = 5;
    /**
     * 患者状态 已退出队列
     */
    private static final Integer PATIENT_QUIT_FOLLOW = 6;

    /**
     * 根据个人ID查找队列列表
     * 此方法旨在为特定个人ID获取相关的队列列表
     *
     * @param personId 个人的唯一标识符
     * @return 包含MyQueue对象的列表如果找不到匹配的队列，返回空列表
     */
    @Override
    public List<MyQueue> findListByPersonId(Long personId) {
        List<MyQueue> list = myQueueMapper.findListByPersonId(personId);
        for (MyQueue myQueue : list) {
            if(QUEUE_FINISH.equals(myQueue.getQueueStatus())) {
                myQueue.setStatus("已关闭");
            } else {
                if(myQueue.getNoAuditCount()) {
                    myQueue.setStatus("申请中");
                    continue;
                } else if(myQueue.getAgreeCount()) {
                    myQueue.setStatus("已退出");
                    continue;
                }
                if(PATIENT_FINISH.equals(myQueue.getPatientStatus())) {
                    myQueue.setStatus("已完成");
                } else if(PATIENT_LOSS_FOLLOW.equals(myQueue.getPatientStatus())) {
                    myQueue.setStatus("已失访");
                } else if(PATIENT_QUIT_APPLY_FOLLOW.equals(myQueue.getPatientStatus())) {
                    myQueue.setStatus("申请中");
                } else if(PATIENT_QUIT_FOLLOW.equals(myQueue.getPatientStatus())) {
                    myQueue.setStatus("已退出");
                } else {
                    myQueue.setStatus("进行中");
                }
            }
        }
        return list;
    }
}
