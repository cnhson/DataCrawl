package com.crawl.model;

import java.math.BigDecimal;

public class VietCapFinancalAnalystEntity {

    private Integer yearReport;
    private Integer lengthReport;
    private Long revenue;
    private BigDecimal revenueGrowth;
    private Long netProfit;
    private BigDecimal netProfitGrowth;
    private BigDecimal roe;
    private BigDecimal roic;
    private BigDecimal roa;
    private BigDecimal pe;
    private BigDecimal pb;
    private BigDecimal eps;

    // Vốn hóa - Enterprise value
    private Long ev;
    // Số cổ phiếu lưu hành - issue share
    private Long issueShare;
    private Long ebit;

    public VietCapFinancalAnalystEntity() {

    }

    public Integer getYearReport() {
        return yearReport;
    }

    public void setYearReport(Integer yearReport) {
        this.yearReport = yearReport;
    }

    public Integer getLengthReport() {
        return lengthReport;
    }

    public void setLengthReport(Integer lengthReport) {
        this.lengthReport = lengthReport;
    }

    public Long getEv() {
        return this.ev;
    }

    public void setEv(Long ev) {
        this.ev = ev;
    }

    public Long getIssueShare() {
        return issueShare;
    }

    public void setIssueShare(Long issueShare) {
        this.issueShare = issueShare;
    }

    public BigDecimal getEps() {
        return this.eps;
    }

    public void setEps(BigDecimal eps) {
        this.eps = eps;
    }

    public BigDecimal getPe() {
        return this.pe;
    }

    public void setPe(BigDecimal pe) {
        this.pe = pe;
    }

    public BigDecimal getPb() {
        return this.pb;
    }

    public void setPb(BigDecimal pb) {
        this.pb = pb;
    }

    public Long getRevenue() {
        return revenue;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }

    public BigDecimal getRevenueGrowth() {
        return revenueGrowth;
    }

    public void setRevenueGrowth(BigDecimal revenueGrowth) {
        this.revenueGrowth = revenueGrowth;
    }

    public Long getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(Long netProfit) {
        this.netProfit = netProfit;
    }

    public BigDecimal getNetProfitGrowth() {
        return netProfitGrowth;
    }

    public void setNetProfitGrowth(BigDecimal netProfitGrowth) {
        this.netProfitGrowth = netProfitGrowth;
    }

    public BigDecimal getRoe() {
        return roe;
    }

    public void setRoe(BigDecimal roe) {
        this.roe = roe;
    }

    public BigDecimal getRoic() {
        return roic;
    }

    public void setRoic(BigDecimal roic) {
        this.roic = roic;
    }

    public BigDecimal getRoa() {
        return roa;
    }

    public void setRoa(BigDecimal roa) {
        this.roa = roa;
    }

    public Long getEbit() {
        return ebit;
    }

    public void setEbit(Long ebit) {
        this.ebit = ebit;
    }

}
