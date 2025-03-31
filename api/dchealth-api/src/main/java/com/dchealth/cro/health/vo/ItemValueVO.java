package com.dchealth.cro.health.vo;

import lombok.Data;

@Data
public class ItemValueVO {
  private String name;
  private String value;
  private String minValue;
  private String maxValue;
  private String unit;
  private String tip;
  private String rangeValue;

  public void setTip(String tip) {

        this.tip = tip;
    }

    public String getTip() {
        return tip;
    }

  public void setRangeValue(String rangeValue) {
    this.rangeValue = rangeValue;
  }

  public String getRangeValue() {
    return rangeValue;
  }
}
