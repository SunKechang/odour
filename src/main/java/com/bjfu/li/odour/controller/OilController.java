package com.bjfu.li.odour.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bjfu.li.odour.common.pojo.SverResponse;
import com.bjfu.li.odour.common.token.JWTUtils;
import com.bjfu.li.odour.po.Oil;
import com.bjfu.li.odour.po.OilKey;
import com.bjfu.li.odour.po.OilOdour;
import com.bjfu.li.odour.service.impl.OilServiceImpl;
import com.bjfu.li.odour.utils.OilExcel;
import com.bjfu.li.odour.utils.ExcelUtils;
import com.bjfu.li.odour.vo.NewsVo;
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


@RestController
@RequestMapping("/oil")
public class OilController {

    @Resource
    OilServiceImpl oilService;





    @PostMapping("/searchoil")
    public SverResponse<List<Oil>> searchOils(@RequestParam String property, @RequestParam String propertyDescription){
        List<Oil> oil=oilService.searchOils(property,propertyDescription);
        return SverResponse.createRespBySuccess(oil);
    }


    @GetMapping("/{id}")
    public SverResponse<List<Oil>> getOil(@PathVariable Integer id){
        Oil oil=oilService.getById(id);
        List<Oil> oilList=new ArrayList<>();
        oilList.add(oil);
        return SverResponse.createRespBySuccess(oilList);
    }


    @PostMapping("/add")
    public SverResponse<String> addOil(@RequestBody Oil oil, HttpServletRequest request){
        if(oilService.save(oil)) {
            String token= request.getHeader("Authorization");
            DecodedJWT verify=JWTUtils.verify(token);
            Integer adminId=Integer.valueOf(verify.getClaim("id").asString());
            return SverResponse.createRespBySuccess();
        }else
            return SverResponse.createRespByError();
    }



    @DeleteMapping("/delete/{id}")
    public SverResponse<String> deleteOil(@PathVariable Integer id, HttpServletRequest request){
        if(oilService.removeById(id)){
            String token= request.getHeader("Authorization");
            DecodedJWT verify=JWTUtils.verify(token);
            Integer adminId=Integer.valueOf(verify.getClaim("id").asString());
//            Log log=new Log(null,"Delete",id,adminId, LocalDateTime.now());
//            logService.save(log);
            return SverResponse.createRespBySuccess();
        }else
            return SverResponse.createRespByError();
    }



    @PostMapping("/update")
    public SverResponse<String> updateOil(@RequestBody Oil oil,HttpServletRequest request){
        if(oilService.updateById(oil)){
            String token= request.getHeader("Authorization");
            DecodedJWT verify=JWTUtils.verify(token);
            Integer adminId=Integer.valueOf(verify.getClaim("id").asString());


            return SverResponse.createRespBySuccess();
        }else
            return SverResponse.createRespByError();
    }
    @PostMapping("/download")
    public SverResponse<List<Oil>> downloadOils(@RequestBody List<Oil> oils, HttpServletResponse response) throws Exception {

        OilExcel excel=new OilExcel();
        String strResult=excel.createExcelFile(response,oils);

        return SverResponse.createRespBySuccess();

    }

    @GetMapping("/all")
    public SverResponse<List<Oil>> getOils(){
        List<Oil> oils=oilService.list();
        for (Oil c:oils){
            System.out.println("-1--- ");

        }

        return SverResponse.createRespBySuccess(oils);
    }





}