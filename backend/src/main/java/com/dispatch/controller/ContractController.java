package com.dispatch.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dispatch.common.Result;
import com.dispatch.entity.Contract;
import com.dispatch.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @GetMapping("/page")
    public Result<Page<Contract>> page(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long enterpriseId,
            @RequestParam(required = false) Integer status) {
        return Result.success(contractService.pageList(current, size, enterpriseId, status));
    }

    @GetMapping("/{id}")
    public Result<Contract> getById(@PathVariable Long id) {
        return Result.success(contractService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody Contract contract) {
        contractService.save(contract);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Contract contract) {
        contractService.updateById(contract);
        return Result.success();
    }

    @PostMapping("/sign/{id}")
    public Result<Void> sign(@PathVariable Long id) {
        contractService.sign(id);
        return Result.success();
    }

    @PostMapping("/terminate/{id}")
    public Result<Void> terminate(@PathVariable Long id) {
        contractService.terminate(id);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        contractService.removeById(id);
        return Result.success();
    }
}
