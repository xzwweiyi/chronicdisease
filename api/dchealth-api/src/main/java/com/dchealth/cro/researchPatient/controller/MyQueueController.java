package com.dchealth.cro.researchPatient.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.researchPatient.domain.MyQueue;
import com.dchealth.cro.researchPatient.domain.QuitRecord;
import com.dchealth.cro.researchPatient.service.MyQueueService;
import com.dchealth.cro.researchPatient.service.QuitRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 我的队列
 * @author zhangxiao
 */
@RestController
@RequestMapping(value = "/myQueue")
public class MyQueueController extends ApiBaseController {
    private static final Logger log = LoggerFactory.getLogger(MyQueueController.class);

    private final MyQueueService queueService;

    private final QuitRecordService quitRecordService;

    public MyQueueController(MyQueueService queueService, QuitRecordService quitRecordService) {
        this.queueService = queueService;
        this.quitRecordService = quitRecordService;
    }

    /**
     * 根据personId查询列表信息
     * 此方法解释了如何通过个人ID获取其对应的队列列表它使用GET请求获取数据，
     * 并通过构建成功的响应实体返回列表信息
     *
     * @param personId 要查询的人员ID，此参数是必需的
     * @return 返回一个构建好的成功响应实体，包含查询到的队列列表
     */
    @GetMapping("findList")
    public String findList(@RequestParam(value = "personId", required = true) Long personId) {
        // 初始化响应实体
        DchealthCloudResponseEntity<List<MyQueue>> result = new DchealthCloudResponseEntity<>();
        // 调用服务层方法，根据personId查询队列列表
        List<MyQueue> list = queueService.findListByPersonId(personId);
        // 将查询到的列表设置到响应实体中
        result.setData(list);
        // 构建并返回一个包含成功信息和队列列表的响应实体
        return buildSuccessMView(result);
    }

    /**
     * 处理保存退出队列的提交请求
     *
     * @param quitRecord 离职记录对象，包含保存退出队列的相关信息
     * @return 返回表示操作结果的字符串
     */
    @PostMapping("applyQuitQueue")
    public String applyQuitQueue(@RequestBody QuitRecord quitRecord) {
        // 创建响应实体对象，用于封装返回结果
        DchealthCloudResponseEntity<String> result = new DchealthCloudResponseEntity<>();
        try {
            // 调用服务层方法，尝试保存退出队列记录
            boolean save = quitRecordService.saveInfo(quitRecord);

            // 根据保存结果，构建并返回相应的响应视图
            if(save) {
                // 如果保存成功，构建并返回成功视图
                result.setCode(CroApiCodeConstant.APPLY_SUCCESS.getCode());
                return buildSuccessMView(result);
            } else {
                // 如果保存失败，构建并返回错误视图
                return buildErrorMView(CroApiCodeConstant.FAIL.getCode());
            }
        } catch (Exception e) {
            return buildErrorParamMView(e);
        }
    }


    /**
     * 根据个人ID和队列ID查找退出记录列表
     *
     * @param personId 个人ID，用于筛选退出记录
     * @param queueId 队列ID，用于筛选退出记录
     * @return 返回包含退出记录列表的响应实体
     */
    @GetMapping("findQuitList")
    public String findQuitList(@RequestParam(value = "personId", required = true) Long personId,
                               @RequestParam(value = "queueId", required = true) Long queueId) {
        // 初始化响应实体，用于封装查询结果
        DchealthCloudResponseEntity<List<QuitRecord>> result = new DchealthCloudResponseEntity<>();

        // 创建查询条件构造器
        LambdaQueryWrapper<QuitRecord> wrapper = new LambdaQueryWrapper<>();

        // 设置查询条件：根据个人ID和研究ID筛选退出记录
        wrapper.eq(QuitRecord::getPersonId, personId);
        wrapper.eq(QuitRecord::getQueueId, queueId);

        // 执行查询并获取结果
        List<QuitRecord> quitRecords = quitRecordService.list(wrapper);

        // 将查询结果封装到响应实体中
        result.setData(quitRecords);

        // 构建并返回成功响应的视图
        return buildSuccessMView(result);
    }

}
