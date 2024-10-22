package com.crawl.VietCap.run;

import java.util.List;

import com.crawl.VietCap.controller.BusinessProfileRequest;
import com.crawl.VietCap.model.BusinessProfileEntity;
import com.crawl.VietCap.util.ExcelWriteUtil;

//`import com.foxthehuman.VietCapCrawl.controller.TransactionController;

public class ExportBusinessProfile {
    public static void main(String[] args) {

        // String[] vn30SymbolList = { "VCI", "VHM", "VRE", "VNM", "GAS", "SAB", "BID",
        // "CTG", "BVH", "STB", "KBC",
        // "NVL", "CII", "MBB", "PNJ", "DHG", "DPM", "HCM", "BMP", "REE", "NT2", "BMI",
        // "EIB" };

        String[] vn30SymbolList = { "VCI", "VHM" };

        String[] extendHeadersList = new String[] {
                "Enterprise_Value", "Issue_Share",
                "Eps", "Pb", "Pe"
        };
        String filename = "vietcap_business_profile";

        ExcelWriteUtil eu = new ExcelWriteUtil();
        BusinessProfileRequest bpr = new BusinessProfileRequest();

        eu.setFileName(filename);

        for (String symbol : vn30SymbolList) {
            // eu.addNewSymbolSheet(symbol, headersList);
            // eu.setHeadersConfig(headersList);
            List<BusinessProfileEntity> transList = bpr.crawlData(symbol);

            // Arrays.sort(transList,
            // Comparator.comparingLong(BusinessProfileEntity::getLongTradingDate));

            eu.loopWriteContent(transList, (row, entity) -> {

                row.createCell(0).setCellValue(entity.getYearReport() + "-" + entity.getLengthReport());
                row.createCell(1).setCellValue(entity.getEv());
                row.createCell(2).setCellValue(entity.getIssueShare());
                row.createCell(3).setCellValue(String.valueOf(entity.getEps()));
                row.createCell(4).setCellValue(String.valueOf(entity.getPb()));
                row.createCell(5).setCellValue(String.valueOf(entity.getPe()));

            });
        }
        eu.saveAndClose();
    }
}