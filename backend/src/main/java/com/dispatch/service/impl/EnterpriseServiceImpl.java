package com.dispatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dispatch.entity.Enterprise;
import com.dispatch.mapper.EnterpriseMapper;
import com.dispatch.service.EnterpriseService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EnterpriseServiceImpl extends ServiceImpl<EnterpriseMapper, Enterprise> implements EnterpriseService {

    @Override
    public Page<Enterprise> pageList(int current, int size, String keyword) {
        LambdaQueryWrapper<Enterprise> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Enterprise::getName, keyword);
        }
        wrapper.orderByDesc(Enterprise::getCreateTime);
        return page(new Page<>(current, size), wrapper);
    }

    @Override
    public void audit(Long id, Integer status) {
        Enterprise enterprise = getById(id);
        if (enterprise == null) {
            throw new RuntimeException("企业不存在");
        }
        enterprise.setAuditStatus(status);
        updateById(enterprise);
    }

    @Override
    public Enterprise getByUserId(Long userId) {
        return getOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getUserId, userId));
    }
}
