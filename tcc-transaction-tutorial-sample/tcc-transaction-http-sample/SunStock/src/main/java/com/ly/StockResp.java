package com.ly;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class StockResp {

    public static List<String> getStockCodes() throws IOException, URISyntaxException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("stock.txt");
        Path path = Paths.get(resource.toURI());
        List<String> lines = Files.readAllLines(path);
        return lines.stream().map(e -> e.substring(0, e.indexOf(" "))).collect(Collectors.toList());
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        List<String> stockCodes = StockResp.getStockCodes();
        System.out.println(stockCodes);
    }
}
