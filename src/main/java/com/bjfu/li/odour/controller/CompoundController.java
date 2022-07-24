package com.bjfu.li.odour.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bjfu.li.odour.common.pojo.SverResponse;
import com.bjfu.li.odour.common.token.JWTUtils;
import com.bjfu.li.odour.mapper.NriMapper;
import com.bjfu.li.odour.po.Compound;
import com.bjfu.li.odour.po.Log;
import com.bjfu.li.odour.po.Measured;
import com.bjfu.li.odour.po.LowMeasured;
import com.bjfu.li.odour.po.Nri;
import com.bjfu.li.odour.service.INriService;
import com.bjfu.li.odour.service.impl.CompoundServiceImpl;
import com.bjfu.li.odour.service.impl.LogServiceImpl;
import com.bjfu.li.odour.utils.Excel;
import com.bjfu.li.odour.utils.ProExcel;
import com.bjfu.li.odour.utils.ExcelUtils;
import com.bjfu.li.odour.vo.NewsVo;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author li
 * @since 2020-11-06
 */
@RestController
@RequestMapping("/compound")
public class CompoundController {

    @Resource
    CompoundServiceImpl compoundService;

    @Resource
    LogServiceImpl logService;

    @Resource
    NriMapper nri;

    /**
     *
     * @param property 属性
     * @param propertyDescription 属性描述
     * @return 搜索结果
     */
    @PostMapping("/search")
    public SverResponse<List<Compound>> searchCompounds(@RequestParam String property, @RequestParam String propertyDescription){
        List<Compound> compounds=compoundService.searchCompounds(property,propertyDescription);
        return SverResponse.createRespBySuccess(compounds);
    }

    @PostMapping("/download")
    public SverResponse<List<Compound>> downloadCompounds(@RequestBody List<Compound> compounds,HttpServletResponse response) throws Exception {
        System.out.println(response);
        Excel excel=new Excel();
        String strResult=excel.createExcelFile(response,compounds);

           return SverResponse.createRespBySuccess();
    }


    @PostMapping("/downloadpro")
    public void downloadProCompounds(@RequestBody  Map<String,List<String>> dataMap,HttpServletResponse response) throws Exception {
        System.out.println(dataMap);
        List<Compound> compounds=new ArrayList<>();
        List<String> needSheet = dataMap.get("check");

        for(int i=0;i<dataMap.get("cas").size();i++) {
           List<Compound> compound = compoundService.searchByCasPro(dataMap.get("cas").get(i));
           if(compound.isEmpty()){
                continue;
            }
           else {
           compounds.add(compound.get(0));}
        }

        System.out.println(compounds.get(1));
        System.out.println(compounds.get(1).getCasNo());

        ProExcel excel=new ProExcel();
        String strResult=excel.createExcelFile(response,compounds,needSheet);
//        return SverResponse.createRespBySuccess();




    }
    /**
     *
     * @param properties 多个属性及其描述
     * @return 搜索结果
     */
    @PostMapping("/advanced")
    public SverResponse<List<Compound>> advancedSearch(@RequestParam Map<String,String> properties){
        List<Compound> compounds=compoundService.advancedSearch(properties);
        return SverResponse.createRespBySuccess(compounds);
    }

    @GetMapping("/{id}")
    public SverResponse<List<Compound>> getCompound(@PathVariable Integer id){
        Compound compound=compoundService.getById(id);
        List<Compound> compoundList=new ArrayList<>();
        compoundList.add(compound);
        return SverResponse.createRespBySuccess(compoundList);
    }

    /**
     *
     * @param compound 化合物信息
     * @param request
     * @return success or error
     */
    @PostMapping("/add")
    public SverResponse<String> addCompound(@RequestBody Compound compound, HttpServletRequest request){
        System.out.println(compound.getChemicalStructure());
        System.out.println(compound.getChemicalStructure());
        if(compoundService.save(compound)) {
            String token= request.getHeader("Authorization");
            DecodedJWT verify=JWTUtils.verify(token);
            Integer adminId=Integer.valueOf(verify.getClaim("id").asString());
            Log log=new Log(null,"Create",compound.getId(),adminId, LocalDateTime.now());
            logService.save(log);
            return SverResponse.createRespBySuccess();
        }else
            return SverResponse.createRespByError();
    }


    /**
     *
     * @param id 化合物Id
     * @param request
     * @return success or error
     */

    @DeleteMapping("/delete/{id}")
    public SverResponse<String> deleteCompound(@PathVariable Integer id, HttpServletRequest request){
        if(compoundService.removeById(id)){
            String token= request.getHeader("Authorization");
            DecodedJWT verify=JWTUtils.verify(token);
            Integer adminId=Integer.valueOf(verify.getClaim("id").asString());
            Log log=new Log(null,"Delete",id,adminId, LocalDateTime.now());
            logService.save(log);
            return SverResponse.createRespBySuccess();
        }else
            return SverResponse.createRespByError();
    }

    /**
     *
     * @param compound 更新后的化合物信息
     * @param request
     * @return success or error
     */

    @PostMapping("/update")
    public SverResponse<String> updateCompound(@RequestBody Compound compound,HttpServletRequest request){
        System.out.println(compound.getChemicalStructure());
        System.out.println(compound.getChemicalStructure());
        if(compoundService.updateById(compound)){
            String token= request.getHeader("Authorization");
            DecodedJWT verify=JWTUtils.verify(token);
            Integer adminId=Integer.valueOf(verify.getClaim("id").asString());
            Log log=new Log(null,"Update",compound.getId(),adminId, LocalDateTime.now());

            logService.save(log);
            return SverResponse.createRespBySuccess();
        }else
            return SverResponse.createRespByError();
    }

    /**
     *
     * @return 所有化合物信息
     */
    @GetMapping("/all")
    public SverResponse<List<Compound>> getCompounds(){
        List<Compound> compounds=compoundService.list();
        for (Compound c:compounds){
            System.out.println("---- ");

        }

        return SverResponse.createRespBySuccess(compounds);
    }


    /**
     *
     * @return update or create news
     */
    @GetMapping("/news")
    public SverResponse<List<NewsVo>> getNews(){
        List<Compound> compounds=compoundService.getNews();
        List<NewsVo> news=new ArrayList<>();
        for(Compound c:compounds){
            String content;
            if(c.getUpdateTime().equals(c.getCreateTime()))
                content=c.getCompoundName()+" has been added.";
            else
                content=c.getCompoundName()+" has been updated.";
            news.add(new NewsVo(c.getId(),c.getUpdateTime(),content));
        }
        return SverResponse.createRespBySuccess(news);
    }

    @PostMapping("/mr")
    public SverResponse<List<Measured>> readMRExcel(@RequestParam MultipartFile mrExcel) throws IOException {
        String fileName = mrExcel.getOriginalFilename();
        String uploadPath = System.getProperty("user.dir")+"/"+fileName;

        File file = new File(uploadPath);
        FileOutputStream out = new FileOutputStream(uploadPath);
        out.write(mrExcel.getBytes());
        out.flush();
        out.close();
        List<Measured> mrList= ExcelUtils.readXls(uploadPath);
        file.delete();

        return SverResponse.createRespBySuccess(mrList);
    }



    @PostMapping("/lowmr")
    public SverResponse<List<LowMeasured>> readLMRExcel(@RequestParam MultipartFile lmrExcel) throws IOException {
        String fileName = lmrExcel.getOriginalFilename();
        String uploadPath = System.getProperty("user.dir")+"/"+fileName;

        File file = new File(uploadPath);
        FileOutputStream out = new FileOutputStream(uploadPath);
        out.write(lmrExcel.getBytes());
        out.flush();
        out.close();
        List<LowMeasured> lmrList= ExcelUtils.readXls1(uploadPath);
        file.delete();

        return SverResponse.createRespBySuccess(lmrList);
    }

//    @RequestMapping("/export")
//    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        excelService.exportData(request,response);
//    }

}