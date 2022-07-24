package com.bjfu.li.odour.utils;

import com.bjfu.li.odour.po.Oil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SheetDataWriter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OilExcel {
    public static String createExcelFile(HttpServletResponse response,List<Oil> oils) throws Exception {
        OutputStream outputStream = null;

        OutputStream os = null;

        try {
            org.apache.poi.ss.usermodel.Workbook wb;
            wb = createReportWorkbook(oils);
//            resultFileName = "D:\\" + File.separator + "excel" + File.separator + fileName;
//            resultFileName = resultFileName.replaceAll("\\\\", "/");
//            File file = new File(resultFileName);
//            if (!file.exists()) {
//                file.getParentFile().mkdirs();
//            }
//            int i=1;
//            while(file.exists()) {
//                String newFilename ="compound"+"("+i+")"+".xls";
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


        }catch (IOException ex) {
            ex.printStackTrace();
            return "failure";
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

        return "success";
    }

    public static Workbook createReportWorkbook(List<Oil> oils) {
        System.out.println(oils);
        String[] titles = { "基本信息","基本信息","基本信息","基本信息","关键化合物","关键化合物","关键化合物","关键化合物" ,"食用油风味","食用油风味","食用油风味"};

        String[] handClum = { "0,0,0,3", "0,0,4,7", "0,0,8,10"};

        String[] titles2 = { "食用油名称", "类型", "品牌", "生产日期", "化合物名称", "化合物CAS", "化合物浓度", "备注", "风味描述","属性强度","备注"};

        // 创建HSSFWorkbook对象，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();


        if (oils != null && oils.size() > 0)

            for(int i=0;i<oils.size();i++){
                HSSFSheet sheet = wb.createSheet(oils.get(i).getOilName());

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
                for (int j = 0; j < handClum.length; j++) {

                    // sheet.autoSizeColumn(i, true);
                    String[] temp = handClum[j].split(",");
                    Integer startrow = Integer.parseInt(temp[0]);
                    Integer overrow = Integer.parseInt(temp[1]);
                    Integer startcol = Integer.parseInt(temp[2]);
                    Integer overcol = Integer.parseInt(temp[3]);
                    sheet.addMergedRegion(new CellRangeAddress(startrow, overrow, startcol, overcol));
                }

                row = sheet.createRow(1);// 创建表头2
                for (int j = 0; j < titles2.length; j++) {
                    HSSFCell cell = row.createCell(j);
                    cell.setCellStyle(style);
                    cell.setCellValue(titles2[j]);
                }
                for(int rownum=2;rownum<15;rownum++){
                    sheet.createRow(rownum);
                }


                HSSFRow row1 = sheet.createRow(  2);// 填充内容,从第2行开始,0行给表头
                //填充名称和CAS
                HSSFCell cell0 = row1.createCell(0);
                cell0.setCellStyle(style);
                cell0.setCellValue(oils.get(i).getOilName());

                HSSFCell cell1 = row1.createCell(1);
                cell1.setCellStyle(style);
                cell1.setCellValue(oils.get(i).getOilType());

                HSSFCell cell2 = row1.createCell(2);
                cell2.setCellStyle(style);
                cell2.setCellValue(oils.get(i).getOilBrand());

                HSSFCell cell3 = row1.createCell(3);
                cell3.setCellStyle(style);
                cell3.setCellValue(oils.get(i).getOilDate());



                if (oils.get(i).getOkList() != null && oils.get(i).getOkList().size() > 0) {
                    for (int oknum = 0; oknum < oils.get(i).getOkList().size(); oknum++) {

                        HSSFRow okRow = sheet.getRow(2 + oknum);// 填充内容,从第2行开始,0行给表头

                        HSSFCell cell4 = okRow.createCell(4);
                        cell4.setCellStyle(style);
                        cell4.setCellValue(oils.get(i).getOkList().get(oknum).getKeycompoundName());

                        HSSFCell cell5 = okRow.createCell(5);
                        cell5.setCellStyle(style);
                        cell5.setCellValue(oils.get(i).getOkList().get(oknum).getCasNo());

                        HSSFCell cell6 = okRow.createCell(6);
                        cell6.setCellStyle(style);
                        if(oils.get(i).getOkList().get(oknum).getConcentration()==null){
                            cell6.setCellValue("");
                        }
                        else {
                            cell6.setCellValue(oils.get(i).getOkList().get(oknum).getConcentration().doubleValue());
                        }

                        HSSFCell cell7 = okRow.createCell(7);
                        cell7.setCellStyle(style);
                        cell7.setCellValue(oils.get(i).getOkList().get(oknum).getOkNote());

                    }
                }

                if (oils.get(i).getOoList() != null && oils.get(i).getOoList().size() > 0) {
                    for (int oonum = 0; oonum < oils.get(i).getOkList().size(); oonum++) {

                        HSSFRow okRow = sheet.getRow(2 + oonum);// 填充内容,从第2行开始,0行给表头

                        HSSFCell cell8 = okRow.createCell(8);
                        cell8.setCellStyle(style);
                        cell8.setCellValue(oils.get(i).getOoList().get(oonum).getOilOdour());


                        HSSFCell cell9 = okRow.createCell(9);
                        cell9.setCellStyle(style);
                        if(oils.get(i).getOoList().get(oonum).getOilOdourIntensity()==null){
                            cell9.setCellValue("");
                        }
                        else {
                            cell9.setCellValue(oils.get(i).getOoList().get(oonum).getOilOdourIntensity().doubleValue());
                        }

                        HSSFCell cell10 = okRow.createCell(10);
                        cell10.setCellStyle(style);
                        cell10.setCellValue(oils.get(i).getOoList().get(oonum).getOoNote());

                    }
                }


            }
        return wb;
    }

}
