package com.crawl.model;

import java.math.BigDecimal;

public class VietCapFinancalAnalystEntity {

    private Integer yearReport;
    private Integer lengthReport;
    // Vốn hóa - Enterprise value
    private Long ev;
    // Số cổ phiếu lưu hành - issue share
    private Long issueShare;
    private BigDecimal eps;
    private BigDecimal pe;
    private BigDecimal pb;

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

}
