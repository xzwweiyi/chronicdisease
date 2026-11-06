package com.dchealth.compatibility.vo;

import lombok.Data;

@Data
public class ItemValueVO {
  private String name;
  private String value;
  private String minValue;
  private String maxValue;
  private String unit;
  private String href;
  private String tip;
  private String rangeValue;
}
