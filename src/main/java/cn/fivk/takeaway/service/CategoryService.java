package cn.fivk.takeaway.service;

import cn.fivk.takeaway.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
