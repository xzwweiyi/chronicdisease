package com.dchealth.elasticsearch.utils;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.dchealth.elasticsearch.annotation.ESID;
import com.dchealth.elasticsearch.annotation.ESIndices;
import com.dchealth.elasticsearch.enums.DataType;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class IndexTools {

    //private static Map<Class,ESDocument> classMap = new ConcurrentHashMap();

    /**
     * 映射关系方法
     * @param clazz
     * @return
     */
    /*public ESDocument document(Class<?> clazz){
        if (classMap.containsKey(clazz)){
            return classMap.get(clazz);
        }
        if (clazz.getAnnotation(ESIndices.class) != null){
            ESDocument document = new ESDocument();
            String indexName = clazz.getAnnotation(ESIndices.class).indexName();
            document.setIndexName(indexName);
            List<ESMetaData> metaDatas = new ArrayList<>();
            for (Field field : clazz.getDeclaredFields()){
                ESMetaData metaData = new ESMetaData();
                Type type = field.getGenericType();//属性类型
                metaData.setType(type);
                String propertype = field.getName();
                metaData.setProperty(propertype);
                if (field.getAnnotation(ESID.class) != null){
                    metaData.setPkField(true);
                }
                if (field.getAnnotation(ESMapping.class) != null){
                    String column = field.getAnnotation(ESMapping.class).column();
                    if (StringUtils.isNotBlankAndNotEmpty(column)){
                        metaData.setColumn(column);
                    }else{
                        metaData.setColumn(propertype);
                    }
                    DataType dataType = field.getAnnotation(ESMapping.class).datatype();
                    if (dataType != null){
                        metaData.setDataType(getType(dataType));
                    }
                    String[] formats = field.getAnnotation(ESMapping.class).dateFormat();
                    if (formats != null && formats.length > 0){
                        metaData.setDateFormat(new CopyOnWriteArrayList<>(formats));
                    }
                }else{
                    metaData.setColumn(propertype);
                    metaData.setDataType(getType(DataType.text_type));
                }
                metaDatas.add(metaData);
            }
            document.setMetaDatas(metaDatas);
            classMap.put(clazz, document);
            return document;
        }
        return null;
    }*/

    /**
     * 获取ID名称
     * @param clazz
     * @return
     */
    public String getIdName(Class<?> clazz){
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(ESID.class) != null) {
                return field.getName();
            }
        }
        return null;
    }
    
    public void setIdValue(Object obj, String id) throws NoSuchFieldException, IllegalAccessException {
        String idName = getIdName(obj.getClass());
        Field field = obj.getClass().getDeclaredField(idName);
        field.setAccessible(true);
        field.set(obj, id);
    }
    
    public void setValue(Object obj, Hit<?> hit) throws NoSuchFieldException, IllegalAccessException {
        String idName = getIdName(obj.getClass());
        if (idName != null){
            Field field = obj.getClass().getDeclaredField(idName);
            field.setAccessible(true);
            field.set(obj, hit.id());
        }
    }

    /**
     * 获取ID值
     * @param obj
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public String getIdValue(Object obj) throws NoSuchFieldException, IllegalAccessException {
        String idName = getIdName(obj.getClass());
        Field field = obj.getClass().getDeclaredField(idName);
        field.setAccessible(true);
        Object value = field.get(obj);
        if (value == null){
            return null;
        }
        return value.toString();
    }

    /**
     * 获取ID值
     * @param obj
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    /*public String getId(Object obj) throws IllegalAccessException, NoSuchFieldException {
        ESDocument document = document(obj.getClass());
        if (document == null){
            throw new ElasticsearchException("No corresponding entity information found");
        }
        ESMetaData metaData = getIdMetaData(document);
        if (metaData == null){
            throw new ElasticsearchException("Entity configuration error");
        }
        Field field = obj.getClass().getDeclaredField(metaData.getProperty());
        field.setAccessible(true);
        Object value = field.get(obj);
        if (value == null){
            return null;
        }else{
            return value.toString();
        }
    }*/

    /**
     * 获取索引名称
     * @param obj
     * @return
     */
    public String getIndex(Object obj){
        return getIndex(obj.getClass());
    }

    /**
     * 获取索引名称
     * @param clazz
     * @return
     */
    /*public String getIndex(Class<?> clazz){
        ESDocument document = document(clazz);
        if (document == null){
            throw new ElasticsearchException("No corresponding entity information found");
        }
        if (StringUtils.isNotBlankAndNotEmpty(document.getIndexName())){
            return document.getIndexName();
        }else{
            throw new ElasticsearchException("Entity configuration error");
        }
    }*/
    
    public String getIndex(Class<?> clazz){
        if (clazz.getAnnotation(ESIndices.class) != null) {
            return clazz.getAnnotation(ESIndices.class).indexName();
        }
        return null;
    }

    /**
     * Json请求体
     * @param obj
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    /*public String getJsonContent(Object obj) throws NoSuchFieldException, IllegalAccessException {
        ESDocument document = document(obj.getClass());
        if (document == null || document.getMetaDatas() == null){
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        final Field[] fields = obj.getClass().getDeclaredFields();
        for (ESMetaData x : document.getMetaDatas()){
            B:for (Field field : fields){
                if (!x.isPkField() && field.getName().equals(x.getProperty())){
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (value != null){
                        if (field.getGenericType() == Date.class){
                            List<String> formats = x.getDateFormat();
                            if (formats != null && !formats.isEmpty()){
                                map.put(x.getColumn(),DateUtils.parseDateToStr(formats.get(0), (Date) value));
                            }else{
                                map.put(x.getColumn(),DateUtils.parseDateToStrFull((Date) value));
                            }
                        }else{
                            map.put(x.getColumn(),value);
                        }
                    }
                    break B;
                }
            }
        }
        return JSON.toJSONString(map);
    }*/

    /**
     * 获取主键metaData
     * @param document
     * @return
     */
    /*private ESMetaData getIdMetaData(ESDocument document){
        if (document == null || document.getMetaDatas() == null){
            return null;
        }
        return document.getMetaDatas()
                .stream()
                .filter(f->f.isPkField())
                .findFirst()
                .get();
    }*/

    private String getType(DataType dataType) {
        return dataType.toString().replaceAll("_type", "");
    }
}
