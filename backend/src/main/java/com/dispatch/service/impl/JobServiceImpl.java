package com.dispatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dispatch.entity.Job;
import com.dispatch.mapper.JobMapper;
import com.dispatch.service.JobService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    @Override
    public Page<Job> pageList(int current, int size, String keyword, String location) {
        LambdaQueryWrapper<Job> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Job::getTitle, keyword).or().like(Job::getSkills, keyword);
        }
        if (StringUtils.hasText(location)) {
            wrapper.like(Job::getLocation, location);
        }
        wrapper.eq(Job::getStatus, 1).orderByDesc(Job::getCreateTime);
        return page(new Page<>(current, size), wrapper);
    }
}
