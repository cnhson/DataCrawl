package T4.model;

public class RatioEntity {

    private Integer lengthReport;
    private Integer yearReport;
    private Double revenue;
    private Double revenueGrowth;
    private Double netProfit;
    private Double netProfitGrowth;
    private Double ebitMargin;
    private Double roe;
    private Double roic;
    private Double roa;
    private Double pe;
    private Double pb;
    private Double eps;
    private Double currentRatio;
    private Double cashRatio;
    private Double quickRatio;
    private Double interestCoverage;
    private Double ae;

    public RatioEntity(Integer lengthReport, Integer yearReport, Double revenue, Double revenueGrowth, Double netProfit,
            Double netProfitGrowth, Double ebitMargin, Double roe, Double roic, Double roa, Double pe, Double pb,
            Double eps, Double currentRatio, Double cashRatio, Double quickRatio, Double interestCoverage, Double ae) {
        this.lengthReport = lengthReport;
        this.yearReport = yearReport;
        this.revenue = revenue;
        this.revenueGrowth = revenueGrowth;
        this.netProfit = netProfit;
        this.netProfitGrowth = netProfitGrowth;
        this.ebitMargin = ebitMargin;
        this.roe = roe;
        this.roic = roic;
        this.roa = roa;
        this.pe = pe;
        this.pb = pb;
        this.eps = eps;
        this.currentRatio = currentRatio;
        this.cashRatio = cashRatio;
        this.quickRatio = quickRatio;
        this.interestCoverage = interestCoverage;
        this.ae = ae;
    }

    public Integer getLengthReport() {
        return lengthReport;
    }

    public void setLengthReport(Integer lengthReport) {
        this.lengthReport = lengthReport;
    }

    public Integer getYearReport() {
        return yearReport;
    }

    public void setYearReport(Integer yearReport) {
        this.yearReport = yearReport;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public Double getRevenueGrowth() {
        return revenueGrowth;
    }

    public void setRevenueGrowth(Double revenueGrowth) {
        this.revenueGrowth = revenueGrowth;
    }

    public Double getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(Double netProfit) {
        this.netProfit = netProfit;
    }

    public Double getNetProfitGrowth() {
        return netProfitGrowth;
    }

    public void setNetProfitGrowth(Double netProfitGrowth) {
        this.netProfitGrowth = netProfitGrowth;
    }

    public Double getEbitMargin() {
        return ebitMargin;
    }

    public void setEbitMargin(Double ebitMargin) {
        this.ebitMargin = ebitMargin;
    }

    public Double getRoe() {
        return roe;
    }

    public void setRoe(Double roe) {
        this.roe = roe;
    }

    public Double getRoic() {
        return roic;
    }

    public void setRoic(Double roic) {
        this.roic = roic;
    }

    public Double getRoa() {
        return roa;
    }

    public void setRoa(Double roa) {
        this.roa = roa;
    }

    public Double getPe() {
        return pe;
    }

    public void setPe(Double pe) {
        this.pe = pe;
    }

    public Double getPb() {
        return pb;
    }

    public void setPb(Double pb) {
        this.pb = pb;
    }

    public Double getEps() {
        return eps;
    }

    public void setEps(Double eps) {
        this.eps = eps;
    }

    public Double getCurrentRatio() {
        return currentRatio;
    }

    public void setCurrentRatio(Double currentRatio) {
        this.currentRatio = currentRatio;
    }

    public Double getCashRatio() {
        return cashRatio;
    }

    public void setCashRatio(Double cashRatio) {
        this.cashRatio = cashRatio;
    }

    public Double getQuickRatio() {
        return quickRatio;
    }

    public void setQuickRatio(Double quickRatio) {
        this.quickRatio = quickRatio;
    }

    public Double getInterestCoverage() {
        return interestCoverage;
    }

    public void setInterestCoverage(Double interestCoverage) {
        this.interestCoverage = interestCoverage;
    }

    public Double getAe() {
        return ae;
    }

    public void setAe(Double ae) {
        this.ae = ae;
    }
}
