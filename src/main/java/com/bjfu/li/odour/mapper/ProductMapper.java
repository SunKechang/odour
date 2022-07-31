package com.bjfu.li.odour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfu.li.odour.po.Compound;
import com.bjfu.li.odour.po.Product;
import com.bjfu.li.odour.vo.SearchVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper extends BaseMapper<Product> {
    List<Product> selectAll();
    List<Product> selectTop(@Param("num") Integer num);
    List<Product> dynamicSelect(List<SearchVo> searchVoList);
    List<Compound> selectCompound(SearchVo searchVo);

    List<Compound> selectCompoundByOdourDescription(
            @Param("productId") Integer productId,
            @Param("odourDescription") String odourDescription
    );
    List<Compound> selectCompoundByOdourThreshold(
            @Param("productId") Integer productId,
            @Param("low") int low,
            @Param("height") int height
    );
    List<Compound> selectCompoundByRi(
            @Param("productId") Integer productId,
            @Param("low") int low,
            @Param("height") int height
    );

    List<Compound> selectCompoundByNri(
            @Param("productId") Integer productId,
            @Param("low") int low,
            @Param("height") int height
    );

    List<Compound> selectCompoundByMeasured(
            @Param("productId") Integer productId,
            @Param("low") double low,
            @Param("height") double height
    );

    List<Compound> selectCompoundByLowMeasured(
            @Param("productId") Integer productId,
            @Param("low") double low,
            @Param("height") double height
    );
}
