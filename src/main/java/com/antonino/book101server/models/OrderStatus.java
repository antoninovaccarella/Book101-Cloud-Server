package com.antonino.book101server.models;

public enum OrderStatus {
    AWAITING_PAYMENT, 
    AWAITING_SHIPMENT, 
    AWAITING_FULFILLMENT, 
    PENDING, 
    SHIPPED, 
    CANCELLED, 
    DECLINED, 
    COMPLETED
}
