package com.dispatch.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dispatch.entity.User;
import com.dispatch.mapper.UserMapper;
import com.dispatch.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public String login(String username, String password) {
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getStatus, 1));
        if (user == null) {
            throw new RuntimeException("用户不存在或已禁用");
        }
        String md5Pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Pwd.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        StpUtil.login(user.getId());
        return StpUtil.getTokenValue();
    }

    @Override
    public void register(User user) {
        User exist = getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername()));
        if (exist != null) {
            throw new RuntimeException("用户名已存在");
        }
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setStatus(1);
        if (user.getRole() == null) user.setRole(3);
        save(user);
    }

    @Override
    public User getCurrentUser() {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = getById(userId);
        user.setPassword(null);
        return user;
    }

    @Override
    public Page<User> pageList(int current, int size, Integer role) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (role != null) wrapper.eq(User::getRole, role);
        wrapper.orderByDesc(User::getCreateTime);
        Page<User> page = page(new Page<>(current, size), wrapper);
        page.getRecords().forEach(u -> u.setPassword(null));
        return page;
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        User user = getById(id);
        if (user == null) throw new RuntimeException("用户不存在");
        user.setStatus(status);
        updateById(user);
    }
}
