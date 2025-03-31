package com.dchealth.template.service;

import com.dchealth.template.domain.SmsTemplate;

import java.util.List;


public interface ISmsTemplateService{
    /**
     * 查询短信模板
     * 
     * @param id 短信模板主键
     * @return 短信模板
     */
    public SmsTemplate selectSmsTemplateById(Long id);

    /**
     * 查询短信模板列表
     * 
     * @param smsTemplate 短信模板
     * @return 短信模板集合
     */
    public List<SmsTemplate> selectSmsTemplateList(SmsTemplate smsTemplate);

    /**
     * 新增短信模板
     * 
     * @param smsTemplate 短信模板
     * @return 结果
     */
    public int insertSmsTemplate(SmsTemplate smsTemplate);

    public SmsTemplate selectSmsTemplateByType(String type);

    /**
     * 修改短信模板
     * 
     * @param smsTemplate 短信模板
     * @return 结果
     */
    public int updateSmsTemplate(SmsTemplate smsTemplate);

    /**
     * 批量删除短信模板
     * 
     * @param ids 需要删除的短信模板主键集合
     * @return 结果
     */
    public int deleteSmsTemplateByIds(Long[] ids);

    /**
     * 删除短信模板信息
     * 
     * @param id 短信模板主键
     * @return 结果
     */
    public int deleteSmsTemplateById(Long id);
}
