package com.dchealth.web.job;

import com.alibaba.fastjson2.JSON;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.common.utils.SplitListUtils;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.research.domain.ResearchInfo;
import com.dchealth.system.domain.PatientCardInfo;
import com.dchealth.system.service.IPatientCardInfoService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class PatientCardInfoJob {

    @Autowired
    public IPatientCardInfoService iPatientCardInfoService;

    /**
     * 选举主号
     * @throws Exception
     */
    @XxlJob("syncToMasterTable")
    public void syncToMasterTable() throws Exception {
        XxlJobHelper.log("选举主号任务日期{}开始", DateUtils.getTime());
        iPatientCardInfoService.updateMasterPeson();
        XxlJobHelper.handleSuccess();
    }

    /**
     * 选举主号
     * @throws Exception
     */
    @XxlJob("selectMasterPersonJob")
    public void selectMasterPersonJob() throws Exception {
        XxlJobHelper.log("选举主号任务日期{}开始", DateUtils.getTime());
        iPatientCardInfoService.updateMasterPeson();
        XxlJobHelper.handleSuccess();
    }

    /**
     * 更新所有的就诊号id
     * @throws Exception
     */
    @XxlJob("updatePatientAllPersonIdJob")
    public void updatePatientAllPersonIdJob() throws Exception {
        XxlJobHelper.log("更新personid任务日期{}开始", DateUtils.getTime());
        Map<String, Integer> stringIntegerMap = iPatientCardInfoService.updatePatientAllPersonId();
        XxlJobHelper.handleSuccess(JSON.toJSONString(stringIntegerMap));
    }
}
