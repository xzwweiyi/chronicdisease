package com.dchealth.medical.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.medical.domain.AppLoginRecord;
import com.dchealth.medical.mapper.AppLoginRecordMapper;
import com.dchealth.medical.service.IAppLoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AppLoginRecordServiceImpl implements IAppLoginRecordService {
    @Autowired
    private AppLoginRecordMapper appLoginRecordMapper;

    /**
     * 查询app登录记录
     * 
     * @param id app登录记录主键
     * @return app登录记录
     */
    @Override
    public AppLoginRecord selectAppLoginRecordById(Long id){
        return appLoginRecordMapper.selectAppLoginRecordById(id);
    }

    /**
     * 查询app登录记录列表
     * 
     * @param appLoginRecord app登录记录
     * @return app登录记录
     */
    @Override
    public List<AppLoginRecord> selectAppLoginRecordList(AppLoginRecord appLoginRecord){
        return appLoginRecordMapper.selectAppLoginRecordList(appLoginRecord);
    }

    /**
     * 新增app登录记录
     * 
     * @param appLoginRecord app登录记录
     * @return 结果
     */
    @Override
    public int insertAppLoginRecord(AppLoginRecord appLoginRecord){
        appLoginRecord.setCreateTime(DateUtils.getNowDate());
        return appLoginRecordMapper.insertAppLoginRecord(appLoginRecord);
    }

    /**
     * 修改app登录记录
     * 
     * @param appLoginRecord app登录记录
     * @return 结果
     */
    @Override
    public int updateAppLoginRecord(AppLoginRecord appLoginRecord){
        return appLoginRecordMapper.updateAppLoginRecord(appLoginRecord);
    }

    /**
     * 批量删除app登录记录
     * 
     * @param ids 需要删除的app登录记录主键
     * @return 结果
     */
    @Override
    public int deleteAppLoginRecordByIds(Long[] ids)
    {
        return appLoginRecordMapper.deleteAppLoginRecordByIds(ids);
    }

    /**
     * 删除app登录记录信息
     * 
     * @param id app登录记录主键
     * @return 结果
     */
    @Override
    public int deleteAppLoginRecordById(Long id)
    {
        return appLoginRecordMapper.deleteAppLoginRecordById(id);
    }
}
