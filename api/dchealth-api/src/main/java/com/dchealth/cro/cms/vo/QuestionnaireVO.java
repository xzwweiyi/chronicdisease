package com.dchealth.cro.cms.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionnaireVO {

    private List<ArticleListVO> questionnaireVoList = new ArrayList<>();

    private Integer questionnaireNum;
}
