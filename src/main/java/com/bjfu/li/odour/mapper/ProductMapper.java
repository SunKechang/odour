package com.bjfu.li.odour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfu.li.odour.po.Compound;
import com.bjfu.li.odour.po.Product;
import com.bjfu.li.odour.vo.SearchVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public interface ProductMapper extends BaseMapper<Product> {
    List<Product> selectAll();
    List<Product> selectTop(@Param("num") Integer num);
    List<Product> dynamicSelect(List<SearchVo> searchVoList);
}
