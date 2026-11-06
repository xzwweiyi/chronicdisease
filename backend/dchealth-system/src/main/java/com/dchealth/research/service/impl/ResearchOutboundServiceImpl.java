package com.dchealth.research.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dchealth.common.constant.Constants;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.common.utils.http.XfServiceHttpClientUtil;
import com.dchealth.common.utils.http.XunfeiHttpClientUtil;
import com.dchealth.medical.vo.xunfei.SpeechListRes;
import com.dchealth.medical.vo.xunfei.SpeechVariableRes;
import com.dchealth.medical.vo.xunfei.Var;
import com.dchealth.research.domain.ResearchOutbound;
import com.dchealth.research.domain.ResearchXunfeiTemplate;
import com.dchealth.research.domain.ResearchXunfeiTemplateVar;
import com.dchealth.research.domain.vo.ResearchOutboundVo;
import com.dchealth.research.mapper.ResearchOutboundMapper;
import com.dchealth.research.mapper.ResearchXunfeiTemplateMapper;
import com.dchealth.research.mapper.ResearchXunfeiTemplateVarMapper;
import com.dchealth.research.service.IResearchOutboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ResearchOutboundServiceImpl implements IResearchOutboundService {
    @Autowired
    private ResearchOutboundMapper researchOutboundMapper;

    @Value("${xunfei.requestUrl}")
    private String baseUrl ;

    @Autowired
    private ResearchXunfeiTemplateMapper researchXunfeiTemplateMapper;

    @Autowired
    private ResearchXunfeiTemplateVarMapper researchXunfeiTemplateVarMapper;

    /**
     * 查询队列智能外呼设置
     * 
     * @param id 队列智能外呼设置主键
     * @return 队列智能外呼设置
     */
    @Override
    public ResearchOutbound selectResearchOutboundById(Long id)
    {
        return researchOutboundMapper.selectResearchOutboundById(id);
    }

    /**
     * 查询队列智能外呼设置列表
     * 
     * @param researchOutbound 队列智能外呼设置
     * @return 队列智能外呼设置
     */
    @Override
    public List<ResearchOutbound> selectResearchOutboundList(ResearchOutbound researchOutbound)
    {
        researchOutbound.setDelFlag("0");
        return researchOutboundMapper.selectResearchOutboundList(researchOutbound);
    }

    @Override
    public List<SpeechListRes> speechList() {
//        Map<String, String> maps = new HashMap<String, String>();
////        String result = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(Constants.XUNFEI_SPEECH_LIST, maps);
//        String result = XfServiceHttpClientUtil.getInstance().sendDefaultHttpPost(baseUrl+"xunfei/speechList", maps);
//        JSONObject jsonObject = JSON.parseObject(result);
//        List<SpeechListRes> list = JSONArray.parseArray(jsonObject.getString("data"), SpeechListRes.class);

        List<SpeechListRes> list = new ArrayList<>();
        ResearchXunfeiTemplate researchXunfeiTemplate = new ResearchXunfeiTemplate();
        researchXunfeiTemplate.setDelFlag("0");
        List<ResearchXunfeiTemplate> researchXunfeiTemplateList = researchXunfeiTemplateMapper.selectResearchXunfeiTemplateList(researchXunfeiTemplate);
        if(researchXunfeiTemplateList.size()>0){
            for(ResearchXunfeiTemplate p:researchXunfeiTemplateList){
                SpeechListRes res = new SpeechListRes();
                res.setId(p.getSpeechId());
                res.setIntro(p.getIntro());
                res.setName(p.getName());
                list.add(res);
            }
        }
        return list;
    }

    @Override
    public List<Var> speechDetailBySpeechId(String speechId) {
//        SpeechVariableRes speechVariable = new SpeechVariableRes();
//        speechVariable.setSpeechId(speechId);
////        String result = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(Constants.XUNFEI_SPEECH_VARIABLE, speechVariable);
//        String result = XfServiceHttpClientUtil.getInstance().sendDefaultHttpPost(baseUrl+"xunfei/speechVariable", speechVariable);
//        JSONObject jsonObject = JSON.parseObject(result);
//        List<Var> list = JSONArray.parseArray(jsonObject.getString("data"),Var.class);
        List<Var> list = new ArrayList<>();
        ResearchXunfeiTemplateVar researchXunfeiTemplateVar = new ResearchXunfeiTemplateVar();
        researchXunfeiTemplateVar.setDelFlag("0");
        researchXunfeiTemplateVar.setSpeechId(Long.parseLong(speechId));
        List<ResearchXunfeiTemplateVar> varList = researchXunfeiTemplateVarMapper.selectResearchXunfeiTemplateVarList(researchXunfeiTemplateVar);
        if(varList.size()>0){
            for(ResearchXunfeiTemplateVar p:varList){
                Var var = new Var();
                var.setOriginal(p.getOriginal());
                var.setLimit(p.getLimits());
                var.setContent(p.getContent());
                var.setRemarks(p.getRemarks());
                list.add(var);
            }
        }
        return list;
    }


    @Override
    public String speechTemplateImage(String speechId) {
        ResearchXunfeiTemplate researchXunfeiTemplate = researchXunfeiTemplateMapper.selectBySpeedId(speechId);
        if(researchXunfeiTemplate!=null){
            return researchXunfeiTemplate.getImageUrl();
        }
        return "";
    }

    /**
     * 新增队列智能外呼设置
     * 
     * @param researchOutbound 队列智能外呼设置
     * @return 结果
     */
    @Override
    public int insertResearchOutbound(ResearchOutbound researchOutbound)
    {
        researchOutbound.setCreateTime(DateUtils.getNowDate());
        return researchOutboundMapper.insertResearchOutbound(researchOutbound);
    }

    @Override
    @Transactional
    public int add(ResearchOutboundVo researchOutboundVo) {
        if(researchOutboundVo.getResearchOutboundList().size()>0){
            List<ResearchOutbound> list = researchOutboundVo.getResearchOutboundList();
            Long researchId = list.get(0).getResearchId();
            researchOutboundMapper.updateSetDelByResearchId(researchId);
            for(ResearchOutbound ro:list){
                if(ro.getId()!=null){
                    ro.setUpdateTime(DateUtils.getNowDate());
                    ro.setUpdateBy(researchOutboundVo.getUpdateBy());
                    ro.setDelFlag("0");
                    researchOutboundMapper.updateResearchOutbound2(ro);
                }else{
                    ro.setCreateTime(DateUtils.getNowDate());
                    ro.setCreateBy(researchOutboundVo.getUpdateBy());
                    ro.setDelFlag("0");
                    researchOutboundMapper.insertResearchOutbound(ro);
                }
            }
        }
        return 1;
    }

    /**
     * 修改队列智能外呼设置
     * 
     * @param researchOutbound 队列智能外呼设置
     * @return 结果
     */
    @Override
    public int updateResearchOutbound(ResearchOutbound researchOutbound)
    {
        researchOutbound.setUpdateTime(DateUtils.getNowDate());
        return researchOutboundMapper.updateResearchOutbound(researchOutbound);
    }

    /**
     * 批量删除队列智能外呼设置
     * 
     * @param ids 需要删除的队列智能外呼设置主键
     * @return 结果
     */
    @Override
    public int deleteResearchOutboundByIds(Long[] ids)
    {
        return researchOutboundMapper.deleteResearchOutboundByIds(ids);
    }

    /**
     * 删除队列智能外呼设置信息
     * 
     * @param id 队列智能外呼设置主键
     * @return 结果
     */
    @Override
    public int deleteResearchOutboundById(Long id)
    {
        return researchOutboundMapper.deleteResearchOutboundById(id);
    }



}
