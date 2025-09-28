package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "菜品接口")
@RequestMapping("/admin/dish")
@RestController
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @ApiOperation("新增菜品")
    @PostMapping
    public Result add(@RequestBody DishDTO dishDTO){
        dishService.add(dishDTO);
        return  Result.success();
    }

    /**
     * 分页查询菜品列表
     * @param dishPageQueryDTO
     * @return
     */
    @ApiOperation("分页查询菜品列表")
    @GetMapping("/page")
    public  Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("分页查询菜品列表:{}",dishPageQueryDTO);
        PageResult pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @ApiOperation("删除菜品")
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        log.info("删除菜品:{}",ids);
        dishService.delete(ids);
        return Result.success();
    }

    /**
     * 回显菜品
     * @param id
     * @return
     */
    @ApiOperation("根据id查询菜品")
    @GetMapping("/{id}")
    public  Result getById(@PathVariable Long id){
        log.info("回显菜品:{}",id);
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }
    @ApiOperation("修改菜品")
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品:{}",dishDTO);
        dishService.update(dishDTO);
        return Result.success();
    }

    @ApiOperation("起售菜品")
    @PostMapping("/status/{status}")
    public Result TurnOnOff(@PathVariable("status") Integer status, Long id){
        dishService.turnOnOff(status,id);
        return Result.success();
    }
}
