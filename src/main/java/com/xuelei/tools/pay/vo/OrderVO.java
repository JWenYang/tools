package com.xuelei.tools.pay.vo;

import java.math.BigDecimal;

public class OrderVO {

    private String name;
    private String orderNo;
    private BigDecimal price;

    public OrderVO() {
    }

    private OrderVO(OrderVOBuilder orderVOBuilder) {
        this.name = orderVOBuilder.name;
        this.orderNo = orderVOBuilder.orderNo;
        this.price = orderVOBuilder.price;
    }

    public static final class OrderVOBuilder {

        private String name;
        private String orderNo;
        private BigDecimal price;

        public OrderVOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public OrderVOBuilder orderNo(String orderNo) {
            this.orderNo = orderNo;
            return this;
        }

        public OrderVOBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public OrderVO build(){
            return new OrderVO(this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
