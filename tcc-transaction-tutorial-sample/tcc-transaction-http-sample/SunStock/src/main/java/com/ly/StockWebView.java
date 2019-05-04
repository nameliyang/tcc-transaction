package com.ly;

import com.ly.holder.Stock;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker.State;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class StockWebView extends Application {

    Button sourceBtn = new Button("source");
    Button back = new Button("<-");
    Button pre = new Button("->");
    Button fresh = new Button("fresh");
    Button go = new Button("GO");
    static final TextField urlText = new TextField();
    static final WebView browser = new WebView();

    static final WebEngine webEngine = browser.getEngine();

    static final ReentrantLock lock = new ReentrantLock();

    static final Condition condition = lock.newCondition();

    static final AtomicBoolean resolving = new AtomicBoolean(true);

    private static final String STOCK_URL_PATTERN = "https://stock.quote.stockstar.com/share_%s.shtml";

    private static List<String> codes;

    static {
        codes = new ArrayList<>();
        try {
            codes = StockResp.getStockCodes();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
//        codes.add("000970");
//        codes.add("600039");
    }


    @Override
    public void start(final Stage stage) {

        StackPane group = new StackPane();
        VBox vBox = new VBox();

        vBox.setPadding(new Insets(10));

        group.getChildren().add(vBox);
        Scene scene = new Scene(group);

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.BASELINE_LEFT);

        hbox.getChildren().addAll(back, pre, fresh);
        hbox.getChildren().add(urlText);
        hbox.getChildren().add(go);
        hbox.getChildren().add(sourceBtn);
        hbox.setSpacing(10);
        HBox.setHgrow(urlText, Priority.ALWAYS);

        vBox.getChildren().add(hbox);

        browser.prefHeightProperty().bind(stage.heightProperty());
        browser.prefWidthProperty().bind(stage.widthProperty());

        StackPane stackPanel = new StackPane();
        stackPanel.getChildren().add(browser);

        vBox.getChildren().add(stackPanel);

        webEngine.getLoadWorker().stateProperty()
                .addListener((ov, oldState, newState) -> {
                    if (newState == State.SUCCEEDED) {
                        lock.lock();
                        try {
                            String str = (String) webEngine.executeScript("document.documentElement.outerHTML");
                            try{
                                Stock stock = HolderResolver.resolveTopTen(str);
                                MysqlUtils.insert(stock.getStockHolders());
                            }catch (Exception e){
                                System.out.println("resolving error"+urlText.getText());
                                e.printStackTrace();
                            }
                            resolving.set(false);
                            condition.signalAll();
                        } finally {
                            lock.unlock();
                        }
                    }
                });
        webEngine.load(String.format(STOCK_URL_PATTERN, codes.get(0)));
        stage.setScene(scene);
        stage.setWidth(1000);
        stage.setHeight(1000);
        stage.show();
    }

    public static void main(String[] args) {
        new ScheduleStock().start();
        launch(args);
    }

    static class ScheduleStock extends Thread {


        public ScheduleStock() {
            setDaemon(true);
        }


        @Override
        public void run() {
            for (int i = 1; i < codes.size(); i++) {
                String code = codes.get(i);
                while (resolving.get()) {
                    lock.lock();
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
                System.out.println("start load " + String.format(STOCK_URL_PATTERN, code));
                urlText.setText(String.format(STOCK_URL_PATTERN, code));
                Platform.runLater(() -> {
                    resolving.set(true);
                    webEngine.load(urlText.getText());
                });
            }

        }
    }

    public StockWebView() {
        bindPreEvent();
        bindBackEvent();
        bindRefreshEvent();
        bindGoEvent();
        bindKeyEvent();
        bindSourceBtnEvent();
    }

    private void bindSourceBtnEvent() {
        sourceBtn.setOnAction(event -> Platform.runLater(() -> System.out.println(webEngine.executeScript("document.documentElement.outerHTML"))));
    }

    private void bindKeyEvent() {
        urlText.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Platform.runLater(() -> webEngine.load(urlText.getText()));
            }
        });
    }


    private void bindGoEvent() {
        go.setOnAction((e) -> Platform.runLater(() -> webEngine.load(urlText.getText())));
    }

    private void bindRefreshEvent() {
        fresh.setOnAction((e) -> Platform.runLater(webEngine::reload));
    }

    private void bindBackEvent() {
        back.setOnAction((e) -> Platform.runLater(() -> webEngine.executeScript("history.back()")));
    }

    void bindPreEvent() {
        pre.setOnAction((e) -> Platform.runLater(() -> webEngine.executeScript("history.forward()")));
    }
}



