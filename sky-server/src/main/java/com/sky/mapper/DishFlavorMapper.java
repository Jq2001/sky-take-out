package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

//    void add(DishFlavor dishFlavor);

    void add(List<DishFlavor> flavorList);

    void deleteBatch(List<Long> ids);

    List<DishFlavor> selectById(Long id);

    /**
     * 根据菜品id删除口味
     * @param id
     */
    void deleteByDishId(Long id);
}

