package com.bjfu.li.odour.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjfu.li.odour.mapper.*;
import com.bjfu.li.odour.po.*;
import com.bjfu.li.odour.service.ICompoundService;
import com.bjfu.li.odour.utils.Base64Utils;
import com.bjfu.li.odour.utils.PageResult;
import com.bjfu.li.odour.utils.PageUtil;
import com.bjfu.li.odour.vo.CompoundVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
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
    ProductKeyMapper productKeyMapper;
    @Resource
    ProductOdourThresholdMapper productOtMapper;
    @Resource
    ProductOdourDescriptionMapper productOdMapper;



    @Value("${localImgPath}")
    String localImgPath;
    @Value("${networkImgPath}")
    String networkImgPath;

    @Override
    public List<Compound> searchCompounds(String property, String propertyDescription) {
        propertyDescription=propertyDescription.trim();
        switch (property) {
            case "compound_name":
                return compoundMapper.selectByCompoundName(propertyDescription);
            case "odour_description":
                String description = propertyDescription;
                return   compoundMapper.selectByOdourDescription(description);
            case "odour_threshold":
                int odourThreshold= Integer.parseInt(propertyDescription);
                return compoundMapper.selectByOdourThreshold(odourThreshold-10,odourThreshold+10);
            case "cas_no":
                propertyDescription=propertyDescription.replaceAll("-","");
                return compoundMapper.selectByCasNo(propertyDescription);
            case "compound_ri":
                int ri= Integer.parseInt(propertyDescription);
                return compoundMapper.selectByRi(ri-100,ri+100);
            case "compound_nri":
                int nri= Integer.parseInt(propertyDescription);
                return compoundMapper.selectByNri(nri-100,nri+100);
            case "measured":
                double measured= Double.parseDouble(propertyDescription);
                return compoundMapper.selectByMeasured(measured-0.05,measured+0.05);
            case "lowmeasured":
                double lowmeasured= Double.parseDouble(propertyDescription);
                return compoundMapper.selectByLowMeasured(lowmeasured-0.05,lowmeasured+0.05);

        }

        return null;
    }

    public List<Compound> searchByCasPro(String cas){
        cas=cas.replaceAll("-","");
        return compoundMapper.selectByCasPro(cas);
    }

    @Override
    public List<Compound> advancedSearch(Map<String, String> properties) {
        if(properties.size()==0)
            return list();
        else if(properties.size()==1) {
            String property="",propertyDescription="";
            for(Map.Entry<String,String> e:properties.entrySet()){
                if(e!=null){
                    property=e.getKey();
                    propertyDescription=e.getValue();
                    break;
                }
            }
            return  searchCompounds(property,propertyDescription);
        }

        if(!properties.containsKey("odour_description"))
            return compoundMapper.selectByProperties(properties);
        else{
            QueryWrapper<Compound> compoundQueryWrapper=new QueryWrapper<>();
            for(Map.Entry<String,String> e:properties.entrySet()){
                String property=e.getKey();
                String propertyDescription=e.getValue().trim();
                switch (property) {
                    case "compound_name":
                        compoundQueryWrapper.and(wrapper->wrapper
                                .like(property, propertyDescription)
                                .or()
                                .like("synonym", propertyDescription));
                        break;
                    case "odour_description":
                        String[] descriptions = propertyDescription.split(",");
                        System.out.println(Arrays.toString(descriptions));
                        for (String description : descriptions) {
                            description=description.trim();
                            compoundQueryWrapper.like(property, description);
                        }
                        break;
                    case "cas_no":
                        compoundQueryWrapper.eq(property, propertyDescription);
                        break;
                }
            }
            List<Compound> compounds=compoundMapper.selectList(compoundQueryWrapper);
            for(Compound c:compounds){
                QueryWrapper<Ri> riQueryWrapper=new QueryWrapper<>();
                riQueryWrapper.eq("compound_id",c.getId());
                c.setRiList(riMapper.selectList(riQueryWrapper));

                QueryWrapper<Measured> mrQueryWrapper=new QueryWrapper<>();
                mrQueryWrapper.eq("compound_id",c.getId());
                c.setMrList(measuredMapper.selectList(mrQueryWrapper));
            }
            return compounds;
        }

    }


    public Compound getById(Integer id){
        return compoundMapper.selectOne(id);
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
            // 产品
            if (compound.getProductList().size() != 0) {
                for (Product product:compound.getProductList()) {
                    // Product 和 Compound 对应关系
                    ProductKey productKey = new ProductKey();
                    productKey.setProductId(product.getId());
                    productKey.setCompoundId(compound.getId());
                    // Product 风味阈值
                    List<ProductOdourThreshold> productOtList = product.getOtList();
                    for (ProductOdourThreshold productOt: productOtList) {
                        productOt.setCompoundId(compound.getId());
                        productOtMapper.insert(productOt);
                    }
                    List<ProductOdourDescription> productOdList = product.getOdList();
                    for (ProductOdourDescription productOd: productOdList) {
                        productOd.setCompoundId(compound.getId());
                        productOdMapper.insert(productOd);
                    }
                    // Product 风味描述
                    productKeyMapper.insert(productKey);
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean updateById(Compound compound) {
        String chemicalStructure = compound.getChemicalStructure();
        String massSpectrogram = compound.getMassSpectrogram();
        String massSpectrogramNist = compound.getMassSpectrogramNist();
        Compound _compound= new Compound();
        if(chemicalStructure.startsWith("data")||massSpectrogram.startsWith("data"))
            _compound = compoundMapper.selectById(compound.getId());
        try {
            if(!chemicalStructure.equals("")) {
                //不是base64就说明没有更新
                if(chemicalStructure.startsWith("data")&&_compound!=null){
                    if(_compound.getChemicalStructure().lastIndexOf("/")!=-1) {
                        String chemicalStructureFilename = _compound.getChemicalStructure().substring(_compound.getChemicalStructure().lastIndexOf("/"));
                        File oldChemicalStructure = new File(localImgPath + "chemical structure/" + chemicalStructureFilename);
                        if (oldChemicalStructure.exists())
                            oldChemicalStructure.delete();
                    }
                    chemicalStructure = networkImgPath+"chemical structure/" + Base64Utils.generateImage(chemicalStructure, localImgPath+"chemical structure/");
                    compound.setChemicalStructure(chemicalStructure);
                }
            }
            //逻辑和上面一样
            if(!massSpectrogram.equals("")) {
                if(massSpectrogram.startsWith("data")&&_compound!=null) {
                    if(_compound.getMassSpectrogram().lastIndexOf("/")!=-1) {
                        String massSpectrogramFilename = _compound.getMassSpectrogram().substring(_compound.getMassSpectrogram().lastIndexOf("/"));
                        File oldMassSpectrogram = new File(localImgPath + "Orbitrap-MS mass spectrometry/" + massSpectrogramFilename);
                        if (oldMassSpectrogram.exists())
                            oldMassSpectrogram.delete();
                    }
                    massSpectrogram = networkImgPath+"Orbitrap-MS mass spectrometry/" + Base64Utils.generateImage(massSpectrogram, localImgPath+"Orbitrap-MS mass spectrometry/");
                    compound.setMassSpectrogram(massSpectrogram);
                }
            }

            if(!massSpectrogramNist.equals("")) {
                if(massSpectrogramNist.startsWith("data")&&_compound!=null) {
                    if(_compound.getMassSpectrogramNist().lastIndexOf("/")!=-1) {
                        String massSpectrogramNistFilename = _compound.getMassSpectrogramNist().substring(_compound.getMassSpectrogramNist().lastIndexOf("/"));
                        File oldMassSpectrogramNist = new File(localImgPath + "Low-resolution mass spectrometry/" + massSpectrogramNistFilename);
                        if (oldMassSpectrogramNist.exists())
                            oldMassSpectrogramNist.delete();
                    }
                    massSpectrogramNist = networkImgPath+"Low-resolution mass spectrometry/" + Base64Utils.generateImage(massSpectrogramNist, localImgPath+"Low-resolution mass spectrometry/");
                    compound.setMassSpectrogramNist(massSpectrogramNist);
                }
            }
            compoundMapper.updateById(compound);

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
            }

            // 更新odlist
            QueryWrapper<OdourDescription> odQueryWrapper=new QueryWrapper<>();
            odQueryWrapper.eq("compound_id",compound.getId());
            odMapper.delete(odQueryWrapper);
            for(OdourDescription odourDescription:compound.getOdList()){
                odourDescription.setCompoundId(compound.getId());
                odMapper.insert(odourDescription);
            }

            QueryWrapper<Measured> mrQueryWrapper=new QueryWrapper<>();
            mrQueryWrapper.eq("compound_id",compound.getId());
            measuredMapper.delete(mrQueryWrapper);
            for(Measured measured:compound.getMrList()){
                measured.setCompoundId(compound.getId());
                measuredMapper.insert(measured);
            }

            QueryWrapper<LowMeasured> lmrQueryWrapper=new QueryWrapper<>();
            lmrQueryWrapper.eq("compound_id",compound.getId());
            lowmeasuredMapper.delete(lmrQueryWrapper);
            for(LowMeasured lowmeasured:compound.getLowmrList()){
                lowmeasured.setCompoundId(compound.getId());
                lowmeasuredMapper.insert(lowmeasured);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public boolean removeById(Integer id){
        Compound compound=compoundMapper.selectById(id);
        try {
            if(compound.getChemicalStructure().lastIndexOf("/")!=-1) {
                String chemicalStructureFilename = compound.getChemicalStructure().substring(compound.getChemicalStructure().lastIndexOf("/"));
                File oldChemicalStructure = new File(localImgPath+"chemical structure/" + chemicalStructureFilename);
                if (oldChemicalStructure.exists())
                    oldChemicalStructure.delete();
            }
            if(compound.getMassSpectrogram().lastIndexOf("/")!=-1) {
                String massSpectrogramFilename = compound.getMassSpectrogram().substring(compound.getMassSpectrogram().lastIndexOf("/"));
                File oldMassSpectrogram = new File(localImgPath+"Orbitrap-MS mass spectrometry/" + massSpectrogramFilename);
                if (oldMassSpectrogram.exists())
                    oldMassSpectrogram.delete();
            }

            if(compound.getMassSpectrogramNist().lastIndexOf("/")!=-1) {
                String massSpectrogramNistFilename = compound.getMassSpectrogramNist().substring(compound.getMassSpectrogramNist().lastIndexOf("/"));
                File oldMassSpectrogramNist = new File(localImgPath+"Low-resolution mass spectrometry/" + massSpectrogramNistFilename);
                if (oldMassSpectrogramNist.exists())
                    oldMassSpectrogramNist.delete();
            }

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
            compoundMapper.deleteById(id);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public PageResult getList(Integer current, Integer size){
        PageHelper.startPage(current, size);
        List<Compound> compoundList = compoundMapper.selectAll();
        PageInfo<Compound> pageInfo = new PageInfo<>(compoundList);
        return PageUtil.getPageResult(pageInfo);
    }
    @Override
    public List<Compound> getNews() {
        return compoundMapper.selectNewsList();
    }

}
