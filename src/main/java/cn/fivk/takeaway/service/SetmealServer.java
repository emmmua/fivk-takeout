package cn.fivk.takeaway.service;


import cn.fivk.takeaway.dto.DishDto;
import cn.fivk.takeaway.dto.SetmealDto;
import cn.fivk.takeaway.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;


public interface SetmealServer extends IService<Setmeal> {
    /**
     * 新增套餐：将套餐基本信息以及关联的菜品信息进行保存
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);


    /**
     * 删除套餐：同时删除套餐和菜品的关联数据
     * @param ids
     */
    public void removeWhitDish(List<Long> ids);


    // 查询套餐，根据id查询菜品信息以及对应的口味信息，需要操作两张表：setmeal, setmeal_dish
    public SetmealDto getByIdWithSetmealDishes(Long id);


    /**
     * 更新套餐，同时更新口味信息
     * @param setmealDto
     */
    public void updateWithDish(SetmealDto setmealDto);
}
