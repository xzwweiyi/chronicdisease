package com.dchealth.template.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.template.domain.SmsTemplate;
import com.dchealth.template.mapper.SmsTemplateMapper;
import com.dchealth.template.service.ISmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SmsTemplateServiceImpl implements ISmsTemplateService {
    @Autowired
    private SmsTemplateMapper smsTemplateMapper;

    /**
     * 查询短信模板
     * 
     * @param id 短信模板主键
     * @return 短信模板
     */
    @Override
    public SmsTemplate selectSmsTemplateById(Long id)
    {
        return smsTemplateMapper.selectSmsTemplateById(id);
    }

    /**
     * 查询短信模板列表
     * 
     * @param smsTemplate 短信模板
     * @return 短信模板
     */
    @Override
    public List<SmsTemplate> selectSmsTemplateList(SmsTemplate smsTemplate){
        return smsTemplateMapper.selectSmsTemplateList(smsTemplate);
    }

    /**
     * 新增短信模板
     * 
     * @param smsTemplate 短信模板
     * @return 结果
     */
    @Override
    public int insertSmsTemplate(SmsTemplate smsTemplate){
        smsTemplate.setCreateTime(DateUtils.getNowDate());
        return smsTemplateMapper.insertSmsTemplate(smsTemplate);
    }

    @Override
    public SmsTemplate selectSmsTemplateByType(String type) {
        if("1".equals(type)){
            return smsTemplateMapper.selectSmsTemplateByName("随访");
        }else if("2".equals(type)){
            return smsTemplateMapper.selectSmsTemplateByName("超窗");
        }else if("3".equals(type)){
            return smsTemplateMapper.selectSmsTemplateByName("预警");
        }
        return new SmsTemplate();
    }

    /**
     * 修改短信模板
     * 
     * @param smsTemplate 短信模板
     * @return 结果
     */
    @Override
    public int updateSmsTemplate(SmsTemplate smsTemplate){
        smsTemplate.setUpdateTime(DateUtils.getNowDate());
        return smsTemplateMapper.updateSmsTemplate(smsTemplate);
    }

    /**
     * 批量删除短信模板
     * 
     * @param ids 需要删除的短信模板主键
     * @return 结果
     */
    @Override
    public int deleteSmsTemplateByIds(Long[] ids)
    {
        return smsTemplateMapper.deleteSmsTemplateByIds(ids);
    }

    /**
     * 删除短信模板信息
     * 
     * @param id 短信模板主键
     * @return 结果
     */
    @Override
    public int deleteSmsTemplateById(Long id)
    {
        return smsTemplateMapper.deleteSmsTemplateById(id);
    }
}
