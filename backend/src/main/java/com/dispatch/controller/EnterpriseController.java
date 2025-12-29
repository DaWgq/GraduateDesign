package com.dispatch.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dispatch.common.Result;
import com.dispatch.entity.Enterprise;
import com.dispatch.service.EnterpriseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enterprise")
@RequiredArgsConstructor
public class EnterpriseController {

    private final EnterpriseService enterpriseService;

    @GetMapping("/page")
    public Result<Page<Enterprise>> page(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        return Result.success(enterpriseService.pageList(current, size, keyword));
    }

    @GetMapping("/{id}")
    public Result<Enterprise> getById(@PathVariable Long id) {
        return Result.success(enterpriseService.getById(id));
    }

    @GetMapping("/my")
    public Result<Enterprise> getMy() {
        Long userId = StpUtil.getLoginIdAsLong();
        return Result.success(enterpriseService.getByUserId(userId));
    }

    @PostMapping
    public Result<Void> save(@RequestBody Enterprise enterprise) {
        enterpriseService.save(enterprise);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Enterprise enterprise) {
        enterpriseService.updateById(enterprise);
        return Result.success();
    }

    @PostMapping("/audit/{id}")
    public Result<Void> audit(@PathVariable Long id, @RequestParam Integer status) {
        enterpriseService.audit(id, status);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        enterpriseService.removeById(id);
        return Result.success();
    }
}
