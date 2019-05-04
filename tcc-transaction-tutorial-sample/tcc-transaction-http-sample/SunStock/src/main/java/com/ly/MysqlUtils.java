package com.ly;

import com.ly.holder.StockHolder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

public class MysqlUtils {

    private static final Connection conn = getConnection();

    public static Connection getConnection() {
        Connection connection = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/stock";
            Class.forName(myDriver);
            connection = DriverManager.getConnection(myUrl, "root", "liyang");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }


    public static void insert(List<StockHolder> stockHolders){
        stockHolders.forEach(MysqlUtils::insert);
    }

    public static void insert(StockHolder stockHolder) {
        try {

            Connection conn = getConnection();
            // the mysql insert statement
            String query = " insert into stock_holder (code, name, count, rate, changeState,changeCount,stockName)"
                    + " values (?, ?, ?, ?, ?,?,?)";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, stockHolder.getCode());
            preparedStmt.setString(2, stockHolder.getName());
            preparedStmt.setDouble(3, stockHolder.getCount());
            preparedStmt.setDouble(4, stockHolder.getRate());
            preparedStmt.setString(5, stockHolder.getChangeState());
            preparedStmt.setDouble(6, stockHolder.getChangeCount());
            preparedStmt.setString(7, stockHolder.getStockName());
            preparedStmt.execute();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        StockHolder stockHolder = new StockHolder();
        stockHolder.setCode("sdfds");
        stockHolder.setChangeState("sdfds");
        stockHolder.setName("hellol");
        stockHolder.setChangeCount(1231.0);
        stockHolder.setCount(123.0);
        stockHolder.setRate(234.0);
        insert(stockHolder);
    }
}
