package com.dispatch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dispatch.entity.Enterprise;

public interface EnterpriseService extends IService<Enterprise> {
    Page<Enterprise> pageList(int current, int size, String keyword);
    void audit(Long id, Integer status);
    Enterprise getByUserId(Long userId);
}
