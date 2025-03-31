package com.dchealth.cro.researchPatient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dchealth.cro.common.exception.base.ServiceException;
import com.dchealth.cro.researchPatient.domain.QuitRecord;

/**
 * 退出队列服务
 * @author zhangxiao
 */
public interface QuitRecordService extends IService<QuitRecord> {
    /**
     * 保存退出记录信息
     * 该方法使用了事务管理，确保数据的一致性
     * 当方法执行过程中出现Exception类及其子类异常时，事务将会回滚
     * 这个方法主要是为了保存退出记录，并更新患者的离院状态
     *
     * @param quitRecord 退出记录对象，包含需要保存的退出相关信息
     * @return 返回一个布尔值，表示退出记录是否成功保存并且患者状态成功更新
     *         如果插入和更新操作都成功，则返回true，否则返回false
     */
    boolean saveInfo(QuitRecord quitRecord) throws ServiceException;
}
