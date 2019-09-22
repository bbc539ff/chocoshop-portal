package com.chocoshop.model.json;

import com.chocoshop.model.Goods;

import java.io.Serializable;
import java.util.List;

public class NewGoods implements Serializable {
    String categoryName;
    List<Goods> goodsList;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

}