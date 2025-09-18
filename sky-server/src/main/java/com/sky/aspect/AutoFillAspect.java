package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段自动填充处理逻辑
 */
@Component
@Aspect
public class AutoFillAspect {
    @Before("@annotation(com.sky.annotation.AutoFill)")
    public void autoFill(JoinPoint joinPoint) throws NoSuchFieldException, IllegalAccessException {
        //获取方法参数
        Object arg = joinPoint.getArgs()[0];

        //获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        //获取方法对象
        Method method = signature.getMethod();

        //获取方法上的注解
        AutoFill annotation = method.getAnnotation(AutoFill.class);

        //获取注解里的value值
        OperationType value = annotation.value();

        //判断
        if (value == OperationType.INSERT){
            //添加 四个属性值
            //获取到了参数的字节码对象
            Class aClass = arg.getClass();
            //获取属性
            Field f1 = aClass.getDeclaredField("creatTime");
            Field f2 = aClass.getDeclaredField("updateTime");
            Field f3 = aClass.getDeclaredField("createUser");
            Field f4 = aClass.getDeclaredField("updateUser");
            //暴力反射
            f1.setAccessible(true);
            f2.setAccessible(true);
            f3.setAccessible(true);
            f4.setAccessible(true);

            f1.set(arg, LocalDateTime.now());
            f2.set(arg, LocalDateTime.now());
            f3.set(arg, BaseContext.getCurrentId());
            f4.set(arg, BaseContext.getCurrentId());
        }else{
            //更新 两个属性值
            Class aClass = arg.getClass();
            Field f1 = aClass.getDeclaredField("updateTime");
            Field f2 = aClass.getDeclaredField("updateUser");

            f1.setAccessible(true);
            f2.setAccessible(true);

            f1.set(arg, LocalDateTime.now());
            f2.set(arg, BaseContext.getCurrentId());

        }


    }
}
