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
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class Excel {

    public static void createExcelFile(HttpServletResponse response,List<Compound> compounds) {
        OutputStream outputStream = null;
        try {
            org.apache.poi.ss.usermodel.Workbook wb;
            wb = createReportWorkbook(compounds);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.flushBuffer();
            outputStream = response.getOutputStream();
            wb.write(outputStream);
        }catch (IOException ex) {
            ex.printStackTrace();
        }  finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static Workbook createReportWorkbook(List<Compound> compounds) {
        System.out.println(compounds);
        String[] titles = { "基本信息","基本信息", "RI", "RI", "RI",  "NRI","NRI", "NRI", "高分辨率离子碎片","高分辨率离子碎片","低分辨率离子碎片","低分辨率离子碎片","香气描述","香气描述","香气阈值","香气阈值" ,"香气阈值"};
        String[] handClum = { "0,0,0,1", "0,0,2,4", "0,0,5,7", "0,0,8,9" , "0,0,10,11","0,0,12,13","0,0,14,16"};
        String[] titles2 = { "化合物名称", "CAS号", "保留指数（极性）", "色谱柱", "来源", "保留指数（非极性）", "色谱柱", "来源", "质荷比","相对丰度","质荷比","相对丰度","描述内容","描述来源","阈值","基质","阈值来源"};
        // 创建HSSFWorkbook对象，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        if (compounds != null && compounds.size() > 0)
            for (Compound compound : compounds) {
                HSSFSheet sheet = wb.createSheet(compound.getCompoundName());
                CellStyle style = wb.createCellStyle();
                style.setAlignment(HorizontalAlignment.CENTER);// 设置单元格水平居中
                style.setVerticalAlignment(VerticalAlignment.CENTER);// 设置单元格垂直居中

                for (int j = 0; j < titles.length; j++) {
                    sheet.setColumnWidth(j, 5000);
                }

                // 在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
                HSSFRow row = sheet.createRow(0);// 创建表头1
                for (int j = 0; j < titles.length; j++) {
                    HSSFCell cell = row.createCell(j);
                    cell.setCellStyle(style);
                    cell.setCellValue(titles[j]);
                }

                // 动态合并单元格
                for (String s : handClum) {
                    String[] temp = s.split(",");
                    int startrow = Integer.parseInt(temp[0]);
                    int overrow = Integer.parseInt(temp[1]);
                    int startcol = Integer.parseInt(temp[2]);
                    int overcol = Integer.parseInt(temp[3]);
                    sheet.addMergedRegion(new CellRangeAddress(startrow, overrow, startcol, overcol));
                }

                row = sheet.createRow(1);// 创建表头2
                for (int j = 0; j < titles2.length; j++) {
                    HSSFCell cell = row.createCell(j);
                    cell.setCellStyle(style);
                    cell.setCellValue(titles2[j]);
                }
                for (int rownum = 2; rownum < 15; rownum++) {
                    sheet.createRow(rownum);
                }


                HSSFRow row1 = sheet.createRow(2);// 填充内容,从第2行开始,0行给表头
                //填充名称和CAS
                HSSFCell cell0 = row1.createCell(0);
                cell0.setCellStyle(style);
                cell0.setCellValue(compound.getCompoundName());
                HSSFCell cell1 = row1.createCell(1);
                cell1.setCellStyle(style);
                cell1.setCellValue(compound.getCasNo());
                System.out.println(compound.getRiList());
                //填充RI
                if (compound.getRiList() != null && compound.getRiList().size() > 0) {
                    for (int rinum = 0; rinum < compound.getRiList().size(); rinum++) {

                        HSSFRow riRow = sheet.getRow(2 + rinum);// 填充内容,从第2行开始,0行给表头
                        //填充RI 色谱柱 来源
                        HSSFCell cell2 = riRow.createCell(2);
                        cell2.setCellStyle(style);
                        if (compound.getRiList().get(rinum).getCompoundRi() == null) {
                            cell2.setCellValue("");
                        } else {
                            cell2.setCellValue(compound.getRiList().get(rinum).getCompoundRi());
                        }
                        HSSFCell cell3 = riRow.createCell(3);
                        cell3.setCellStyle(style);
                        cell3.setCellValue(compound.getRiList().get(rinum).getChromatographicColumn());
                        HSSFCell cell4 = riRow.createCell(4);
                        cell4.setCellStyle(style);
                        cell4.setCellValue(compound.getRiList().get(rinum).getRiResource());
                    }
                } else {
                    System.out.println(1);
                }
                //填充NRI
                if (compound.getNriList() != null && compound.getNriList().size() > 0) {
                    for (int nrinum = 0; nrinum < compound.getNriList().size(); nrinum++) {

                        HSSFRow nriRow = sheet.getRow(2 + nrinum);// 填充内容,从第2行开始,0行给表头
                        //填充RI 色谱柱 来源
                        HSSFCell cell2 = nriRow.createCell(5);
                        cell2.setCellStyle(style);
                        if (compound.getNriList().get(nrinum).getCompoundNri() == null) {
                            cell2.setCellValue("");
                        } else {
                            cell2.setCellValue(compound.getNriList().get(nrinum).getCompoundNri());
                        }
                        HSSFCell cell3 = nriRow.createCell(6);
                        cell3.setCellStyle(style);
                        cell3.setCellValue(compound.getNriList().get(nrinum).getChromatographicColumn());
                        HSSFCell cell4 = nriRow.createCell(7);
                        cell4.setCellStyle(style);
                        cell4.setCellValue(compound.getNriList().get(nrinum).getNriResource());
                    }
                }
                //填充高精度
                if (compound.getMrList() != null && compound.getMrList().size() > 0) {
                    for (int mrnum = 0; mrnum < compound.getMrList().size(); mrnum++) {

                        HSSFRow mrRow = sheet.getRow(2 + mrnum);// 填充内容,从第2行开始,0行给表头
                        //填充RI 色谱柱 来源
                        HSSFCell cell2 = mrRow.createCell(8);
                        cell2.setCellStyle(style);
                        if (compound.getMrList().get(mrnum).getMeasured() == null) {
                            cell2.setCellValue("");
                        } else {
                            cell2.setCellValue(compound.getMrList().get(mrnum).getMeasured().doubleValue());
                        }
                        HSSFCell cell3 = mrRow.createCell(9);
                        cell3.setCellStyle(style);

                        if (compound.getMrList().get(mrnum).getRelativeAbundance() == null) {
                            cell3.setCellValue("");
                        } else {
                            cell3.setCellValue(compound.getMrList().get(mrnum).getRelativeAbundance().toString());
                        }
                    }
                }
                //低精度
                if (compound.getLowmrList() != null && compound.getLowmrList().size() > 0) {
                    for (int lmrnum = 0; lmrnum < compound.getLowmrList().size(); lmrnum++) {

                        HSSFRow lmrRow = sheet.getRow(2 + lmrnum);// 填充内容,从第2行开始,0行给表头
                        //填充RI 色谱柱 来源
                        HSSFCell cell2 = lmrRow.createCell(10);
                        cell2.setCellStyle(style);
                        if (compound.getLowmrList().get(lmrnum).getMeasured() == null) {
                            cell2.setCellValue("");
                        } else {
                            cell2.setCellValue(compound.getLowmrList().get(lmrnum).getMeasured().doubleValue());
                        }
                        HSSFCell cell3 = lmrRow.createCell(11);
                        cell3.setCellStyle(style);
                        if (compound.getLowmrList().get(lmrnum).getRelativeAbundance() == null) {
                            cell3.setCellValue("");
                        } else {
                            cell3.setCellValue(compound.getLowmrList().get(lmrnum).getRelativeAbundance());
                        }
                    }
                }
                //
                if (compound.getOdList() != null && compound.getOdList().size() > 0) {
                    for (int odnum = 0; odnum < compound.getOdList().size(); odnum++) {

                        HSSFRow odRow = sheet.getRow(2 + odnum);
                        HSSFCell cell2 = odRow.createCell(12);
                        cell2.setCellStyle(style);
                        cell2.setCellValue(compound.getOdList().get(odnum).getOdourDescription());
                        HSSFCell cell3 = odRow.createCell(13);
                        cell3.setCellStyle(style);
                        cell3.setCellValue(compound.getOdList().get(odnum).getArticleId());

                    }
                }
                if (compound.getOtList() != null && compound.getOtList().size() > 0) {
                    for (int otnum = 0; otnum < compound.getOtList().size(); otnum++) {

                        HSSFRow otRow = sheet.getRow(2 + otnum);// 填充内容,从第2行开始,0行给表头

                        HSSFCell cell2 = otRow.createCell(14);
                        cell2.setCellStyle(style);
                        if (compound.getOtList().get(otnum).getOdourThreshold() == null) {
                            cell2.setCellValue("");
                        } else {
                            cell2.setCellValue(compound.getOtList().get(otnum).getOdourThreshold().doubleValue());
                        }
                        HSSFCell cell3 = otRow.createCell(15);
                        cell3.setCellStyle(style);
                        cell3.setCellValue(compound.getOtList().get(otnum).getOdourBase());

                        HSSFCell cell4 = otRow.createCell(16);
                        cell4.setCellStyle(style);
                        cell4.setCellValue(compound.getOtList().get(otnum).getArticleId());

                    }
                }
            }
        return wb;
    }


}
