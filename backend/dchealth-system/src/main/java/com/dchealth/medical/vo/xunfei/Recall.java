package com.dchealth.medical.vo.xunfei;

import lombok.Data;

/**
 * 是否启用重拨提醒,不启用可不传
 */
@Data
public class Recall {

    public Recall(){

    }

    /**
     * 重拨次数（上限2次）
     */
    private Integer recallCount;

    /**
     * 重拨间隔 （1-60）分钟
     */
    private Integer timeInterval;

    public Recall(Integer recallCount,Integer timeInterval){
        this.recallCount=recallCount;
        this.timeInterval=timeInterval;
    }

}
