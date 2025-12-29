package com.dispatch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dispatch.entity.Job;

public interface JobService extends IService<Job> {
    Page<Job> pageList(int current, int size, String keyword, String location);
}
