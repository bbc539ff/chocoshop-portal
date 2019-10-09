package com.chocoshop.model;

import javax.persistence.Table;

@Table(name = "cc_goods_sell_number")
public class GoodsSellNumber {

    Long goodsId;
    Long sellNumber;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getSellNumber() {
        return sellNumber;
    }

    public void setSellNumber(Long sellNumber) {
        this.sellNumber = sellNumber;
    }

    @Override
    public String toString() {
        return "GoodsSellNumber{" +
                "goodsId=" + goodsId +
                ", sellNumber=" + sellNumber +
                '}';
    }
}
