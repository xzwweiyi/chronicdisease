package com.dchealth.cro.common.serializers;

import com.dchealth.cro.common.annoation.Desensitize;
import com.dchealth.cro.common.constant.DesensitizeType;
import com.dchealth.cro.common.utils.DesensitizeUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Date;

public class DesensitizeSerializer extends JsonSerializer<Object>
        implements ContextualSerializer {

    private DesensitizeType type;

    public DesensitizeSerializer() {}

    public DesensitizeSerializer(DesensitizeType type) {
        this.type = type;
    }


    @Override
    public void serialize(Object value,
                          JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {

        if (value == null) {
            gen.writeNull();
            return;
        }

        // ① Date 类型（支持 yyyy-MM-dd / yyyy-MM-dd HH:mm:ss）
        if (value instanceof Date) {

            Date date = (Date) value;

            // 统一格式化到秒，避免丢失时间
            String formatted = null;

            // 只对生日做脱敏
            if (type == DesensitizeType.BIRTHDAY) {
                formatted = new java.text.SimpleDateFormat("yyyy-MM-dd")
                        .format(date);
                // yyyy-MM-dd HH:mm:ss → yyyy-xx-xx xx:xx:xx
                String masked = formatted
                        .replaceAll(
                                "(\\d{4})-\\d{2}-\\d{2}",
                                "$1-xx-xx"
                        );

                gen.writeString(masked);
            } else {
                // yyyy-MM-dd HH:mm:ss → yyyy-xx-xx xx:xx:xx
                formatted = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(date);
                String masked = formatted
                        .replaceAll(
                                "(\\d{4})-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}",
                                "$1-xx-xx xx:xx:xx"
                        );
                gen.writeString(masked);
            }
            return;
        }


        // ② String 类型（姓名、电话等）
        if (value instanceof String) {
            gen.writeString(
                    DesensitizeUtil.mask((String) value, type)
            );
            return;
        }

        // ③ 兜底
        gen.writeObject(value);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov,
                                              BeanProperty property)
            throws JsonMappingException {

        if (property != null) {
            Desensitize desensitize =
                    property.getAnnotation(Desensitize.class);
            if (desensitize != null) {
                return new DesensitizeSerializer(desensitize.type());
            }
        }
        return this;
    }
}

