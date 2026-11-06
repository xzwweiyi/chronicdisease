package com.dchealth.account.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.account.mapper.AccountMapper;
import com.dchealth.account.domain.Account;
import com.dchealth.account.service.IAccountService;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-03-28
 */
@Service
public class AccountServiceImpl implements IAccountService 
{
    @Autowired
    private AccountMapper accountMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public Account selectAccountById(String id)
    {
        return accountMapper.selectAccountById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param account 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<Account> selectAccountList(Account account)
    {
        return accountMapper.selectAccountList(account);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param account 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertAccount(Account account)
    {
        account.setCreateTime(DateUtils.getNowDate());
        return accountMapper.insertAccount(account);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param account 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateAccount(Account account)
    {
        account.setUpdateTime(DateUtils.getNowDate());
        return accountMapper.updateAccount(account);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteAccountByIds(String[] ids)
    {
        return accountMapper.deleteAccountByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteAccountById(String id)
    {
        return accountMapper.deleteAccountById(id);
    }
}
