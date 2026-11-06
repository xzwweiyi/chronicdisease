package com.dchealth.medical.vo.xunfei;

import lombok.Data;

import java.util.List;


@Data
public class XunfeiRes {

    //批次集合
    private String[] batch;

    private List<TaskRelationVo> taskRelation;

    //过滤的居民信息
    private List<FilterDweller> filterDwellers;

}
