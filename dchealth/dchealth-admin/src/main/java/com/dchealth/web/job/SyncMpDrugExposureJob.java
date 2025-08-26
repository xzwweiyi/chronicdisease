package com.dchealth.web.job;


import com.dchealth.common.utils.StringUtils;
import com.dchealth.compatibility.domain.MpDrugExposure;
import com.dchealth.compatibility.mapper.MpDrugExposureMapper;
import com.dchealth.system.domain.RelMpDrugExposure;
import com.dchealth.system.mapper.RelMpDrugExposureMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/8/20 16:41
 * @Version: 1.0
 */
@Component
@Slf4j
public class SyncMpDrugExposureJob {

    private static final int BATCH_SIZE = 5000;

    @Autowired
    private RelMpDrugExposureMapper relMpDrugExposureMapper;

    @Autowired
    private MpDrugExposureMapper mpDrugExposureMapper;

    @Scheduled(cron = "0 0 6 * * ?")  // 每天上午8点执行
    private void syncMpDrugExposureHandler() throws Exception {

        // 将数据处理完成后同步到mp_drug_exposure表
        log.info("开始同步mp_drug_exposure表数据！");
        RelMpDrugExposure relMpDrugExposure = new RelMpDrugExposure();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        relMpDrugExposure.setDrugExposureStartDate(format.format(LocalDate.now().minusDays(1)));
        List<RelMpDrugExposure> relMpDrugExposures = relMpDrugExposureMapper.selectRelMpDrugExposureList(relMpDrugExposure);

        List<MpDrugExposure> mpDrugExposures = new ArrayList<>();
        partition(relMpDrugExposures, BATCH_SIZE).forEach(item -> {


            item.forEach(x -> {
                MpDrugExposure mpDrugExposure = new MpDrugExposure();
                try {
                    mpDrugExposure.setPersonId(x.getPersonId())
                            .setVisitOccurrenceId(x.getDrugSourceConceptId())
                            .setDrugExposureId(x.getDrugExposureId())
                            .setDrugType(x.getDrugType())
                            .setDosageForm(x.getDosageForm())
                            .setDrugValue(x.getDrugSourceValue())
                            .setDrugCode(x.getDrugConceptId())
                            .setDrugSpec(x.getEffectiveDrugDose())
                            .setDrugUnit(x.getDoseUnitConceptId())
                            .setDrugExposureStartDate(parseDate(x.getDrugExposureStartDate(), "yyyy-MM-dd HH:mm:ss"))

                            .setDepartmentName(x.getDepartmentName());

                    if (StringUtils.isNotBlank(x.getDaysSupply())) {
                        mpDrugExposure.setDaysSupply(Long.valueOf(x.getDaysSupply()));
                    }

                    mpDrugExposures.add(mpDrugExposure);
                } catch (ParseException e) {
                    log.error("时间转换异常！");
                    throw new RuntimeException(e);
                }

            });
            log.info("同步数据条数{}", mpDrugExposures.size());
            mpDrugExposureMapper.batchInsertMpDrugExposure(mpDrugExposures);
        });
    }


    //辅助方法： 带重试的批量插入
    private static <T> List<List<T>> partition(List<T> list, int size) {
        return IntStream.range(0, (list.size() + size - 1) / size)
                .mapToObj(i -> list.subList(i * size, Math.min((i + 1) * size, list.size())))
                .collect(Collectors.toList());
    }

    // 格式化时间
    public static Date parseDate(String dateString, String pattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(dateString);
    }


}
