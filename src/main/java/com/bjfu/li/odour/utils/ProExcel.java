package com.bjfu.li.odour.utils;

import com.bjfu.li.odour.po.Compound;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ProExcel {
    public static String createExcelFile(HttpServletResponse response, List<Compound> compounds, List<String> needSheet) {
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

    public static Workbook createReportWorkbook(List<Compound>compounds,List<String> needSheet) {
        System.out.println(compounds);
        String[] titles = { "CAS号","化合物名称","俗名"};
        String[] titles1 = {"极性柱", "RI", "来源"};
        String[] titles2 = {"非极性柱", "RI", "来源"};
        String[] titles3 = {"香气描述", "描述来源"};
        String[] titles4 = {"香气阈值(µg/kg)", "来源", "基质"};
        // 创建HSSFWorkbook对象，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);// 设置单元格水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 设置单元格垂直居中
        HSSFSheet sheet1 = wb.createSheet("基本信息");
        HSSFRow row = sheet1.createRow(0);// 创建基本信息表头
        for (int i = 0; i < titles.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(titles[i]);
        }
        for(int rownum=0;rownum<compounds.size();rownum++){
            row=sheet1.createRow(rownum+1);
            HSSFCell cell0 = row.createCell(0);
            cell0.setCellStyle(style);
            cell0.setCellValue(compounds.get(rownum).getCasNo());

            HSSFCell cell1 = row.createCell(1);
            cell1.setCellStyle(style);
            cell1.setCellValue(compounds.get(rownum).getCompoundName());

            HSSFCell cell2 = row.createCell(2);
            cell2.setCellStyle(style);
            cell2.setCellValue(compounds.get(rownum).getSynonym());

        }


        if(needSheet.contains("ri")){
            HSSFSheet sheet2 = wb.createSheet("极性RI");
            HSSFRow rowSheetTitle2 = sheet2.createRow(0);// 创建基本信息表头
            HSSFCell cellS2Cas = rowSheetTitle2.createCell(0);
            cellS2Cas.setCellStyle(style);
            cellS2Cas.setCellValue("CAS");//创建sheet2第一个表头
            for (int i = 1; i <= 12; i++) {
                HSSFCell cell = rowSheetTitle2.createCell(i);
                cell.setCellStyle(style);
                if(i%3==1)
                    cell.setCellValue(titles1[0]);
                if(i%3==2)
                    cell.setCellValue(titles1[1]);
                if(i%3==0)
                    cell.setCellValue(titles1[2]);
            }
            for(int rownum=0;rownum<compounds.size();rownum++){
                row=sheet2.createRow(rownum+1);
                HSSFCell cell0 = row.createCell(0);
                cell0.setCellStyle(style);
                cell0.setCellValue(compounds.get(rownum).getCasNo());//填充sheet2第一列
                for(int colnum=0;colnum<3*compounds.get(rownum).getRiList().size();colnum++){

                    HSSFCell S2cell0 = row.createCell(colnum+1);
                    S2cell0.setCellStyle(style);
                    if(colnum%3==0)
                            S2cell0.setCellValue(compounds.get(rownum).getRiList().get(colnum/3).getChromatographicColumn());
                    if(colnum%3==1)
                        if(compounds.get(rownum).getRiList().get(colnum/3).getCompoundRi()==null)
                            S2cell0.setCellValue("");
                        else
                            S2cell0.setCellValue(compounds.get(rownum).getRiList().get(colnum/3).getCompoundRi());
                    if(colnum%3==2)
                        S2cell0.setCellValue(compounds.get(rownum).getRiList().get(colnum/3).getRiResource());
                }

            }


        }
        if(needSheet.contains("nri")){
            HSSFSheet sheet3 = wb.createSheet("非极性RI");
            HSSFRow rowSheetTitle3 = sheet3.createRow(0);// 创建基本信息表头
            HSSFCell cellS2Cas = rowSheetTitle3.createCell(0);
            cellS2Cas.setCellStyle(style);
            cellS2Cas.setCellValue("CAS");//创建sheet2第一个表头
            for (int i = 1; i <= 21; i++) {
                HSSFCell cell = rowSheetTitle3.createCell(i);
                cell.setCellStyle(style);
                if(i%3==1)
                    cell.setCellValue(titles2[0]);
                if(i%3==2)
                    cell.setCellValue(titles2[1]);
                if(i%3==0)
                    cell.setCellValue(titles2[2]);
            }
            for(int rownum=0;rownum<compounds.size();rownum++){
                row=sheet3.createRow(rownum+1);
                HSSFCell cell0 = row.createCell(0);
                cell0.setCellStyle(style);
                cell0.setCellValue(compounds.get(rownum).getCasNo());//填充sheet2第一列

                for(int colnum=0;colnum<3*compounds.get(rownum).getNriList().size();colnum++){

                    HSSFCell S3cell0 = row.createCell(colnum+1);
                    S3cell0.setCellStyle(style);
                    if(colnum%3==0)

                        S3cell0.setCellValue(compounds.get(rownum).getNriList().get(colnum/3).getChromatographicColumn());
                    if(colnum%3==1)
                        if(compounds.get(rownum).getNriList().get(colnum/3).getCompoundNri()==null)
                            S3cell0.setCellValue("");
                        else
                            S3cell0.setCellValue(compounds.get(rownum).getNriList().get(colnum/3).getCompoundNri());
                    if(colnum%3==2)
                         S3cell0.setCellValue(compounds.get(rownum).getNriList().get(colnum/3).getNriResource());
                }

            }


        }
        if(needSheet.contains("od")){
            HSSFSheet sheet4 = wb.createSheet("香气描述");
            HSSFRow rowSheetTitle4 = sheet4.createRow(0);// 创建基本信息表头
            HSSFCell cellS2Cas = rowSheetTitle4.createCell(0);
            cellS2Cas.setCellStyle(style);
            cellS2Cas.setCellValue("CAS");//创建sheet2第一个表头
            for (int i = 1; i <= 21; i++) {
                HSSFCell cell = rowSheetTitle4.createCell(i);
                cell.setCellStyle(style);
                if(i%2==1)
                    cell.setCellValue(titles3[0]);
                if(i%2==0)
                    cell.setCellValue(titles3[1]);
            }
            for(int rownum=0;rownum<compounds.size();rownum++){
                row=sheet4.createRow(rownum+1);
                HSSFCell cell0 = row.createCell(0);
                cell0.setCellStyle(style);
                cell0.setCellValue(compounds.get(rownum).getCasNo());//填充sheet4第一列
                for(int colnum=0;colnum<2*compounds.get(rownum).getOdList().size();colnum++){

                    HSSFCell S2cell0 = row.createCell(colnum+1);
                    S2cell0.setCellStyle(style);

                    if(colnum%2==0)
                        S2cell0.setCellValue(compounds.get(rownum).getOdList().get(colnum/2).getOdourDescription());
                    if(colnum%2==1)
                        S2cell0.setCellValue(compounds.get(rownum).getOdList().get(colnum/2).getOdourDescriptionReference());
                }

            }
        }
        if(needSheet.contains("ot")){
            HSSFSheet sheet5 = wb.createSheet("香气阈值");
            HSSFRow rowSheetTitle5 = sheet5.createRow(0);// 创建基本信息表头
            HSSFCell cellS2Cas = rowSheetTitle5.createCell(0);
            cellS2Cas.setCellStyle(style);
            cellS2Cas.setCellValue("CAS");//创建sheet2第一个表头
            for (int i = 1; i <= 21; i++) {
                HSSFCell cell = rowSheetTitle5.createCell(i);
                cell.setCellStyle(style);
                if(i%3==1)
                    cell.setCellValue(titles4[0]);
                if(i%3==2)
                    cell.setCellValue(titles4[1]);
                if(i%3==0)
                    cell.setCellValue(titles4[2]);
            }
            for(int rownum=0;rownum<compounds.size();rownum++){
                row=sheet5.createRow(rownum+1);
                HSSFCell cell0 = row.createCell(0);
                cell0.setCellStyle(style);
                cell0.setCellValue(compounds.get(rownum).getCasNo());//填充sheet2第一列
                for(int colnum=0;colnum<3*compounds.get(rownum).getOtList().size();colnum++){

                    HSSFCell S2cell0 = row.createCell(colnum+1);
                    S2cell0.setCellStyle(style);
                    if(colnum%3==0)
                        if(compounds.get(rownum).getOtList().get(colnum/3).getOdourThreshold()==null)
                            S2cell0.setCellValue("");
                        else
                            S2cell0.setCellValue(compounds.get(rownum).getOtList().get(colnum/3).getOdourThreshold().toString());
                    if(colnum%3==1)
                            S2cell0.setCellValue(compounds.get(rownum).getOtList().get(colnum/3).getOdourThresholdReference());
                    if(colnum%3==2)
                        S2cell0.setCellValue(compounds.get(rownum).getOtList().get(colnum/3).getOdourBase());
                }

            }

        }


        return wb;
    }

}
