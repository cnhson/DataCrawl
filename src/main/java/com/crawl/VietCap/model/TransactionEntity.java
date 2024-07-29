package com.crawl.VietCap.model;

import java.util.Date;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class TransactionEntity {
    ////
    // volume -> Integer
    // value -> Long
    ////
    private String id; // timestamp + stockSymbol -> 1721952000VCI
    private Long tradingDate;
    private Integer priceChange;
    private BigDecimal percentPriceChange;
    private Integer openPrice;
    private Integer closePrice;
    private Integer highestPrice;
    private Integer lowestPrice;
    private Integer totalMatchVolume;
    private Long totalMatchValue;
    private Integer totalDealVolume;
    private Long totalDealValue;
    private Integer unMatchedBuyTradeVolume;
    private Integer unMatchedSellTradeVolume;
    private Long totalValue;
    private Integer totalVolume;
    private String stockSymbol;

    public TransactionEntity(Long tradingDate, Integer priceChange, BigDecimal percentPriceChange, Integer openPrice,
            Integer closePrice,
            Integer highestPrice, Integer lowestPrice, Integer totalMatchVolume, Long totalMatchValue,
            Integer totalDealVolume, Long totalDealValue, Integer unMatchedBuyTradeVolume,
            Integer unMatchedSellTradeVolume, Long totalValue, Integer totalVolume, String stockSymbol) {
        this.tradingDate = tradingDate / 1000;
        this.priceChange = priceChange;
        this.percentPriceChange = percentPriceChange;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.highestPrice = highestPrice;
        this.lowestPrice = lowestPrice;
        this.totalMatchVolume = totalMatchVolume;
        this.totalMatchValue = totalMatchValue;
        this.totalDealVolume = totalDealVolume;
        this.totalDealValue = totalDealValue;
        this.unMatchedBuyTradeVolume = unMatchedBuyTradeVolume;
        this.unMatchedSellTradeVolume = unMatchedSellTradeVolume;
        this.totalValue = totalValue;
        this.totalVolume = totalVolume;
        this.stockSymbol = stockSymbol;
        this.id = generateId();
    }

    public String generateId() {
        return String.valueOf(this.tradingDate) + this.stockSymbol;
    }

    public String getId() {
        return this.id;
    }

    public String getFormatStringTradingDate() {
        Date date = new Date(this.tradingDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public void setTradingDate(Long tradingDate) {
        this.tradingDate = tradingDate;
    }

    public Integer getPriceChange() {
        return this.priceChange;
    }

    public void setPriceChange(Integer priceChange) {
        this.priceChange = priceChange;
    }

    public BigDecimal getPercentPriceChange() {
        return this.percentPriceChange;
    }

    public void setPercentPriceChange(BigDecimal percentPriceChange) {
        this.percentPriceChange = percentPriceChange;
    }

    public Integer getOpenPrice() {
        return this.openPrice;
    }

    public void setOpenPrice(Integer openPrice) {
        this.openPrice = openPrice;
    }

    public Integer getClosePrice() {
        return this.closePrice;
    }

    public void setClosePrice(Integer closePrice) {
        this.closePrice = closePrice;
    }

    public Integer getHighestPrice() {
        return this.highestPrice;
    }

    public void setHighestPrice(Integer highestPrice) {
        this.highestPrice = highestPrice;
    }

    public Integer getLowestPrice() {
        return this.lowestPrice;
    }

    public void setLowestPrice(Integer lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public Integer getTotalMatchVolume() {
        return this.totalMatchVolume;
    }

    public void setTotalMatchVolume(Integer totalMatchVolume) {
        this.totalMatchVolume = totalMatchVolume;
    }

    public Long getTotalMatchValue() {
        return this.totalMatchValue;
    }

    public void setTotalMatchValue(Long totalMatchValue) {
        this.totalMatchValue = totalMatchValue;
    }

    public Integer getTotalDealVolume() {
        return this.totalDealVolume;
    }

    public void setTotalDealVolume(Integer totalDealVolume) {
        this.totalDealVolume = totalDealVolume;
    }

    public Long getTotalDealValue() {
        return this.totalDealValue;
    }

    public void setTotalDealValue(Long totalDealValue) {
        this.totalDealValue = totalDealValue;
    }

    public Integer getUnMatchedBuyTradeVolume() {
        return this.unMatchedBuyTradeVolume;
    }

    public void setUnMatchedBuyTradeVolume(Integer unMatchedBuyTradeVolume) {
        this.unMatchedBuyTradeVolume = unMatchedBuyTradeVolume;
    }

    public Integer getUnMatchedSellTradeVolume() {
        return this.unMatchedSellTradeVolume;
    }

    public void setUnMatchedSellTradeVolume(Integer unMatchedSellTradeVolume) {
        this.unMatchedSellTradeVolume = unMatchedSellTradeVolume;
    }

    public Long getTotalValue() {
        return this.totalValue;
    }

    public void setTotalValue(Long totalValue) {
        this.totalValue = totalValue;
    }

    public Integer getTotalVolume() {
        return this.totalVolume;
    }

    public void setTotalVolume(Integer totalVolume) {
        this.totalVolume = totalVolume;
    }

    public String getStockSymbol() {
        return this.stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }
}
