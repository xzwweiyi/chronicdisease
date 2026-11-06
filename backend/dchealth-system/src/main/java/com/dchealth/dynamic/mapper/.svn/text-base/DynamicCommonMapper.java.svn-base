package com.dchealth.dynamic.mapper;

import com.dchealth.medical.domain.form.DynamicCommonProperty;
import com.dchealth.medical.domain.form.DynamicForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DynamicCommonMapper {

    /**
     * 动态查询数据执行sql
     * @param dynamicForm
     * @return
     */
    public List<DynamicCommonProperty> selectBySql(DynamicForm dynamicForm);

    /**
     * 执行新增SQL
     * @param sql
     */
    public int executeInsertSql(@Param("sql") String sql);

    /**
     * 执行删除SQL
     * @param sql
     * @return
     */
    public int executeDeleteSql(@Param("sql") String sql);

}
