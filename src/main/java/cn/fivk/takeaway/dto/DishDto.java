package cn.fivk.takeaway.dto;


import cn.fivk.takeaway.entity.Dish;
import cn.fivk.takeaway.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    // 菜品的口味数据
    private List<DishFlavor> flavors = new ArrayList<>();

    // 菜品的名字
    private String categoryName;

    private Integer copies;
}
