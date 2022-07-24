package com.bjfu.li.odour.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjfu.li.odour.mapper.*;
import com.bjfu.li.odour.po.*;
import com.bjfu.li.odour.service.IOilService;
import com.bjfu.li.odour.utils.Base64Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class OilServiceImpl extends ServiceImpl<OilMapper, Oil> implements IOilService {


    @Resource
    OilMapper oilMapper;

    @Resource
    OilKeyMapper okMapper;


    @Resource
    OilOdourMapper ooMapper;

    @Override
    public List<Oil> searchOils(String property, String propertyDescription) {
        propertyDescription=propertyDescription.trim();
        switch (property) {
            case "oil_name":
                return oilMapper.selectByOilName(propertyDescription);
            case "oil_type":
                return oilMapper.selectByOilType(propertyDescription);
            case "oil_brand":
                return oilMapper.selectByOilBrand(propertyDescription);
            case "key_compound_cas":
                String cas = new String(propertyDescription);

                return oilMapper.selectByCas(propertyDescription);
            case "oil_odour":
                String description = new String(propertyDescription);
                return oilMapper.selectByOilOdour(propertyDescription);
        }

        return null;
    }




    public Oil getById(Integer id){
        return oilMapper.selectOne(id);
    }

    @Override
    public boolean save(Oil oil) {
        try {

            oil.setIsDeleted(0);

            System.out.println(oil);
            oilMapper.insert(oil);


            //关键呈香化合物
        if(oil.getOkList().size()==0)
            okMapper.insert(new OilKey(null,null,null,null,null,oil.getId()));
        else{
            int validNum=0;
            for(OilKey oilKey:oil.getOkList()){
                if(oilKey.getConcentration().doubleValue()==0&& oilKey.getKeycompoundName()==null&& oilKey.getCasNo()==null&& oilKey.getOkNote()==null)
                    continue;
//                if(oilKey.getConcentration()==null|| oilKey.getConcentration().intValue()==0)
//                    continue;
                validNum++;
                oilKey.setOilId(oil.getId());
                okMapper.insert(oilKey);
            }
            if(validNum==0)
                okMapper.insert(new OilKey(null,null,null,null,null,oil.getId()));
        }

           //植物油属性
            if(oil.getOoList().size()==0)
                ooMapper.insert(new OilOdour(null,null,null,null,oil.getId()));
            else{
                int validNum=0;
                for(OilOdour oilOdour:oil.getOoList()){
                    if( oilOdour.getOilOdour()==null&&oilOdour.getOoNote()==null)
                        continue;
//                    if(oilOdour.getOilOdourIntensity()==null|| oilOdour.getOilOdourIntensity().intValue()==0)
//                        continue;
                    validNum++;
                    oilOdour.setOilId(oil.getId());
                    ooMapper.insert(oilOdour);
                }
                if(validNum==0)
                    ooMapper.insert(new OilOdour(null,null,null,null,oil.getId()));
            }

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateById(Oil oil) {

        Oil _oil=null;


        try {
            if(oil.getOilName()==null)
                oil.setOilName(null);
            oilMapper.updateById(oil);

            if(oil.getOilBrand()==null)
                oil.setOilBrand(null);
            oilMapper.updateById(oil);

            if(oil.getOilType()==null)
                oil.setOilType(null);
            oilMapper.updateById(oil);

            if(oil.getOilDate()==null)
                oil.setOilDate(null);
            oilMapper.updateById(oil);

//更新ok

            QueryWrapper<OilKey> okQueryWrapper=new QueryWrapper<>();
            okQueryWrapper.eq("oil_id",oil.getId());
            okMapper.delete(okQueryWrapper);

            if(oil.getOkList().size()==0)
               okMapper.insert(new OilKey(null,null,null,null,null,oil.getId()));
            else{
                int validNum=0;
                for(OilKey oilKey:oil.getOkList()){
                    if(oilKey.getKeycompoundName()==null||oilKey.getKeycompoundName().equals(""))
                        continue;
                    validNum++;
                    oilKey.setOilId(oil.getId());
                    okMapper.insert(oilKey);
                }
                if(validNum==0)
                    okMapper.insert(new OilKey(null,null,null,null,null,oil.getId()));
            }

            //更新OO
            QueryWrapper<OilOdour> ooQueryWrapper=new QueryWrapper<>();
            ooQueryWrapper.eq("oil_id",oil.getId());
            ooMapper.delete(ooQueryWrapper);

            if(oil.getOoList().size()==0)
                ooMapper.insert(new OilOdour(null,null,null,null,oil.getId()));
            else{
                int validNum=0;
                for(OilOdour oilOdour:oil.getOoList()){
                    if(oilOdour.getOilOdour()==null||oilOdour.getOilOdour().equals(""))
                        continue;
                    validNum++;
                    oilOdour.setOilId(oil.getId());
                    ooMapper.insert(oilOdour);
                }
                if(validNum==0)
                    ooMapper.insert(new OilOdour(null,null,null,null,oil.getId()));
            }


            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public boolean removeById(Integer id){
        Oil oil=oilMapper.selectById(id);
        try {
            QueryWrapper<OilKey> okQueryWrapper=new QueryWrapper<>();
            okQueryWrapper.eq("oil_id",id);
            okMapper.delete(okQueryWrapper);

        QueryWrapper<OilOdour> ooQueryWrapper=new QueryWrapper<>();
        ooQueryWrapper.eq("oil_id",id);
        ooMapper.delete(ooQueryWrapper);


            oilMapper.deleteById(id);



            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }


    @Override
    public List<Oil> list(){
        List<Oil> res= oilMapper.selectAll();
        return res;
    }


}
