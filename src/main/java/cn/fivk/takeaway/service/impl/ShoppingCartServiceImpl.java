package cn.fivk.takeaway.service.impl;

import cn.fivk.takeaway.entity.ShoppingCart;
import cn.fivk.takeaway.mapper.ShoppingCartMapper;
import cn.fivk.takeaway.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
