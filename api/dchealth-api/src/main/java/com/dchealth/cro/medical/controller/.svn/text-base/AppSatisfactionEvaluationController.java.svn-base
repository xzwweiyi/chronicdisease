package com.dchealth.cro.medical.controller;



import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.medical.domain.AppSatisfactionEvaluation;
import com.dchealth.cro.medical.service.AppSatisfactionEvaluationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author app满意度评价
 * @since 2023/5/26
 */
@RestController
@RequestMapping("/evaluation")
@Slf4j
public class AppSatisfactionEvaluationController extends ApiBaseController {

    @Autowired
    AppSatisfactionEvaluationService appSatisfactionEvaluationService;
    /**
     * 评价记录列表
     * @param evaluation
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public String list(AppSatisfactionEvaluation evaluation){
        try {
            evaluation.setPageNo(evaluation.getPageNo()-1);
            return buildSuccessMView(appSatisfactionEvaluationService.queryByPage(evaluation,
                    PageRequest.of(evaluation.getPageNo(),evaluation.getPageSize())).getContent());
        } catch (Exception e) {
            log.info("评价列表查询异常", e);
            return buildExceptionMView();
        }
    }

    /**
     * 评价信息保存
     * @param evaluation
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(AppSatisfactionEvaluation evaluation){
        try {
            return buildSuccessMView(appSatisfactionEvaluationService.insert(evaluation));
        } catch (Exception e) {
            log.info("评价信息保存异常", e);
            return buildExceptionMView();
        }
    }

}
