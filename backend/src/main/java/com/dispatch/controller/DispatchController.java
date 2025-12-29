package com.dispatch.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dispatch.common.Result;
import com.dispatch.entity.Dispatch;
import com.dispatch.service.DispatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dispatch")
@RequiredArgsConstructor
public class DispatchController {

    private final DispatchService dispatchService;

    @GetMapping("/page")
    public Result<Page<Dispatch>> page(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer status) {
        return Result.success(dispatchService.pageList(current, size, status));
    }

    @GetMapping("/{id}")
    public Result<Dispatch> getById(@PathVariable Long id) {
        return Result.success(dispatchService.getById(id));
    }

    @GetMapping("/my")
    public Result<List<Dispatch>> getMy() {
        Long userId = StpUtil.getLoginIdAsLong();
        return Result.success(dispatchService.getByUserId(userId));
    }

    @PostMapping
    public Result<Void> save(@RequestBody Dispatch dispatch) {
        dispatchService.save(dispatch);
        return Result.success();
    }

    @PostMapping("/apply")
    public Result<Void> apply(@RequestBody Map<String, Long> params) {
        Long userId = StpUtil.getLoginIdAsLong();
        dispatchService.apply(userId, params.get("jobId"), params.get("enterpriseId"));
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Dispatch dispatch) {
        dispatchService.updateById(dispatch);
        return Result.success();
    }

    @PostMapping("/status/{id}")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        dispatchService.updateStatus(id, status);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        dispatchService.removeById(id);
        return Result.success();
    }
}
