package com.dchealth.account.service.impl;

import java.util.List;

import com.dchealth.account.domain.AccountNotice;
import com.dchealth.account.mapper.AccountNoticeMapper;
import com.dchealth.account.service.IAccountNoticeService;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AccountNoticeServiceImpl implements IAccountNoticeService {
    @Autowired
    private AccountNoticeMapper accountNoticeMapper;

    /**
     * 查询消息通知
     * 
     * @param id 消息通知主键
     * @return 消息通知
     */
    @Override
    public AccountNotice selectAccountNoticeById(Long id)
    {
        return accountNoticeMapper.selectAccountNoticeById(id);
    }

    /**
     * 查询消息通知列表
     * 
     * @param accountNotice 消息通知
     * @return 消息通知
     */
    @Override
    public List<AccountNotice> selectAccountNoticeList(AccountNotice accountNotice){
        return accountNoticeMapper.selectAccountNoticeList(accountNotice);
    }

    /**
     * 新增消息通知
     * 
     * @param accountNotice 消息通知
     * @return 结果
     */
    @Override
    public int insertAccountNotice(AccountNotice accountNotice){
        accountNotice.setCreateTime(DateUtils.getNowDate());
        return accountNoticeMapper.insertAccountNotice(accountNotice);
    }

    /**
     * 修改消息通知
     * 
     * @param accountNotice 消息通知
     * @return 结果
     */
    @Override
    public int updateAccountNotice(AccountNotice accountNotice){
        accountNotice.setUpdateTime(DateUtils.getNowDate());
        return accountNoticeMapper.updateAccountNotice(accountNotice);
    }

    /**
     * 批量删除消息通知
     * 
     * @param ids 需要删除的消息通知主键
     * @return 结果
     */
    @Override
    public int deleteAccountNoticeByIds(Long[] ids)
    {
        return accountNoticeMapper.deleteAccountNoticeByIds(ids);
    }

    /**
     * 删除消息通知信息
     * 
     * @param id 消息通知主键
     * @return 结果
     */
    @Override
    public int deleteAccountNoticeById(Long id)
    {
        return accountNoticeMapper.deleteAccountNoticeById(id);
    }
}
