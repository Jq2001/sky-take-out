package com.sky.task;


import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class OrderTask {
    @Autowired
    OrderMapper orderMapper;
    @Scheduled(cron = "0 * * * * *")
    public void processTimeoutOrder() {
        LocalDateTime time = LocalDateTime.now().minusMinutes(15);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);
        if (ordersList !=null && !ordersList.isEmpty()){
            ordersList.forEach(orders->{
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelTime(LocalDateTime.now());
                orders.setCancelReason("订单超时");
                orderMapper.update(orders);
            });
        }

    }

    /**
     * 派送中->已完成
     */
    @Scheduled(cron = "0 30 1 * * *")//每天凌晨1点半触发一次
    public void processDeliveryOrder(){
        LocalDateTime time = LocalDateTime.now().minusHours(2);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);
        if (ordersList !=null && !ordersList.isEmpty()) {
            ordersList.forEach(orders->{
                orders.setStatus(Orders.COMPLETED);
                orders.setDeliveryTime(LocalDateTime.now());
                orderMapper.update(orders);
            });
        }
    }
}
