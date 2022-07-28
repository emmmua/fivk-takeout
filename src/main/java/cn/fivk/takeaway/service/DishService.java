package cn.fivk.takeaway.service;

import cn.fivk.takeaway.dto.DishDto;
import cn.fivk.takeaway.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DishService extends IService<Dish> {
    // 新增菜品，同时插入菜品对应的口味数据，需要操作两张表：dish、dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    // 查询菜品，根据id查询菜品信息以及对应的口味信息，需要操作两张表：dish、dish_flavor
    public DishDto getByIdWithFlavor(Long id);

    // 更新菜品，同时更新口味信息
    public void updateWithFlavor(DishDto dishDto);
}
