package com.dchealth.common.utils;

import org.apache.commons.compress.utils.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class SplitListUtils {

    public static <T> List<List<T>> splitList(List<T> resList, int subListLength){
        if(CollectionUtils.isEmpty(resList) || subListLength <=0){
            return Lists.newArrayList();
        }
        List<List<T>> ret = Lists.newArrayList();
        int size = resList.size();
        if(size<subListLength){
            //数量不足subListLength指定的大小
            ret.add(resList);
        }else{
            int pre = size / subListLength;
            int last = size % subListLength;
            //前面pre个集合,每个大小都是subListLength个元素
            for(int i=0;i<pre;i++){
                List<T> itemList = Lists.newArrayList();
                for(int j=0;j<subListLength;j++){
                    itemList.add(resList.get(i * subListLength + j));
                }
                ret.add(itemList);
            }
            //last进行处理
            if(last>0){
                List<T> itemList = Lists.newArrayList();
                for(int i=0;i<last;i++){
                    itemList.add(resList.get(pre * subListLength + i));
                }
                ret.add(itemList);
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        List<String> list = Lists.newArrayList();
        int size=1099;
        for(int i=0;i<size;i++){
            list.add("hello-"+i);
        }
        //大集合里面包含多个小集合
        List<List<String>> temps = splitList(list,100);
        int j=0;
        for(List<String> obj:temps){
            System.out.println(String.format("row:%s -> size:%s,data:%s",++j,obj.size(),obj));
        }


    }
}
