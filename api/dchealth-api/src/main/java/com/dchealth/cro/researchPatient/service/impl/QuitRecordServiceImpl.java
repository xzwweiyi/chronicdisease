package com.dchealth.cro.researchPatient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dchealth.cro.common.exception.base.ServiceException;
import com.dchealth.cro.researchPatient.domain.QuitRecord;
import com.dchealth.cro.researchPatient.mapper.QuitRecordMapper;
import com.dchealth.cro.researchPatient.service.QuitRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 退出队列列表服务实现类
 * @author Administrator
 */
@Service
public class QuitRecordServiceImpl extends ServiceImpl<QuitRecordMapper, QuitRecord> implements QuitRecordService {

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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInfo(QuitRecord quitRecord) throws ServiceException {
        LambdaQueryWrapper<QuitRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuitRecord::getQueueId, quitRecord.getQueueId());
        queryWrapper.eq(QuitRecord::getPersonId, quitRecord.getPersonId());
        queryWrapper.isNull(QuitRecord::getIsAgree);
        List<QuitRecord> records = baseMapper.selectList(queryWrapper);
        if(!CollectionUtils.isEmpty(records)) {
            throw new ServiceException("该患者已存在未处理的退出记录，请先处理该记录!", 500);
        }

        // 插入退出记录到数据库
        int insertCount = baseMapper.insert(quitRecord);
        // 更新患者离院状态
        int updateCount = baseMapper.updatePatientStatus(quitRecord);
        // 只有当插入和更新操作都成功时，才返回true
        return insertCount > 0 && updateCount > 0;
    }
}
