package com.dchealth.research.mapper;

import com.dchealth.es.domain.form.TdHealthIndex;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface MpESDataQueryMapper {

    Integer queryDiagnosisInfosCount(Map params);
    Integer queryDiagnosisInfosCount2(Map params);

    List<TdHealthIndex> queryDiagnosisInfos(Map params);

    List<TdHealthIndex> queryDiagnosisInfos2(@Param("params") Map params);
}
