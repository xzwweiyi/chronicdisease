package com.dchealth.web.controller.obesity;


import cn.hutool.core.util.ObjectUtil;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.domain.entity.SysDictData;
import com.dchealth.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/9/18 15:17
 * @Version: 1.0
 */
@RestController
@RequestMapping("/obesity")
public class ObesityController {

    @Autowired
    private ISysDictDataService sysDictDataService;

    @GetMapping("/isShow/{researchId}")
    public AjaxResult isShow(@PathVariable("researchId") String researchId){

//        if (ObjUtil.isEmpty(researchId)) {
//            // 如果是肥胖门诊直接返回true否则返回false
//            Long deptId = SecurityUtils.getDeptId();
//
//
//
//        }

        SysDictData sysDictData = new SysDictData();
        sysDictData.setDictType("research");
        List<SysDictData> dictDataList = sysDictDataService.selectDictDataList(sysDictData);
        if (ObjectUtil.isEmpty(dictDataList)) {
            return AjaxResult.success(Boolean.FALSE);
        }
        List<String> result = dictDataList.stream().map(SysDictData::getDictValue).collect(Collectors.toList());
        return AjaxResult.success(result.contains(researchId)) ;
    }
}
