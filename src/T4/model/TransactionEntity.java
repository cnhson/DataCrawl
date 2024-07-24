package T4.model;

import java.util.Date;
import java.text.SimpleDateFormat;

public class TransactionEntity {
    private Long tradingDate;
    private Double priceChange;
    private Double openPrice;
    private Double closePrice;
    private Double highestPrice;
    private Double lowestPrice;
    private Double totalMatchVolume;
    private Double totalMatchValue;
    private Double totalDealVolume;
    private Double totalDealValue;
    private Double unMatchedBuyTradeVolume;
    private Double unMatchedSellTradeVolume;
    private Double totalValue;
    private Double totalVolume;

    public TransactionEntity(Long tradingDate, Double priceChange, Double openPrice, Double closePrice,
            Double highestPrice, Double lowestPrice, Double totalMatchVolume, Double totalMatchValue,
            Double totalDealVolume, Double totalDealValue, Double unMatchedBuyTradeVolume,
            Double unMatchedSellTradeVolume, Double totalValue, Double totalVolume) {
        this.tradingDate = tradingDate;
        this.priceChange = priceChange;
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
    }

    public String getTradingDate() {
        Date date = new Date(tradingDate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public void setTradingDate(Long tradingDate) {
        this.tradingDate = tradingDate;
    }

    public Double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(Double priceChange) {
        this.priceChange = priceChange;
    }

    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }

    public Double getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(Double highestPrice) {
        this.highestPrice = highestPrice;
    }

    public Double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(Double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public Double getTotalMatchVolume() {
        return totalMatchVolume;
    }

    public void setTotalMatchVolume(Double totalMatchVolume) {
        this.totalMatchVolume = totalMatchVolume;
    }

    public Double getTotalMatchValue() {
        return totalMatchValue;
    }

    public void setTotalMatchValue(Double totalMatchValue) {
        this.totalMatchValue = totalMatchValue;
    }

    public Double getTotalDealVolume() {
        return totalDealVolume;
    }

    public void setTotalDealVolume(Double totalDealVolume) {
        this.totalDealVolume = totalDealVolume;
    }

    public Double getTotalDealValue() {
        return totalDealValue;
    }

    public void setTotalDealValue(Double totalDealValue) {
        this.totalDealValue = totalDealValue;
    }

    public Double getUnMatchedBuyTradeVolume() {
        return unMatchedBuyTradeVolume;
    }

    public void setUnMatchedBuyTradeVolume(Double unMatchedBuyTradeVolume) {
        this.unMatchedBuyTradeVolume = unMatchedBuyTradeVolume;
    }

    public Double getUnMatchedSellTradeVolume() {
        return unMatchedSellTradeVolume;
    }

    public void setUnMatchedSellTradeVolume(Double unMatchedSellTradeVolume) {
        this.unMatchedSellTradeVolume = unMatchedSellTradeVolume;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public Double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Double totalVolume) {
        this.totalVolume = totalVolume;
    }

}
