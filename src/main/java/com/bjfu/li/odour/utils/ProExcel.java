package com.bjfu.li.odour.utils;

import com.bjfu.li.odour.po.Compound;
import com.bjfu.li.odour.po.OdourDescription;
import com.bjfu.li.odour.po.OdourThreshold;
import com.bjfu.li.odour.vo.DownloadVo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ProExcel {
    public static String createExcelFile(HttpServletResponse response, List<DownloadVo> compounds, List<String> needSheet) {
        OutputStream outputStream = null;
        try {
            org.apache.poi.ss.usermodel.Workbook wb;
            wb = createReportWorkbook(compounds,needSheet);
//
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//            resultFileName = "D:\\" + File.separator + "proexcel" + File.separator + fileName;
//            resultFileName = resultFileName.replaceAll("\\\\", "/");
//            File file = new File(resultFileName);
//            if (!file.exists()) {
//                file.getParentFile().mkdirs();
//            }
//            int i=1;
//            while(file.exists()) {
//                String newFilename ="procompound"+"("+i+")"+".xls";
//                String parentPath = file.getParent();
//                file = new File(parentPath+ File.separator+newFilename);
//                i++;
//            }
//            fos = new FileOutputStream(file);
//            os = new BufferedOutputStream(fos, 1024);
//            wb.write(os);
//            os.flush();
//            state = true;
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.flushBuffer();
            outputStream = response.getOutputStream();
            wb.write(outputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
            return "failure";
        }finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "success";
    }

    public static Workbook createReportWorkbook(List<DownloadVo>compounds,List<String> needSheet) {
        // 创建HSSFWorkbook对象，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);// 设置单元格水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 设置单元格垂直居中

        for (DownloadVo compound : compounds) {
            HSSFSheet sheet = wb.createSheet(compound.getCasNo());
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell = row.createCell(0);
            HSSFCellStyle cellStyle = wb.createCellStyle();
            cell.setCellValue("compound_name");
            cellStyle.setFillForegroundColor((short) 22);
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(1);
            cell.setCellValue("synonym");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(2);
            cell.setCellValue("cas_no");
            cell.setCellStyle(cellStyle);
            row = sheet.createRow(1);
            cell = row.createCell(0);
            cell.setCellValue(compound.getCompoundName());
            cell = row.createCell(1);
            cell.setCellValue(compound.getSynonym());
            cell = row.createCell(2);
            cell.setCellValue(compound.getCasNo());

            row = sheet.createRow(3);
            cell = row.createCell(0);
            cell.setCellValue("odor_threshold");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(1);
            cell.setCellValue("odor_base");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(2);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("reference");
            int j = 4;
            for (OdourThreshold threshold : compound.getOtList()) {
                row = sheet.createRow(j);
                cell = row.createCell(0);
                if(threshold.getOdourThreshold() != null) {
                    cell.setCellValue(threshold.getOdourThreshold().toString());
                } else {
                    cell.setCellValue("");
                }
                cell = row.createCell(1);
                cell.setCellValue(threshold.getOdourBase());
                cell = row.createCell(2);
                cell.setCellValue(threshold.getArticleName());
                j++;
            }

            j++;
            row = sheet.createRow(j);
            j++;
            cell = row.createCell(0);
            cell.setCellValue("odor_description");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(1);
            cell.setCellValue("reference");
            cell.setCellStyle(cellStyle);
            for (OdourDescription description : compound.getOdList()) {
                row = sheet.createRow(j);
                cell = row.createCell(0);
                cell.setCellValue(description.getOdourDescription());
                cell = row.createCell(1);
                cell.setCellValue(description.getArticleName());
                j++;
            }
        }
        return wb;
    }

}
