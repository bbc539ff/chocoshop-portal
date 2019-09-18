package com.chocoshop.mapper;

import com.chocoshop.model.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper extends tk.mybatis.mapper.common.Mapper<Category>  {

    @Select("<script>" +
            "SELECT * FROM cc_goods_category " +
            "<where>" +
            "<if test=\"categoryId != null\">" +
            "category_id = #{categoryId}" +
            "</if>" +
            "<if test=\"categoryName != null\">" +
            "AND category_name LIKE CONCAT('%', #{categoryName}, '%')" +
            "</if>" +
            "<if test=\"categoryParent != null\">" +
            "AND category_parent = #{categoryParent}" +
            "</if>" +
            "</where>" +
            "</script>")
    @Results({
            @Result(property = "categoryId", column = "category_id"),
            @Result(property = "categoryName", column = "category_name"),
            @Result(property = "categoryParent", column = "category_parent"),
    })
    List<Category> search(Category category);
}
