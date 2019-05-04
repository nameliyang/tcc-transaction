package com.ly.holder;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class Stock {

    /**
     *
     */
    private String code;

    private String stockName;


    private String name;


    private List<StockHolder> stockHolders = new ArrayList<>();
    public Stock(){}



    public Stock(String code){
        this.code = code;
    }

    public Stock(String code,String name){
        this.code = code;
        this.name = name;
    }


}
