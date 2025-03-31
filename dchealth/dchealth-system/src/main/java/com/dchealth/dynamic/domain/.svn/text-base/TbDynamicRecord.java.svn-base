package com.dchealth.dynamic.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TbDynamicRecord {
    private int id;
    private int tbOrder;
    private String tbPre;
    private String tbFullName;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String isDel;
}
