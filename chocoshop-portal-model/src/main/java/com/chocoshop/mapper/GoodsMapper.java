package com.chocoshop.mapper;

import com.chocoshop.model.Goods;
import com.chocoshop.model.GoodsSellNumber;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GoodsMapper extends tk.mybatis.mapper.common.Mapper<Goods> {
    @Select("<script>" +
            "SELECT * FROM cc_goods " +
            "<where>" +
            "<if test=\"goodsId != null\">" +
            "goods_id = #{goodsId}" +
            "</if>" +
            "<if test=\"goodsTitle != null\">" +
            "AND goods_title LIKE CONCAT('%', #{goodsTitle}, '%')" +
            "</if>" +
            "<if test=\"categoryId != null\">" +
            "AND category_id = #{categoryId}" +
            "</if>" +
            "<if test=\"goodsPrice != null\">" +
            "goods_price = #{goodsPrice}" +
            "</if>" +
            "<if test=\"goodsNumber != null\">" +
            "AND goods_number = #{goodsNumber}" +
            "</if>" +
            "<if test=\"goodsImageurl != null\">" +
            "AND goods_imageurl LIKE CONCAT('%', #{goodsImageurl}, '%')" +
            "</if>" +
            "<if test=\"goodsStatus != null\">" +
            "goods_status = #{goodsStatus}" +
            "</if>" +
            "<if test=\"goodsCreateTime != null\">" +
            "AND DATE_FORMAT(goods_create_time, '%Y-%m-%d') =  DATE_FORMAT(#{goodsCreateTime}, '%Y-%m-%d')" +
            "</if>" +
            "<if test=\"goodsUpdateTime != null\">" +
            "AND DATE_FORMAT(goods_update_time, '%Y-%m-%d') =  DATE_FORMAT(#{goodsUpdateTime}, '%Y-%m-%d')" +
            "</if>" +
            "<if test=\"goodsDetail != null\">" +
            "AND goods_detail LIKE CONCAT('%', #{goodsDetail}, '%')" +
            "</if>" +
            "<if test=\"goodsDetailImageurl != null\">" +
            "AND goods_detail_imageurl LIKE CONCAT('%', #{goodsDetailImageurl}, '%')" +
            "</if>" +
            "</where>" +
            "</script>")
    @Results({
            @Result(property = "goodsId", column = "goods_id"),
            @Result(property = "goodsTitle", column = "goods_title"),
            @Result(property = "categoryId", column = "category_id"),
            @Result(property = "goodsPrice", column = "goods_price"),
            @Result(property = "goodsNumber", column = "goods_number"),
            @Result(property = "goodsImageurl", column = "goods_imageurl"),
            @Result(property = "goodsStatus", column = "goods_status"),
            @Result(property = "goodsCreateTime", column = "goods_create_time"),
            @Result(property = "goodsUpdateTime", column = "goods_update_time"),
            @Result(property = "goodsDetail", column = "goods_detail"),
            @Result(property = "goodsDetailImageurl", column = "goods_detail_imageurl"),
    })
    List<Goods> search(Goods goods);

    @Select("<script>" +
            "SELECT cc_goods.* FROM cc_goods,cc_goods_category WHERE cc_goods.category_id = cc_goods_category.category_id " +
            "<if test=\"categoryParent != null\">" +
            "AND category_parent = #{categoryParent} " +
            "</if>" +
            "ORDER BY goods_create_time DESC " +
            "</script>")
    @Results({
            @Result(property = "goodsId", column = "goods_id"),
            @Result(property = "goodsTitle", column = "goods_title"),
            @Result(property = "categoryId", column = "category_id"),
            @Result(property = "goodsPrice", column = "goods_price"),
            @Result(property = "goodsNumber", column = "goods_number"),
            @Result(property = "goodsImageurl", column = "goods_imageurl"),
            @Result(property = "goodsStatus", column = "goods_status"),
            @Result(property = "goodsCreateTime", column = "goods_create_time"),
            @Result(property = "goodsUpdateTime", column = "goods_update_time"),
            @Result(property = "goodsDetail", column = "goods_detail"),
            @Result(property = "goodsDetailImageurl", column = "goods_detail_imageurl"),
    })
    List<Goods> findByCategory(@Param("categoryParent") Long categoryParent);

    @Select("SELECT c1.*, d1.`sell_number` \n" +
            "FROM cc_goods AS c1, cc_goods_sell_number AS d1\n" +
            "WHERE c1.`goods_id` = d1.`goods_id` AND\n" +
            "(\n" +
            "   SELECT COUNT(*) FROM cc_goods AS c2, cc_goods_sell_number AS d2\n" +
            "   WHERE c2.`goods_id` = d2.`goods_id` AND\n" +
            "   c1.category_id = c2.category_id AND d2.sell_number <= d1.sell_number\n" +
            ") <= 2;")
    @Results({
            @Result(property = "goodsId", column = "goods_id"),
            @Result(property = "goodsTitle", column = "goods_title"),
            @Result(property = "categoryId", column = "category_id"),
            @Result(property = "goodsPrice", column = "goods_price"),
            @Result(property = "goodsNumber", column = "goods_number"),
            @Result(property = "goodsImageurl", column = "goods_imageurl"),
            @Result(property = "goodsStatus", column = "goods_status"),
            @Result(property = "goodsCreateTime", column = "goods_create_time"),
            @Result(property = "goodsUpdateTime", column = "goods_update_time"),
            @Result(property = "goodsDetail", column = "goods_detail"),
            @Result(property = "goodsDetailImageurl", column = "goods_detail_imageurl"),
    })
    public List<Goods> findBySellNumber();
}