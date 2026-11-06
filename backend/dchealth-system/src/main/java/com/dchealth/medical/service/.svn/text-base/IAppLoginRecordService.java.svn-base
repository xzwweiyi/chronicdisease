package com.dchealth.medical.service;

import com.dchealth.medical.domain.AppLoginRecord;

import java.util.List;


public interface IAppLoginRecordService{
    /**
     * 查询app登录记录
     * 
     * @param id app登录记录主键
     * @return app登录记录
     */
    public AppLoginRecord selectAppLoginRecordById(Long id);

    /**
     * 查询app登录记录列表
     * 
     * @param appLoginRecord app登录记录
     * @return app登录记录集合
     */
    public List<AppLoginRecord> selectAppLoginRecordList(AppLoginRecord appLoginRecord);

    /**
     * 新增app登录记录
     * 
     * @param appLoginRecord app登录记录
     * @return 结果
     */
    public int insertAppLoginRecord(AppLoginRecord appLoginRecord);

    /**
     * 修改app登录记录
     * 
     * @param appLoginRecord app登录记录
     * @return 结果
     */
    public int updateAppLoginRecord(AppLoginRecord appLoginRecord);

    /**
     * 批量删除app登录记录
     * 
     * @param ids 需要删除的app登录记录主键集合
     * @return 结果
     */
    public int deleteAppLoginRecordByIds(Long[] ids);

    /**
     * 删除app登录记录信息
     * 
     * @param id app登录记录主键
     * @return 结果
     */
    public int deleteAppLoginRecordById(Long id);
}
