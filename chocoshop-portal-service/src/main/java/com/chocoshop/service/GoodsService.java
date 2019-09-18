package com.chocoshop.service;

import com.chocoshop.mapper.CategoryMapper;
import com.chocoshop.mapper.GoodsMapper;
import com.chocoshop.model.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import utils.Utils;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    GoodsMapper goodsMapper;

    public List<Goods> showAllGoods(){
        return goodsMapper.selectAll();
    }

    public int addGoods(Goods goods, MultipartFile[] files) {
        try{
            goodsMapper.insert(goods);
            Long goodsId = goodsMapper.selectOne(goods).getGoodsId();
            System.out.println(goodsId);
            goods = new Goods();
            goods.setGoodsId(goodsId);
            return updateGoods(goods, files);
        } catch (Exception e){
            e.printStackTrace();
        }
        return 0;

    }

    public int deleteGoods(Goods goods){
        return goodsMapper.delete(goods);
    }

    public int updateGoods(Goods goods, MultipartFile[] files){
        if(files != null){
            Long goodsId = goods.getGoodsId();
            String path = "";
            for(int i = 0;i<files.length;i++) {
                path += Utils.uploadSingle(files[i], "/upload/goods/"+goodsId, Integer.toString(i), false)+", ";
            }
            goods.setGoodsImageurl(path);
        }

        System.out.println(goods);
        return goodsMapper.updateByPrimaryKeySelective(goods);
    }

    public int updateGoodsDetail(Goods goods, MultipartFile[] files){
        if(files != null){
            Long goodsId = goods.getGoodsId();
            String path = "";
            for(int i = 0;i<files.length;i++) {
                path += Utils.uploadSingle(files[i], "/upload/goods/detail/"+goodsId, files[i].getOriginalFilename(), true)+", ";
            }
            System.out.println(path);
            goods.setGoodsDetailImageurl(path);
        }
        return goodsMapper.updateByPrimaryKeySelective(goods);
    }

    public List<Goods> search(Goods goods){
        try {
            return goodsMapper.search(goods);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public int countGoods(){
        return goodsMapper.selectCount(new Goods());
    }

    public String findByGoodsId(Long goodsId){
        Goods goods = new Goods();
        goods.setGoodsId(goodsId);
        return goodsMapper.selectOne(goods).getGoodsDetail();
    }

    public Goods getGoodsById(Long goodsId){
        return goodsMapper.selectByPrimaryKey(goodsId);
    }

    public List<Goods> newGoods(Long categoryId) {
        return goodsMapper.findByCategory(categoryId);
    }
}
