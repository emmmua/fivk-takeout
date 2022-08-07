package cn.fivk.takeaway.filter;

import cn.fivk.takeaway.common.BaseContext;
import cn.fivk.takeaway.common.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经登录
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    // 路径匹配器、支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURL
     * @return
     */
    public boolean checkURL(String[] urls ,String requestURL) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURL);
            if (match) return true;
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1、获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}", requestURI);
        // 定义不需要处理的请求路径
        String[] urls = new String[] {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",    // 移动端发送短信
                "/user/login",   // 移动端登录

                // Swagger
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };

        // 2、判断本次请求是否需要处理
        boolean check = checkURL(urls, requestURI);

        // 3、如果不需要处理，则直接放行
        if (check) {
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        // 4-1、判断登录状态，如果已登录，则直接放行
        Long empId = (Long) request.getSession().getAttribute("employee");
        if (empId != null) {
            // 在线程中设置用户id的值
            BaseContext.setCurrentId(empId);

            // 能冲Session获得登录时写入的employee，说明已经登录过了
            filterChain.doFilter(request, response);
            return;
        }

        Long userId = (Long) request.getSession().getAttribute("user");
        if (userId != null) {
            // 在线程中设置用户id的值
            BaseContext.setCurrentId(userId);

            // 能冲Session获得登录时写入的employee，说明已经登录过了
            filterChain.doFilter(request, response);
            return;
        }

        log.info("用户未登录");
        // 5、如果未登录则返回未登录结果，通过输出流方式向客户端相应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }
}
