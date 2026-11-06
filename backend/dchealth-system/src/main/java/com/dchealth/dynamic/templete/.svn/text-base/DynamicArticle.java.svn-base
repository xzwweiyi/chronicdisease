package com.dchealth.dynamic.templete;

import com.dchealth.template.domain.vo.FMedicalValueVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.dchealth.dynamic.DynamicConstant.*;

/**
 * 问卷
 */
@Service
public class DynamicArticle extends DynamicBase{

    String TABLE_NAME_PRE = "res_article_";

    @Override
    public void buildDynamicTableName() {
        tableNamePre = TABLE_NAME_PRE;
        List<String> tableNames = dynamicTableMapper.querySamePreTable(TABLE_NAME_PRE);
        samePreTables.addAll(tableNames.stream().filter(s -> isNum(s.replace(TABLE_NAME_PRE, "")))
                .collect(Collectors.toList()));
    }

    /**
     * 构建表字段
     */
    @Override
    public void buildDynamicColumn() {
        if (CollectionUtils.isNotEmpty(samePreTables)) {
            return;
        }
        columnList.add(buildColumnPrimaryKey("id", COLUMN_TYPE_BIGINT, true));
        columnList.add(buildColumnDefaultNull("form_id", COLUMN_TYPE_BIGINT, "表单ID", true));
        columnList.add(buildColumnDefaultNull("article_id", COLUMN_TYPE_BIGINT, "问卷ID", true));
        columnList.add(buildColumnDefaultNull("patient_id", COLUMN_TYPE_BIGINT, "患者ID", true));
        columnList.add(buildColumnDefaultNull("research_id", COLUMN_TYPE_BIGINT, "队列ID", true));
        columnList.add(buildColumnDefaultNull("follow_id", COLUMN_TYPE_BIGINT, "随访ID", true));
        columnList.add(buildColumnDefaultNull("exam_record_id", COLUMN_TYPE_BIGINT, "问卷填写记录id", true));
        columnList.add(buildColumnDefaultNull("property_id", COLUMN_TYPE_BIGINT, "属性Id"));
        columnList.add(buildColumnDefaultNull("property_code", COLUMN_TYPE_VARCHAR64, "属性code"));
        columnList.add(buildColumnDefaultNull("property_value", COLUMN_TYPE_VARCHAR256, "属性值"));
        columnList.add(buildColumnDefaultNull("other", COLUMN_TYPE_VARCHAR256, "其它属性值"));
        columnList.add(buildColumnWithDefaultValue("create_by", COLUMN_TYPE_VARCHAR64, " ", "创建者"));
        columnList.add(buildColumnDefaultNull("create_time", COLUMN_TYPE_TIME, "创建时间"));
        columnList.add(buildColumnWithDefaultValue("update_by", COLUMN_TYPE_VARCHAR64, " ", "更新者"));
        columnList.add(buildColumnDefaultNull("update_time", COLUMN_TYPE_TIME, "更新时间"));
    }

    /**
     * 查询患者问卷相关数据值
     * @param articleId
     * @param examRecordId
     * @return
     */
    public String buildSelectSql(Long articleId, Long examRecordId, int i){
        StringBuilder builder = new StringBuilder();
        builder.append("select id,form_id formId,property_id propertyId,property_code propertyCode,property_value propertyValue, other, exam_record_id examRecordId from ");
        builder.append(TABLE_NAME_PRE + i);
        builder.append(" where 1=1 ");
        if(articleId != null){
            builder.append(" and article_id = " + articleId);
        }
        if(examRecordId != null){
            builder.append(" and exam_record_id = " + examRecordId);
        }
        return builder.toString();
    }

    /**
     * 构建存储语句
     * @param vos
     * @param suffixId
     * @return
     */
    public String buildArticleInsertSql(List<FMedicalValueVo> vos, int suffixId){
        if (vos == null || vos.isEmpty()){
            return null;
        }
        StringBuffer insertSql = new StringBuffer();
        insertSql.append("insert into "+ TABLE_NAME_PRE + suffixId)
                .append("(")
                .append("form_id, article_id, patient_id, research_id, follow_id, exam_record_id, property_id, property_code, property_value, other, create_by, create_time")
                .append(")")
                .append(" values ");
        vos.forEach(vo->{
            insertSql.append(" (")
                    .append(vo.getFromId()).append(",")
                    .append(vo.getArticleId()).append(",")
                    .append(vo.getPatientId()).append(",")
                    .append(vo.getRecordId()).append(",")
                    .append(vo.getFollowId()).append(",")
                    .append(vo.getExamRecordId()).append(",")
                    .append(vo.getPropertyId()).append(",")
                    .append("'").append(vo.getPropertyCode()).append("'").append(",")
                    .append("'").append(vo.getPropertyValue()).append("'").append(",")
                    .append("'").append(vo.getOther()).append("'").append(",")
                    .append("'").append(vo.getCreateBy()).append("'").append(",")
                    .append("now()")
                    .append("),");
        });
        return insertSql.toString().substring(0,insertSql.length()-1);
    }

}
