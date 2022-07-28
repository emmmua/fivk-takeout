package cn.fivk.takeaway.service.impl;

import cn.fivk.takeaway.entity.DishFlavor;
import cn.fivk.takeaway.mapper.DishFlavorMapper;
import cn.fivk.takeaway.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
