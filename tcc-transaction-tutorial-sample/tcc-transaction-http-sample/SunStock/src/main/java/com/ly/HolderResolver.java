package com.ly;

import com.ly.holder.Stock;
import com.ly.holder.StockHolder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class HolderResolver {

    public static Stock resolveTopTen(String html) {
        Document doc = Jsoup.parse(html);
        Element info = doc.select("div.name").get(0);
        Elements h2 = info.getElementsByTag("h2");
        String name = h2.get(0).select("h2").text();
        String code = h2.get(1).select("h2").text().replace("(", "").replace(")", "");

        Stock stock = new Stock(code, name);
        Elements elements = doc.select("div.bom_bor.skinBox.skinBox2");
        Element element = elements.get(1);
        Elements holders = element.select("tbody tr");

        for (int i = 0; i < holders.size(); i++) {
            Element trElement = holders.get(i);
            Elements td = trElement.getElementsByTag("td");
            StockHolder stockHolder = new StockHolder();
            stockHolder.setCode(code);
            stockHolder.setStockName(name);
            stockHolder.setName(td.get(0).text());
            stockHolder.setCount(Double.parseDouble(td.get(1).text()));
            stockHolder.setRate(Double.parseDouble(td.get(2).text()));
            stockHolder.setChangeState(td.get(3).text());
            stockHolder.setChangeCount(Double.parseDouble(td.get(4).text()));
            stock.getStockHolders().add(stockHolder);
        }
        return stock;
    }


    public static List<String> getStockCodes() throws IOException, URISyntaxException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("stock.txt");
        Path path = Paths.get(resource.toURI());
        List<String> lines = Files.readAllLines(path);
        return lines;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("stock.txt");
        Path path = Paths.get(resource.toURI());
        List<String> lines = Files.readAllLines(path);
        lines.forEach(System.out::println);

//        HolderResolver resolver = new HolderResolver();
//        Stock stock = resolver.resolveTopTen(new String(Files.readAllBytes(Paths.get("D:/stock.txt"))));
//        System.out.println(stock);
    }
}
