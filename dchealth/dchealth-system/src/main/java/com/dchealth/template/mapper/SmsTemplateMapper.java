package com.dchealth.template.mapper;

import com.dchealth.template.domain.SmsTemplate;

import java.util.List;


public interface SmsTemplateMapper{
    /**
     * 查询短信模板
     * 
     * @param id 短信模板主键
     * @return 短信模板
     */
    public SmsTemplate selectSmsTemplateById(Long id);

    public SmsTemplate selectSmsTemplateByName(String templateName);

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

    /**
     * 修改短信模板
     * 
     * @param smsTemplate 短信模板
     * @return 结果
     */
    public int updateSmsTemplate(SmsTemplate smsTemplate);

    /**
     * 删除短信模板
     * 
     * @param id 短信模板主键
     * @return 结果
     */
    public int deleteSmsTemplateById(Long id);

    /**
     * 批量删除短信模板
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSmsTemplateByIds(Long[] ids);
}
