package cn.fivk.takeaway.controller;

import cn.fivk.takeaway.common.R;
import cn.fivk.takeaway.entity.User;
import cn.fivk.takeaway.service.UserService;
import cn.fivk.takeaway.utils.SMSUtils;
import cn.fivk.takeaway.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 发送手机短信验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        // 1.获取手机号
        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)) {
            // 2.生成随机4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code = {}", code);

            // 3. 调用阿里云短信API服务
            //SMSUtils.sendMessage("Fivk博客", "", phone, code);

            // 4. 将生成的验证码保存到Session
            //session.setAttribute(phone, code);

            // 4. 将生成的验证码保存到redis中， 并且设置有效期为5分钟
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

            return R.success("手机验证码短信发送成功");
        }

        return R.error("短信发送失败");
    }


    @PostMapping("/login")
    public R<User> sendMsg(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());

        // 1. 获取手机号和验证码
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();

        // 2. 从Session中获取保存的验证码
        //String codeInSession = (String) session.getAttribute(phone);

        // 2. 从redis中获取我们的验证码
        String codeInRedis = (String) redisTemplate.opsForValue().get(phone);

        // 3. 进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
        if (codeInRedis != null && codeInRedis.equals(code)) {
            // 如果能够比对成功，说明登录成功

            // 4. 判断如果是新用户，我们就自动完成注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                // 是新用户，自动注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());

            // 如果用户登录成功，删除Redis中缓存的验证码
            redisTemplate.delete(phone);
            return R.success(user);
        }
        return R.error("登录失败");
    }
}
