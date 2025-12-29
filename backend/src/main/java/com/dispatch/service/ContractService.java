package com.dispatch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dispatch.entity.Contract;

public interface ContractService extends IService<Contract> {
    Page<Contract> pageList(int current, int size, Long enterpriseId, Integer status);
    void sign(Long id);
    void terminate(Long id);
}
