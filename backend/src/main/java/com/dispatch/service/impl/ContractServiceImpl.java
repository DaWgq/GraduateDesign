package com.dispatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dispatch.entity.Contract;
import com.dispatch.mapper.ContractMapper;
import com.dispatch.service.ContractService;
import org.springframework.stereotype.Service;

@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractService {

    @Override
    public Page<Contract> pageList(int current, int size, Long enterpriseId, Integer status) {
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<>();
        if (enterpriseId != null) wrapper.eq(Contract::getEnterpriseId, enterpriseId);
        if (status != null) wrapper.eq(Contract::getStatus, status);
        wrapper.orderByDesc(Contract::getCreateTime);
        return page(new Page<>(current, size), wrapper);
    }

    @Override
    public void sign(Long id) {
        Contract contract = getById(id);
        if (contract == null) throw new RuntimeException("合同不存在");
        contract.setStatus(1);
        updateById(contract);
    }

    @Override
    public void terminate(Long id) {
        Contract contract = getById(id);
        if (contract == null) throw new RuntimeException("合同不存在");
        contract.setStatus(2);
        updateById(contract);
    }
}
