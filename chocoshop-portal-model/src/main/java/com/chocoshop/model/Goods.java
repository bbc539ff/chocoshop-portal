package com.chocoshop.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="cc_goods")
public class Goods implements Serializable {
    @Id
    private Long goodsId;
    @Length(min = 6, max = 36)
    private String goodsTitle;
    private Long categoryId;
    @DecimalMin("0")
    private Double goodsPrice;
    @Min(0)
    private Integer goodsNumber;
    private String goodsImageurl;
    private Integer goodsStatus;
    private Date goodsCreateTime;
    private Date goodsUpdateTime;
    private String goodsDetail;
    private String goodsDetailImageurl;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Integer getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(Integer goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public String getGoodsImageurl() {
        return goodsImageurl;
    }

    public void setGoodsImageurl(String goodsImageurl) {
        this.goodsImageurl = goodsImageurl;
    }

    public Integer getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(Integer goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public Date getGoodsCreateTime() {
        return goodsCreateTime;
    }

    public void setGoodsCreateTime(Date goodsCreateTime) {
        this.goodsCreateTime = goodsCreateTime;
    }

    public Date getGoodsUpdateTime() {
        return goodsUpdateTime;
    }

    public void setGoodsUpdateTime(Date goodsUpdateTime) {
        this.goodsUpdateTime = goodsUpdateTime;
    }

    public String getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    public String getGoodsDetailImageurl() {
        return goodsDetailImageurl;
    }

    public void setGoodsDetailImageurl(String goodsDetailImageurl) {
        this.goodsDetailImageurl = goodsDetailImageurl;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "goodsId=" + goodsId +
                ", goodsTitle='" + goodsTitle + '\'' +
                ", categoryId=" + categoryId +
                ", goodsPrice=" + goodsPrice +
                ", goodsNumber=" + goodsNumber +
                ", goodsImageurl='" + goodsImageurl + '\'' +
                ", goodsStatus=" + goodsStatus +
                ", goodsCreateTime=" + goodsCreateTime +
                ", goodsUpdateTime=" + goodsUpdateTime +
                ", goodsDetail='" + goodsDetail + '\'' +
                ", goodsDetailImageurl='" + goodsDetailImageurl + '\'' +
                '}';
    }
}
