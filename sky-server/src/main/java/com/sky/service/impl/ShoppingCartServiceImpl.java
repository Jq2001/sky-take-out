package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void addCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        //判断商品是否已经在购物车--条件:dish_id dishflavor userid
        shoppingCart.setUserId(BaseContext.getCurrentId());
        ShoppingCart cart = shoppingCartMapper.selectBy(shoppingCart);

        if (cart == null) {
            //判断是新增套餐还是菜品
            if (shoppingCartDTO.getDishId() != null) {
                //新增菜品
                Dish dish = dishMapper.selectById(shoppingCartDTO.getDishId());
                //补充属性值
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                //新增套餐
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            //商品数据存入购物车表中
            shoppingCartMapper.insert(shoppingCart);
        } else {//购物车中有这个商品
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.update(cart);
        }


    }

    @Override
    public List<ShoppingCart> list() {
        return shoppingCartMapper.list(BaseContext.getCurrentId());
    }

    @Override
    public void clean() {
        shoppingCartMapper.clean(BaseContext.getCurrentId());
    }

    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        //设置查询条件，查询当前登录用户的购物车数据
        shoppingCart.setUserId(BaseContext.getCurrentId());

        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart.getUserId());

        if (shoppingCarts != null && shoppingCarts.size() > 0) {
            shoppingCart = shoppingCarts.get(0);
            Integer number = shoppingCart.getNumber();
            if (number == 1){
                shoppingCartMapper.deleteBySCId(shoppingCart.getId());
            }else{
                shoppingCart.setNumber(shoppingCart.getNumber()-1);
                shoppingCartMapper.update(shoppingCart);
            }
        }
    }
}
