package com.crawl.VietCap.model;

import java.math.BigDecimal;

public class BusinessProfileEntity {

    private Integer yearReport;
    private Integer lengthReport;
    // Vốn hóa - Enterprise value
    private Long ev = 0L;
    // Số cổ phiếu lưu hành - issue share
    private Long issueShare;
    private BigDecimal eps;
    private BigDecimal pe;
    private BigDecimal pb;

    public BusinessProfileEntity() {

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
        if (this.ev == null)
            return 0L;
        else
            return this.ev;
    }

    public void setEv(Long ev) {
        this.ev = ev;
    }

    public Long getIssueShare() {
        if (this.issueShare == null)
            return 0L;
        else
            return issueShare;
    }

    public void setIssueShare(Long issueShare) {
        this.issueShare = issueShare;
    }

    public BigDecimal getEps() {
        if (this.eps == null)
            return new BigDecimal(0);
        else
            return this.eps;
    }

    public void setEps(BigDecimal eps) {
        this.eps = eps;
    }

    public BigDecimal getPe() {
        if (this.pe == null)
            return new BigDecimal(0);
        else
            return this.pe;
    }

    public void setPe(BigDecimal pe) {
        this.pe = pe;
    }

    public BigDecimal getPb() {
        if (this.pb == null)
            return new BigDecimal(0);
        else
            return this.pb;
    }

    public void setPb(BigDecimal pb) {
        this.pb = pb;
    }

}
