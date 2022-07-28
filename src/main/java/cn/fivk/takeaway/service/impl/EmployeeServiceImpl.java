package cn.fivk.takeaway.service.impl;

import cn.fivk.takeaway.entity.Employee;
import cn.fivk.takeaway.mapper.EmployeeMapper;
import cn.fivk.takeaway.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
