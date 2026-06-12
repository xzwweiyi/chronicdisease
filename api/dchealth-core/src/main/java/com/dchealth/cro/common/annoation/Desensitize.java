package com.dchealth.cro.common.annoation;

import com.dchealth.cro.common.constant.DesensitizeType;
import com.dchealth.cro.common.serializers.DesensitizeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JsonSerialize(using = DesensitizeSerializer.class)
public @interface Desensitize {

    DesensitizeType type();
}

