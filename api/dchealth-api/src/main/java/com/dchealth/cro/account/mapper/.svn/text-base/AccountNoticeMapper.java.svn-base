package com.dchealth.cro.account.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dchealth.cro.account.domain.AccountNotice;
import com.dchealth.cro.account.form.MessageSearchForm;

/**
 * 用户通知Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2020-01-02
 */
@Repository
public interface AccountNoticeMapper 
{
    /**
     * 查询用户通知
     * 
     * @param id 用户通知ID
     * @return 用户通知
     */
    public AccountNotice selectAccountNoticeById(Long id);

    /**
     * 查询用户通知列表
     * 
     * @param accountNotice 用户通知
     * @return 用户通知集合
     */
    public List<AccountNotice> selectAccountNoticeList(AccountNotice accountNotice);

    /**
     * 新增用户通知
     * 
     * @param accountNotice 用户通知
     * @return 结果
     */
    public int insertAccountNotice(AccountNotice accountNotice);

    /**
     * 修改用户通知
     * 
     * @param accountNotice 用户通知
     * @return 结果
     */
    public int updateAccountNotice(AccountNotice accountNotice);

    /**
     * 删除用户通知
     * 
     * @param id 用户通知ID
     * @return 结果
     */
    public int deleteAccountNoticeById(Long id);

    /**
     * 批量删除用户通知
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAccountNoticeByIds(String[] ids);
    /**
     * 更新已读
     * @param ids
     * @return
     */
    public int batchUpdateMessageStatus(List<Long> list);

    public int updateReadById(Long id);
    
    List<AccountNotice> selectAccountMessage(MessageSearchForm  messageSearchForm);

    List<AccountNotice> selectAccountMessageNew(MessageSearchForm  messageSearchForm);


    public Integer getDoctorPersonalCenterRedDotVoNum(Long accountId);

    public Integer getPatientPersonalCenterRedDotVoNum(Long accountId);

    public Integer getPatientPersonalCenterRedDotVoNum2(Long accountId);


}
