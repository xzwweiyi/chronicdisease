package com.dchealth.compatibility.vo;

import lombok.Data;

@Data
public class HealthDetailVO<T> {
     private  GeneralInspectionVO general;
     
     private  HealthItemVO<T>  diagnosis;
}
