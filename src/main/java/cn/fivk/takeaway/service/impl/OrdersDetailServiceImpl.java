package cn.fivk.takeaway.service.impl;

import cn.fivk.takeaway.entity.OrderDetail;
import cn.fivk.takeaway.mapper.OrderDetailMapper;
import cn.fivk.takeaway.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrdersDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
