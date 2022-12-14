package com.bjfu.li.odour.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjfu.li.odour.article.mapper.ArticleMapper;
import com.bjfu.li.odour.article.po.Article;
import com.bjfu.li.odour.base.mapper.BaseDao;
import com.bjfu.li.odour.base.po.Base;
import com.bjfu.li.odour.mapper.*;
import com.bjfu.li.odour.po.*;
import com.bjfu.li.odour.review.mapper.ReviewerMapper;
import com.bjfu.li.odour.service.ICompoundService;
import com.bjfu.li.odour.utils.Base64Utils;
import com.bjfu.li.odour.utils.PageResult;
import com.bjfu.li.odour.utils.PageUtil;
import com.bjfu.li.odour.vo.BasesVo;
import com.bjfu.li.odour.vo.DownloadVo;
import com.bjfu.li.odour.vo.SearchVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author li
 * @since 2020-11-06
 */
@Service
public class CompoundServiceImpl extends ServiceImpl<CompoundMapper, Compound> implements ICompoundService {

    @Resource
    CompoundMapper compoundMapper;

    @Resource
    RiMapper riMapper;

    @Resource
    NriMapper nriMapper;

    @Resource
    OdourDescriptionMapper odMapper;

    @Resource
    OdourThresholdMapper otMapper;

    @Resource
    MeasuredMapper measuredMapper;

    @Resource
    LowMeasuredMapper lowmeasuredMapper;
    @Resource
    ArticleMapper articleMapper;
    @Resource
    ReviewerMapper reviewerMapper;
    @Resource
    OdourIntensityFunctionMapper functionMapper;
    @Resource
    BaseDao baseDao;

    @Value("${localImgPath}")
    String localImgPath;
    @Value("${networkImgPath}")
    String networkImgPath;
    @Override
    public List<DownloadVo> searchByCasPro(String cas){
        cas=cas.replaceAll("-","");
        return compoundMapper.selectByCasUltra(cas);
    }
    @Override
    public List<Compound> advancedSearch(Map<String, String> properties) {
        return compoundMapper.advancedSearch(properties);
    }
    @Override
    public PageResult searchList(SearchVo searchVo) {
        String propertyDescription = searchVo.getSearchValue().toString().trim();
        String property = searchVo.getSearchProperty();
        List<Compound> compoundList;
        PageHelper.startPage(searchVo.getPage(), searchVo.getSize());
        switch (property) {
            case "odour_description":
                compoundList = compoundMapper.selectByOdourDescription(propertyDescription, searchVo.getSearchKind(), searchVo.getBase());
                break;
            case "odour_threshold":
                int odourThreshold= Integer.parseInt(propertyDescription);
                compoundList = compoundMapper.selectByOdourThreshold(odourThreshold-10,odourThreshold+10, searchVo.getSearchKind(), searchVo.getBase());
                break;
            case "compound_ri":
                int ri= Integer.parseInt(propertyDescription);
                compoundList = compoundMapper.selectByRi(ri-100,ri+100);
                break;
            case "compound_nri":
                int nri= Integer.parseInt(propertyDescription);
                compoundList = compoundMapper.selectByNri(nri-100,nri+100);
                break;
            case "measured":
                double measured= Double.parseDouble(propertyDescription);
                compoundList = compoundMapper.selectByMeasured(measured-0.05,measured+0.05);
                break;
            case "lowmeasured":
                double lowmeasured= Double.parseDouble(propertyDescription);
                compoundList = compoundMapper.selectByLowMeasured(lowmeasured-0.05,lowmeasured+0.05);
                break;
            case "product":
                compoundList = compoundMapper.selectByProduct(propertyDescription);
                break;
            default:
                searchVo.setSearchRule("like");
                searchVo.setSearchValue(propertyDescription);
                searchVo.setSearchProperty(property);
//                PageHelper.startPage(searchVo.getPage(), searchVo.getSize());
                compoundList = this.dynamicSelect(searchVo);
        }
        PageInfo<Compound> pageInfo = new PageInfo<>(compoundList);
        return PageUtil.getPageResult(pageInfo);
    }

    @Override
    public boolean save(Compound compound) {
        String chemicalStructure = compound.getChemicalStructure();
        String massSpectrogram = compound.getMassSpectrogram();
        String massSpectrogramNist = compound.getMassSpectrogramNist();
        try {
            // 存图片
            if(chemicalStructure != null && !chemicalStructure.equals("")) {
                chemicalStructure = networkImgPath + "chemical structure/" + Base64Utils.generateImage(chemicalStructure, localImgPath+"chemical structure");
                compound.setChemicalStructure(chemicalStructure);
            }
            if(massSpectrogram != null &&! massSpectrogram.equals("")) {
                massSpectrogram = networkImgPath+"Orbitrap-MS mass spectrometry/" + Base64Utils.generateImage(massSpectrogram, localImgPath+"Orbitrap-MS mass spectrometry");
                compound.setMassSpectrogram(massSpectrogram);
            }
            if(massSpectrogramNist!=null&&!massSpectrogramNist.equals("")) {
                massSpectrogramNist = networkImgPath+"Low-resolution mass spectrometry/" + Base64Utils.generateImage(massSpectrogramNist, localImgPath+"Low-resolution mass spectrometry");
                compound.setMassSpectrogramNist(massSpectrogramNist);
            }
            compoundMapper.insert(compound);
            //风味描述
            for(OdourDescription odourDescription:compound.getOdList()){
                odourDescription.setCompoundId(compound.getId());
                odMapper.insert(odourDescription);
            }
            //风味阈值
            for(OdourThreshold odourThreshold:compound.getOtList()){
                odourThreshold.setCompoundId(compound.getId());
                otMapper.insert(odourThreshold);
                List<Base> baseList = baseDao.selectByName(odourThreshold.getOdourBase());
                if(baseList.size() == 0) {
                    Base base = new Base();
                    base.setBase(odourThreshold.getOdourBase());
                    base.setKind(1);
                    baseDao.insert(base);
                } else {
                    baseDao.incrByOne(baseList.get(0).getId());
                }
            }
            // RI
            for(Ri ri:compound.getRiList()){
                ri.setCompoundId(compound.getId());
                riMapper.insert(ri);
            }
            // NRI
            for(Nri nri:compound.getNriList()){
                nri.setCompoundId(compound.getId());
                nriMapper.insert(nri);
            }

            // 离子碎片和相对丰度
            for(Measured measured :compound.getMrList()){
                measured.setCompoundId(compound.getId());
                measuredMapper.insert(measured);
            }
            //低精度离子碎片和相对丰度
            for(LowMeasured lowMeasured :compound.getLowmrList()){
                lowMeasured.setCompoundId(compound.getId());
                lowmeasuredMapper.insert(lowMeasured);
            }
            //强度函数
            for(OdourIntensityFunction function : compound.getFunctionList()) {
                String functionImg = function.getFunctionImg();
                if(functionImg != null && !functionImg.equals("")) {
                    functionImg = networkImgPath + "intensity function/" + Base64Utils.generateImage(functionImg, localImgPath+"intensity function");
                    function.setFunctionImg(functionImg);
                }
                function.setCompoundId(compound.getId());
                functionMapper.insert(function);

                List<Base> baseList2 = baseDao.selectByName(function.getOdourBase());
                if(baseList2.size() == 0) {
                    Base base = new Base();
                    base.setBase(function.getOdourBase());
                    base.setKind(2);
                    baseDao.insert(base);
                } else {
                    baseDao.incrByOne(baseList2.get(0).getId());
                }
            }
            // 产品
//            return insertProducts(compound);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }



    @Override
    public boolean update(Compound compound) {
        String chemicalStructure = compound.getChemicalStructure() == null ? "" : compound.getChemicalStructure();
        String massSpectrogram = compound.getMassSpectrogram() == null ? "" : compound.getMassSpectrogram();
        String massSpectrogramNist = compound.getMassSpectrogramNist() == null ? "" : compound.getMassSpectrogramNist();
        Compound _compound= new Compound();
        if(chemicalStructure.startsWith("data")||massSpectrogram.startsWith("data"))
            _compound = compoundMapper.selectById(compound.getId());
        try {
            // 上传了 chemicalStructure 且是一个有效的base64
            if(!chemicalStructure.equals("") & chemicalStructure.startsWith("data")) {
                // 数据库中该数据存在
                assert _compound != null;
                // 文件地址存在且能够争取解析
                delChemicalStructure(_compound);
                // 保存新的文件并更新字段
                chemicalStructure = networkImgPath + "chemical structure/" + Base64Utils.generateImage(chemicalStructure, localImgPath+"chemical structure/");
                compound.setChemicalStructure(chemicalStructure);
            }
            //逻辑和上面一样
            if(!massSpectrogram.equals("") & massSpectrogram.startsWith("data")) {
                assert _compound != null;
                delMassSpectrogram(_compound);
                massSpectrogram = networkImgPath+"Orbitrap-MS mass spectrometry/" + Base64Utils.generateImage(massSpectrogram, localImgPath+"Orbitrap-MS mass spectrometry/");
                compound.setMassSpectrogram(massSpectrogram);
            }

            if(!massSpectrogramNist.equals("") & massSpectrogramNist.startsWith("data")) {
                assert _compound != null;
                delMassSpectrogramNist(_compound);
                massSpectrogramNist = networkImgPath+"Low-resolution mass spectrometry/" + Base64Utils.generateImage(massSpectrogramNist, localImgPath+"Low-resolution mass spectrometry/");
                compound.setMassSpectrogramNist(massSpectrogramNist);
            }
            compound.setUpdateTime(LocalDateTime.now());
            compoundMapper.updateOne(compound);

            // 更新ri
            QueryWrapper<Ri> riQueryWrapper=new QueryWrapper<>();
            riQueryWrapper.eq("compound_id",compound.getId());
            riMapper.delete(riQueryWrapper);
            for(Ri ri:compound.getRiList()){
                ri.setCompoundId(compound.getId());
                riMapper.insert(ri);
            }

            // 更新nri
            QueryWrapper<Nri> nriQueryWrapper=new QueryWrapper<>();
            nriQueryWrapper.eq("compound_id",compound.getId());
            nriMapper.delete(nriQueryWrapper);
            for(Nri nri:compound.getNriList()){
                nri.setCompoundId(compound.getId());
                nriMapper.insert(nri);
            }

            // 更新阈值
            QueryWrapper<OdourThreshold> otQueryWrapper=new QueryWrapper<>();
            otQueryWrapper.eq("compound_id",compound.getId());
            otMapper.delete(otQueryWrapper);
            for(OdourThreshold odourThreshold:compound.getOtList()){
                odourThreshold.setCompoundId(compound.getId());
                otMapper.insert(odourThreshold);

                List<Base> baseList = baseDao.selectByName(odourThreshold.getOdourBase());
                if(baseList.size() == 0) {
                    Base base = new Base();
                    base.setBase(odourThreshold.getOdourBase());
                    base.setKind(1);
                    baseDao.insert(base);
                } else {
                    baseDao.incrByOne(baseList.get(0).getId());
                }
            }

            // 更新odlist
            QueryWrapper<OdourDescription> odQueryWrapper=new QueryWrapper<>();
            odQueryWrapper.eq("compound_id",compound.getId());
            odMapper.delete(odQueryWrapper);
            for(OdourDescription odourDescription:compound.getOdList()){
                odourDescription.setCompoundId(compound.getId());
                odMapper.insert(odourDescription);
            }
            // 更新 mr
            QueryWrapper<Measured> mrQueryWrapper=new QueryWrapper<>();
            mrQueryWrapper.eq("compound_id",compound.getId());
            measuredMapper.delete(mrQueryWrapper);
            for(Measured measured:compound.getMrList()){
                measured.setCompoundId(compound.getId());
                measuredMapper.insert(measured);
            }
            // 更新 LowMeasured
            QueryWrapper<LowMeasured> lmrQueryWrapper=new QueryWrapper<>();
            lmrQueryWrapper.eq("compound_id",compound.getId());
            lowmeasuredMapper.delete(lmrQueryWrapper);
            for(LowMeasured lowmeasured:compound.getLowmrList()){
                lowmeasured.setCompoundId(compound.getId());
                lowmeasuredMapper.insert(lowmeasured);
            }

            QueryWrapper<OdourIntensityFunction> oifQueryWrapper=new QueryWrapper<>();
            oifQueryWrapper.eq("compound_id",compound.getId());
            functionMapper.delete(oifQueryWrapper);
            for(OdourIntensityFunction function:compound.getFunctionList()){
                function.setCompoundId(compound.getId());
                String functionImg = function.getFunctionImg();
                if(functionImg != null && !functionImg.equals("") && functionImg.startsWith("data")) {
                    functionImg = networkImgPath + "intensity function/" + Base64Utils.generateImage(functionImg, localImgPath+"intensity function");
                    function.setFunctionImg(functionImg);
                }
                functionMapper.insert(function);

                List<Base> baseList = baseDao.selectByName(function.getOdourBase());
                if(baseList.size() == 0) {
                    Base base = new Base();
                    base.setBase(function.getOdourBase());
                    base.setKind(2);
                    baseDao.insert(base);
                } else {
                    baseDao.incrByOne(baseList.get(0).getId());
                }
            }
            // 更新 product
//            deleteProductInfo(compound);
//            return insertProducts(compound);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void delMassSpectrogramNist(Compound _compound) {
        if(_compound.getMassSpectrogramNist()!= null) {
            if (_compound.getMassSpectrogramNist().lastIndexOf("/")!=-1) {
                String massSpectrogramNistFilename = _compound.getMassSpectrogramNist().substring(_compound.getMassSpectrogramNist().lastIndexOf("/"));
                File oldMassSpectrogramNist = new File(localImgPath + "Low-resolution mass spectrometry/" + massSpectrogramNistFilename);
                assert !oldMassSpectrogramNist.exists() || oldMassSpectrogramNist.delete();
            }
        }
    }

    private void delMassSpectrogram(Compound _compound) {
        if(_compound.getMassSpectrogram()!= null) {
            if (_compound.getMassSpectrogram().lastIndexOf("/")!=-1) {
                String massSpectrogramFilename = _compound.getMassSpectrogram().substring(_compound.getMassSpectrogram().lastIndexOf("/"));
                File oldMassSpectrogram = new File(localImgPath + "Orbitrap-MS mass spectrometry/" + massSpectrogramFilename);
                assert !oldMassSpectrogram.exists() || oldMassSpectrogram.delete();
            }
        }
    }

    private void delChemicalStructure(Compound _compound) {
        if(_compound.getChemicalStructure() != null) {
            if (_compound.getChemicalStructure().lastIndexOf("/") != -1) {
                // 文件名
                String chemicalStructureFilename = _compound.getChemicalStructure().substring(_compound.getChemicalStructure().lastIndexOf("/"));
                File oldChemicalStructure = new File(localImgPath + "chemical structure/" + chemicalStructureFilename);
                // 删除原文件成功
                assert !oldChemicalStructure.exists() || oldChemicalStructure.delete();
            }
        }
    }


    public boolean delete(Integer id){
        Compound compound=compoundMapper.selectOne(id);
        try {
            delChemicalStructure(compound);
            delMassSpectrogram(compound);
            delMassSpectrogramNist(compound);

            QueryWrapper<Measured> mrQueryWrapper=new QueryWrapper<>();
            mrQueryWrapper.eq("compound_id",id);
            measuredMapper.delete(mrQueryWrapper);

            QueryWrapper<LowMeasured> lowMeasuredQueryWrapper=new QueryWrapper<>();
            lowMeasuredQueryWrapper.eq("compound_id",id);
            lowmeasuredMapper.delete(lowMeasuredQueryWrapper);

            QueryWrapper<Ri> riQueryWrapper=new QueryWrapper<>();
            riQueryWrapper.eq("compound_id",id);
            riMapper.delete(riQueryWrapper);

            QueryWrapper<Nri> nriQueryWrapper=new QueryWrapper<>();
            nriQueryWrapper.eq("compound_id",id);
            nriMapper.delete(nriQueryWrapper);

            QueryWrapper<OdourDescription> odQueryWrapper=new QueryWrapper<>();
            odQueryWrapper.eq("compound_id",id);
            odMapper.delete(odQueryWrapper);

            QueryWrapper<OdourThreshold> otQueryWrapper=new QueryWrapper<>();
            otQueryWrapper.eq("compound_id",id);
            otMapper.delete(otQueryWrapper);
            compoundMapper.deleteOne(id);

//            deleteProductInfo(compound);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public PageResult getList(SearchVo searchVo){
        PageHelper.startPage(searchVo.getPage(), searchVo.getSize());
        List<Compound> compoundList = compoundMapper.selectAll();
        PageInfo<Compound> pageInfo = new PageInfo<>(compoundList);
        return PageUtil.getPageResult(pageInfo);
    }

    @Override
    public Compound getOne(Integer id) {
        Compound compound = compoundMapper.selectOne(id);
        // odList
        compound.setOdList(odMapper.selectByCompoundId(id));
        // otList
        compound.setOtList(otMapper.selectByCompoundId(id));

        compound.setFunctionList(functionMapper.selectByCompoundId(id));
        // riList
        QueryWrapper<Ri> riQueryWrapper=new QueryWrapper<>();
        riQueryWrapper.eq("compound_id", id);
        compound.setRiList(riMapper.selectList(riQueryWrapper));
        // nriList
        QueryWrapper<Nri> nriQueryWrapper=new QueryWrapper<>();
        nriQueryWrapper.eq("compound_id", id);
        compound.setNriList(nriMapper.selectList(nriQueryWrapper));
        // mrList
        QueryWrapper<Measured> mrQueryWrapper=new QueryWrapper<>();
        mrQueryWrapper.eq("compound_id", id);
        compound.setMrList(measuredMapper.selectList(mrQueryWrapper));
        // lowmrList
        QueryWrapper<LowMeasured> lowmrQueryWrapper=new QueryWrapper<>();
        lowmrQueryWrapper.eq("compound_id", id);
        compound.setLowmrList(lowmeasuredMapper.selectList(lowmrQueryWrapper));
        // productList
        QueryWrapper<ProductKey> keyQueryWrapper = new QueryWrapper<>();
        keyQueryWrapper.eq("compound_id", id);
        String reviewerName = reviewerMapper.getNameByEmail(compound.getReviewer());
        compound.setReviewerName(reviewerName);
        return compound;
    }

    @Override
    public List<Compound> dynamicSelect(SearchVo searchVo) {
        //为了测试连表和前后端字段名不一致，可省略
        if (StringUtils.equalsIgnoreCase("id", searchVo.getSearchProperty())){
            searchVo.setSearchProperty("compound.id");
        }else if (StringUtils.equalsIgnoreCase("compound_name", searchVo.getSearchProperty())) {
            searchVo.setSearchProperty("compound.compound_name");
        }
        return compoundMapper.dynamicSelect(searchVo);
    }

    @Override
    public BasesVo getAllBase() {
        BasesVo basesVo = new BasesVo();
        basesVo.setThresholdBase(otMapper.getThresholdBase());
        basesVo.setFunctionBase(otMapper.getFunctionBase());
        return basesVo;
    }

    @Override
    public List<Compound> getNews() {
        return compoundMapper.selectNewsList();
    }

}
