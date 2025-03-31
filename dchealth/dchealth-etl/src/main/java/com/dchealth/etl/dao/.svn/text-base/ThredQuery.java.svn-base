package com.dchealth.etl.dao;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 多线程查询
 * @author zjl
 * @since 2023/4/14
 */
@Slf4j
public class ThredQuery implements Callable<List> {
    /******查询条件 根据条件来定义该类的属性******/
    private Map<String, Object> params;
    private Object myService;
    private String methodName;
    /******分页index******/
    private int offset;
    /******数量******/
    private int num;

    public ThredQuery(Object myService,String methodName, Map<String, Object> params, int offset, int num) {
        this.myService = myService;
        this.methodName = methodName;
        this.params = params;
        this.offset = offset;
        this.num = num;
    }


    @Override
    public List call() throws Exception {
        /******通过service查询得到对应结果******/
        return querySyncInfoByPage(myService,methodName,params);
    }

    private List querySyncInfoByPage(Object obj,String methodName,Map params) throws Exception{
        try{
            Method method = obj.getClass().getDeclaredMethod(methodName,Map.class);
            return (List)method.invoke(obj,params);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }

    }

}
