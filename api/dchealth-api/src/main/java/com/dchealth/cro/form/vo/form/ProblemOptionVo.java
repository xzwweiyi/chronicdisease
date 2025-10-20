package com.dchealth.cro.form.vo.form;


import lombok.Data;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/10/16 12:15
 * @Version: 1.0
 */
@Data
public class ProblemOptionVo {
    private String label;
    private String value;
    private Boolean isShowInputValue;
    private String inputType;
    private Integer subjectScores;
    private Integer itemScores;
    private Boolean disabled;
}
