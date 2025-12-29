package com.dispatch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dispatch.entity.User;

public interface UserService extends IService<User> {
    String login(String username, String password);
    void register(User user);
    User getCurrentUser();
    Page<User> pageList(int current, int size, Integer role);
    void updateStatus(Long id, Integer status);
}
