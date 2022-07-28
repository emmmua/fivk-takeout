package cn.fivk.takeaway.service.impl;

import cn.fivk.takeaway.common.CustomException;
import cn.fivk.takeaway.entity.Category;
import cn.fivk.takeaway.entity.Dish;
import cn.fivk.takeaway.entity.Setmeal;
import cn.fivk.takeaway.mapper.CategoryMapper;
import cn.fivk.takeaway.service.CategoryService;
import cn.fivk.takeaway.service.DishService;
import cn.fivk.takeaway.service.SetmealServer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServerImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishServer;

    @Autowired
    private SetmealServer setmealServer;

    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        // 1. 查询当前分类是否关联了菜品，如果关联了，抛出业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件（根据分类id进行查询）
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishServer.count(dishLambdaQueryWrapper);

        if (count1 > 0) {
            // 已经关联了菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }



        // 2. 查询当前分类是否关联了套餐，如果关联了，抛出业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件（根据id进行查询）
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealServer.count(setmealLambdaQueryWrapper);

        if (count2 > 0) {
            // 已经关联了套餐，抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        // 正常删除
        super.removeById(id);
    }
}
