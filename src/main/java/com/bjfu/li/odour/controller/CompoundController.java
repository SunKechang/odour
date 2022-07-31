package com.bjfu.li.odour.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bjfu.li.odour.common.pojo.SverResponse;
import com.bjfu.li.odour.common.token.JWTUtils;
import com.bjfu.li.odour.po.Compound;
import com.bjfu.li.odour.po.Log;
import com.bjfu.li.odour.po.LowMeasured;
import com.bjfu.li.odour.po.Measured;
import com.bjfu.li.odour.service.ICompoundService;
import com.bjfu.li.odour.service.impl.LogServiceImpl;
import com.bjfu.li.odour.utils.Excel;
import com.bjfu.li.odour.utils.ExcelUtils;
import com.bjfu.li.odour.utils.PageResult;
import com.bjfu.li.odour.vo.NewsVo;
import com.bjfu.li.odour.vo.SearchVo;
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
    LogServiceImpl logService;
    @Resource
    ICompoundService compoundService;
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
    /**
     * @param searchVo 查询条件
     * @return 搜索结果
     */
    @PostMapping("/search")
    public SverResponse<PageResult> searchCompounds(
            @RequestBody SearchVo searchVo
    ){
        PageResult pageResult = compoundService.searchList(searchVo);
        return SverResponse.createRespBySuccess(pageResult);
    }
    @PostMapping("/download")
    public SverResponse<List<Compound>> downloadCompounds(
            @RequestBody List<Compound> compounds,
            HttpServletResponse response
    ) {
        Excel.createExcelFile(response,compounds);
        return SverResponse.createRespBySuccess();
    }
    /**
     *
     * @param compound 化合物信息
     * @param request request
     * @return success or error
     */
    @PostMapping("/add")
    public SverResponse<String> addCompound(@RequestBody Compound compound, HttpServletRequest request){
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
     * @param request request
     * @return success or error
     */

    @DeleteMapping("/delete/{id}")
    public SverResponse<String> deleteCompound(
            @PathVariable Integer id, HttpServletRequest request
    ){
        if(compoundService.delete(id)){
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
     * @param request request
     * @return success or error
     */
    @PostMapping("/update")
    public SverResponse<String> updateCompound(@RequestBody Compound compound,HttpServletRequest request){
        if(compoundService.update(compound)){
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
     * @return 获取所有化合物列表
     */
    @GetMapping("/all")
    public SverResponse<PageResult> getCompounds(
            @RequestParam(defaultValue = "1", value = "page") Integer page,
            @RequestParam(defaultValue = "10",value = "size") Integer size
    ){
        SearchVo searchVo = new SearchVo();
        searchVo.setPage(page);
        searchVo.setSize(size);
        PageResult pageResult = compoundService.getList(searchVo);
        return SverResponse.createRespBySuccess(pageResult);
    }
    /**
     * 获取单条化合物信息
     */
    @GetMapping("/{id}")
    public SverResponse<Compound> getOne(
            @PathVariable Integer id
    ){
        Compound compound = compoundService.getOne(id);
        return SverResponse.createRespBySuccess(compound);
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
        assert  file.delete();
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
        assert  file.delete();
        return SverResponse.createRespBySuccess(lmrList);
    }
}