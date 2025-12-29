package com.dispatch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dispatch.entity.Dispatch;

import java.util.List;

public interface DispatchService extends IService<Dispatch> {
    Page<Dispatch> pageList(int current, int size, Integer status);
    void updateStatus(Long id, Integer status);
    List<Dispatch> getByUserId(Long userId);
    void apply(Long userId, Long jobId, Long enterpriseId);
}
