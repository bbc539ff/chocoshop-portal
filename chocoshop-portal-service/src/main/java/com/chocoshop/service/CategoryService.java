package com.chocoshop.service;

import com.chocoshop.mapper.CategoryMapper;
import com.chocoshop.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    public CategoryMapper categoryMapper;

    public List<Category> getAllCategory(){
        return categoryMapper.selectAll();
    }

    public int addCategory(Category category){
        return categoryMapper.insert(category);
    }

    public int deleteCategory(Category category){
        return categoryMapper.delete(category);
    }

    public int updateCategory(Category category){
        return categoryMapper.updateByPrimaryKeySelective(category);
    }

    public int countCategory(){
        return categoryMapper.selectCount(new Category());
    }

    public List<Category> searchResult(Category category) {
        try {
            return categoryMapper.search(category);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
