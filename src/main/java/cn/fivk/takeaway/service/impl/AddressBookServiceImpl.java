package cn.fivk.takeaway.service.impl;

import cn.fivk.takeaway.entity.AddressBook;
import cn.fivk.takeaway.mapper.AddressBookMapper;
import cn.fivk.takeaway.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
