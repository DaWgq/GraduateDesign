package com.dispatch.config;

import cn.dev33.satoken.stp.StpInterface;
import com.dispatch.entity.User;
import com.dispatch.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final UserMapper userMapper;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return new ArrayList<>();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> roles = new ArrayList<>();
        User user = userMapper.selectById(Long.parseLong(loginId.toString()));
        if (user != null) {
            switch (user.getRole()) {
                case 1 -> roles.add("admin");
                case 2 -> roles.add("enterprise");
                case 3 -> roles.add("employee");
            }
        }
        return roles;
    }
}
