package cn.fivk.takeaway.dto;


import cn.fivk.takeaway.entity.Setmeal;
import cn.fivk.takeaway.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    // 菜品信息
    private List<SetmealDish> setmealDishes;

    // 菜品名字
    private String categoryName;
}
