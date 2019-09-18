package com.chocoshop.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="cc_goods_category")
public class Category implements Serializable {

    @Id
    private Long categoryId;
    private String categoryName;
    private Long categoryParent;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCategoryParent() {
        return categoryParent;
    }

    public void setCategoryParent(Long categoryParent) {
        this.categoryParent = categoryParent;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", categoryParent=" + categoryParent +
                '}';
    }
}
