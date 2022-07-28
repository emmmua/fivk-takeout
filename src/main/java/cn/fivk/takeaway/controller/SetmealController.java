package cn.fivk.takeaway.controller;

import cn.fivk.takeaway.common.R;
import cn.fivk.takeaway.dto.SetmealDto;
import cn.fivk.takeaway.entity.Category;
import cn.fivk.takeaway.entity.Setmeal;
import cn.fivk.takeaway.service.CategoryService;
import cn.fivk.takeaway.service.SetmealDishService;
import cn.fivk.takeaway.service.SetmealServer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealServer setmealServer;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;


    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealServer.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        // 1. 分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        // 2. 条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        // 3. 执行查询
        setmealServer.page(pageInfo, queryWrapper);

        // 4. 对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");

        // 5. 获取records进行额外修改（添加name值）
        List<Setmeal> records = pageInfo.getRecords();
        // 集合内遍历每个元素进行修改
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);

            // 获得分类id
            Long categoryId = item.getCategoryId();

            // 根据分类id获取Category对象
            Category category = categoryService.getById(categoryId);

            // 将分类名称赋值给SetmealDto
            if (category != null) {
                // 赋值
                setmealDto.setCategoryName(category.getName());
            }

            return setmealDto;
        }).collect(Collectors.toList());

        // 将获取了菜品名字的records放到dtoPage
        dtoPage.setRecords(list);

        // 返回完整Page
        return R.success(dtoPage);
    }


    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("ids: {}" ,ids.toString());
        setmealServer.removeWhitDish(ids);
        return R.success("套餐删除成功");
    }


    /**
     * 查询套餐
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() !=null, Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealServer.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 根据id查询套餐信息以及对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    public R<SetmealDto> getById(@PathVariable Long id) {
        SetmealDto SetmealDto = setmealServer.getByIdWithSetmealDishes(id);
        return R.success(SetmealDto);
    }
}
