package com.ly.holder;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StockHolder {

    private String code;

    private String stockName;

    /**
     * 股东名称
     */
    private String name;

    /**
     * 持股数(万股)
     */
    private Double count;

    /**
     *  占总股
     * 比例(%)
     */
    private Double rate;

    /**
     *  变动类型
     */
    private String changeState;

    /**
     * 变动数量
     */
    private Double changeCount;

}
