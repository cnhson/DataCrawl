package com.crawl.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.annotations.SerializedName;

public class CafeFTransactionEntity {

    private transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @SerializedName("Ngay")
    private String tradingDate;
    @SerializedName("ThayDoi")
    private String priceChange;
    @SerializedName("GiaMoCua")
    private Double openPrice;
    @SerializedName("GiaDongCua")
    private Double closePrice;
    @SerializedName("GiaCaoNhat")
    private Double highestPrice;
    @SerializedName("GiaThapNhat")
    private Double lowestPrice;
    @SerializedName("KhoiLuongKhopLenh")
    private Integer totalMatchVolume;
    @SerializedName("GiaTriKhopLenh")
    private Long totalMatchValue;
    @SerializedName("KLThoaThuan")
    private Integer totalDealVolume;
    @SerializedName("GtThoaThuan")
    private Long totalDealValue;
    private transient String stockSymbol;

    public CafeFTransactionEntity() {
    }

    public String getTradingDate() {
        return tradingDate;
    }

    public LocalDate getTradingLocalDate() {
        return LocalDate.parse(tradingDate, formatter);
    }

    public String getPriceChange() {
        return priceChange.split("\\(")[0];
    }

    public Integer getPriceChangeAsInteger() {
        return (int) (Double.parseDouble(priceChange.split("\\(")[0]) * 1000);
    }

    public Integer getOpenPrice() {
        return (int) (openPrice * 1000);
    }

    public Integer getClosePrice() {
        return (int) (closePrice * 1000);
    }

    public Integer getHighestPrice() {
        return (int) (highestPrice * 1000);
    }

    public Integer getLowestPrice() {
        return (int) (lowestPrice * 1000);
    }

    public Integer getTotalMatchVolume() {
        return totalMatchVolume;
    }

    public Long getTotalMatchValue() {
        return totalMatchValue;
    }

    public Integer getTotalDealVolume() {
        return totalDealVolume;
    }

    public Long getTotalDealValue() {
        return totalDealValue;
    }

    public Long getTotalValue() {
        return (totalMatchValue + totalDealValue);
    }

    public Integer getTotalVolume() {
        return (totalMatchVolume + totalDealVolume);
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

}
