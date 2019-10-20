package com.chocoshop.controller;

import com.chocoshop.model.Category;
import com.chocoshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @RequestMapping("/category/list")
    @ResponseBody
    public List<Category> categoryList(){
        List<Category> categoryList = categoryService.getAllCategory();
        return categoryList;
    }


    @RequestMapping("/category/search")
    @ResponseBody
    public List<Category> searchCategory(Category category){
        System.out.println(category);
        List<Category> categoryList = categoryService.searchResult(category);
        System.out.println(categoryList);
        return categoryList;
    }
}