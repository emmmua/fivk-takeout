package cn.fivk.takeaway.service.impl;

import cn.fivk.takeaway.common.CustomException;
import cn.fivk.takeaway.common.R;
import cn.fivk.takeaway.dto.SetmealDto;
import cn.fivk.takeaway.entity.Setmeal;
import cn.fivk.takeaway.entity.SetmealDish;
import cn.fivk.takeaway.mapper.SetmealMapper;
import cn.fivk.takeaway.service.SetmealDishService;
import cn.fivk.takeaway.service.SetmealServer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServerImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealServer {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐：将套餐基本信息以及关联的菜品信息进行保存
     * @param setmealDto
     */
    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        // 1. 保存套餐基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        // 2. 将每一个 SetmealDish 对应的SetmealId进行赋值
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        // 3. 保存套餐和菜品的关联信息，操作setmeal_dish，执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐：同时删除套餐和菜品的关联数据
     * @param ids
     */
    @Transactional
    @Override
    public void removeWhitDish(List<Long> ids) {
        // 1. 查询套餐的状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);


        if (count > 0) {
            // 2. 如果不能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        // 3. 如果可以删除，先删除套餐中的数据 --- setmeal
        this.removeByIds(ids);

        // 4. 删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(dishQueryWrapper);
    }


    /**
     * 查询套餐，根据id查询菜品信息以及对应的口味信息，需要操作两张表：setmeal, setmeal_dish
     * @param id
     * @return
     */
    public SetmealDto getByIdWithSetmealDishes(Long id) {
        // 1. 通过id获得setmeal信息，并拷贝到setmealDto中
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        // 2. 通过setmeal获取套餐中的所有菜品
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
        List<SetmealDish> setmealDishList = setmealDishService.list(queryWrapper);

        // 3. 将套餐中的菜品信息放到Dto中
        setmealDto.setSetmealDishes(setmealDishList);

        return setmealDto;
    }
}
