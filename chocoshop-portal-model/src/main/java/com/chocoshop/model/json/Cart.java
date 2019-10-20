package com.chocoshop.model.json;

import com.chocoshop.model.Goods;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Cart {
    private Map<Long, Integer> goodsIdMap;
    private Integer number;
    private BigDecimal price;

    public Cart(){}

    public Cart(Map<Long, Integer> goodsIdMap, Integer number, BigDecimal price) {
        this.goodsIdMap = goodsIdMap;
        this.number = number;
        this.price = price;
    }

    public Map<Long, Integer> getGoodsIdMap() {
        return goodsIdMap;
    }

    public void setGoodsIdMap(Map<Long, Integer> goodsIdMap) {
        this.goodsIdMap = goodsIdMap;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "goodsIdMap=" + goodsIdMap +
                ", number=" + number +
                ", price=" + price +
                '}';
    }
}
