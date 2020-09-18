package com.zh.cloud.admin.config;

import com.ch.Constants;
import com.ch.e.PubError;
import com.ch.result.Result;
import com.ch.utils.CommonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.zh.cloud.admin.controller.LoginController;
import com.zh.cloud.admin.et.PermissionType;
import com.zh.cloud.admin.model.BaseModel;
import com.zh.cloud.admin.model.upms.Permission;
import com.zh.cloud.admin.model.upms.User;
import com.zh.cloud.admin.security.PermissionRoles;
import com.zh.cloud.admin.service.upms.PermissionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 相关MVC拦截器配置
 *
 * @author zhimin.ma 2020-07-13 下午05:12:16
 * @version 1.0.0
 */
@Configuration
@Log4j2
public class WebConfig implements WebMvcConfigurer {

    public static final LoadingCache<String, List<PermissionRoles>> permissions = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build(key -> null); // 用户登录权限缓存

    @Autowired
    private PermissionService permissionService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {

            @Override
            public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                     Object o) throws Exception {
                httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
                httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
                httpServletResponse.setHeader("Access-Control-Allow-Headers",
                        "Origin, X-Requested-With, Content-Type, Accept, Authorization, X-Token");
                httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
                httpServletResponse.setHeader("Access-Control-Max-Age", String.valueOf(3600 * 24));

                if (HttpMethod.OPTIONS.toString().equals(httpServletRequest.getMethod())) {
                    httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                    return false;
                }

                return true;
            }
        }).addPathPatterns("/api/**");

        registry.addInterceptor(new HandlerInterceptor() {

            @Override
            public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                     Object o) throws Exception {
                String token = httpServletRequest.getHeader("X-Token");
                boolean valid = false;
                if (token != null) {
                    User user = LoginController.loginUsers.getIfPresent(token);
                    if (user != null) {
//                        valid = true;
//                        httpServletRequest.setAttribute("user", user);
                        valid = checkPermission(httpServletRequest, user);
                    }
                }
                if (!valid) {
                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(Result.error(PubError.NOT_LOGIN));
                    try {
                        httpServletResponse.setContentType("application/json;charset=UTF-8");
                        PrintWriter out = httpServletResponse.getWriter();
                        out.print(json);
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                    return false;
                }

                return true;
            }
        })
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/**/login/**");
    }

    private static final String METHOD_ALL = "ALL";

    private boolean checkPermission(HttpServletRequest request, User user) {
        log.info("request method: {}, url: {}", request.getMethod(), request.getRequestURI());
        if (CommonUtils.isEquals(user.getRoleId(), Constants.SUPER_ID)) {
            return true;
        }
        if (permissions.estimatedSize() == 0) {
            initPermissions();
        }
        List<PermissionRoles> permissions2 = permissions.get(request.getMethod());
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        if (CommonUtils.isNotEmpty(permissions2)) {
            for (PermissionRoles permissionRoles : permissions2) {
                if (CommonUtils.isEmpty(permissionRoles.getUrl())) {
                    continue;
                }
            }
        }
//        if(permissions.estimatedSize())
        return false;
    }

    private void initPermissions() {
        List<Permission> permissionList = permissionService.findAllByType(PermissionType.BUTTON);
    }
}
