package com.antonino.book101server.services;

import com.antonino.book101server.dto.ReportItem;
import com.antonino.book101server.models.Order;
import com.antonino.book101server.models.OrderItem;
import com.antonino.book101server.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    private OrderService orderService;

    @Transactional(readOnly = false)
    public List<ReportItem> getReport() {
        List<ReportItem> result = new LinkedList<>();
        for(Order order: orderService.getOrders()){
            ReportItem reportItem = new ReportItem();
            reportItem.setOrderId(order.getId());
            reportItem.setProductCounter(order.getOrderItems().size());
            OrderItem orderItemMax = order.getOrderItems().stream().max(Comparator.comparing(orderItem -> orderItem.getProduct().getPrice())).get(); //tutta la linea
            reportItem.setMaxPrice(orderItemMax.getProduct().getPrice());
            result.add(reportItem);
        }
        return result;
    }
}
