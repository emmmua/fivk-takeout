package cn.fivk.takeaway.service.impl;

import cn.fivk.takeaway.entity.User;
import cn.fivk.takeaway.mapper.UserMapper;
import cn.fivk.takeaway.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
