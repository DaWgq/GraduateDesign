package com.dispatch.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> {
            // 登录校验
            SaRouter.match("/api/**")
                    .notMatch("/api/user/login", "/api/user/register")
                    .check(r -> StpUtil.checkLogin());

            // 管理员专属接口
            SaRouter.match(
                    "/api/enterprise/audit/**",
                    "/api/user/list",
                    "/api/user/status/**"
            ).check(r -> StpUtil.checkRole("admin"));

            // 企业客户接口
            SaRouter.match(
                    "/api/job/**",
                    "/api/dispatch/**",
                    "/api/contract/**",
                    "/api/attendance/**",
                    "/api/salary/calculate"
            ).notMatch(
                    "/api/job/page", 
                    "/api/job/{id}",
                    "/api/dispatch/my",
                    "/api/dispatch/apply",
                    "/api/attendance/my",
                    "/api/salary/my"
            ).check(r -> StpUtil.checkRoleOr("admin", "enterprise"));

            // 员工接口 - 需要登录即可访问
            SaRouter.match(
                    "/api/dispatch/my",
                    "/api/dispatch/apply",
                    "/api/attendance/my",
                    "/api/salary/my"
            ).check(r -> StpUtil.checkLogin());

        })).addPathPatterns("/api/**");
    }
}
