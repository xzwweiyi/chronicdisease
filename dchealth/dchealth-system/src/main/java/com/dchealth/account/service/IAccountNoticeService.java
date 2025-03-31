package com.dchealth.account.service;

import com.dchealth.account.domain.AccountNotice;

import java.util.List;


public interface IAccountNoticeService{
    /**
     * 查询消息通知
     * 
     * @param id 消息通知主键
     * @return 消息通知
     */
    public AccountNotice selectAccountNoticeById(Long id);

    /**
     * 查询消息通知列表
     * 
     * @param accountNotice 消息通知
     * @return 消息通知集合
     */
    public List<AccountNotice> selectAccountNoticeList(AccountNotice accountNotice);

    /**
     * 新增消息通知
     * 
     * @param accountNotice 消息通知
     * @return 结果
     */
    public int insertAccountNotice(AccountNotice accountNotice);

    /**
     * 修改消息通知
     * 
     * @param accountNotice 消息通知
     * @return 结果
     */
    public int updateAccountNotice(AccountNotice accountNotice);

    /**
     * 批量删除消息通知
     * 
     * @param ids 需要删除的消息通知主键集合
     * @return 结果
     */
    public int deleteAccountNoticeByIds(Long[] ids);

    /**
     * 删除消息通知信息
     * 
     * @param id 消息通知主键
     * @return 结果
     */
    public int deleteAccountNoticeById(Long id);
}
