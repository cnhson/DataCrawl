package com.crawl.model;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.text.Normalizer;

public class VietCapICBEntity {

    @SerializedName("icbName")
    private String name;
    @SerializedName("icbCode")
    private Integer icbCode;
    // @SerializedName("enIcbName")
    // private String enIcbName;
    @SerializedName("RSD11")
    private Long capitalization;
    @SerializedName("percentPriceChange1Day")
    private BigDecimal percentPriceChange1Day;
    @SerializedName("percentPriceChange1Week")
    private BigDecimal percentPriceChange1Week;
    @SerializedName("percentPriceChange1Month")
    private BigDecimal percentPriceChange1Month;
    @SerializedName("RSD21")
    private BigDecimal pe;
    @SerializedName("RSD25")
    private BigDecimal pb;
    @SerializedName("RSQ34")
    private BigDecimal revenueGrowth;
    @SerializedName("RSQ41")
    private BigDecimal assetGrowth;
    @SerializedName("RSQ42")
    private BigDecimal oeGrowth;
    @SerializedName("RSQ14")
    private BigDecimal roa;
    @SerializedName("RSQ12")
    private BigDecimal roe;
    @SerializedName("netProfitMargin")
    private BigDecimal netProfitMargin;
    @SerializedName("RSQ25")
    private BigDecimal grossProfitMargin;
    @SerializedName("RSQ11")
    private BigDecimal debtRatio;
    @SerializedName("RSD30")
    private BigDecimal ev;

    public VietCapICBEntity() {

    }

    public String getName() {
        return name;
    }

    public String getNameAsCode() {
        String temp = name.toLowerCase();
        temp = Normalizer.normalize(temp, Normalizer.Form.NFD);
        temp = temp.replaceAll("đ", "d");
        temp = temp.replaceAll("\\p{M}", "");
        temp = temp.replaceAll("[^a-z0-9]", "");
        return temp;
    }

    public Long getCapitalization() {
        return capitalization;
    }

    public BigDecimal getPercentPriceChange1Day() {
        return percentPriceChange1Day;
    }

    public BigDecimal getPercentPriceChange1Week() {
        return percentPriceChange1Week;
    }

    public BigDecimal getPercentPriceChange1Month() {
        return percentPriceChange1Month;
    }

    public BigDecimal getPe() {
        return pe;
    }

    public BigDecimal getPb() {
        return pb;
    }

    public BigDecimal getRevenueGrowth() {
        return revenueGrowth;
    }

    public BigDecimal getAssetGrowth() {
        return assetGrowth;
    }

    public BigDecimal getOeGrowth() {
        return oeGrowth;
    }

    public BigDecimal getRoa() {
        return roa;
    }

    public BigDecimal getRoe() {
        return roe;
    }

    public BigDecimal getNetProfitMargin() {
        return netProfitMargin;
    }

    public BigDecimal getGrossProfitMargin() {
        return grossProfitMargin;
    }

    public BigDecimal getDebtRatio() {
        return debtRatio;
    }

    public BigDecimal getEv() {
        return ev;
    }

    public Integer getIcbCode() {
        return icbCode;
    }

}
