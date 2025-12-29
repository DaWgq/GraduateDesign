package com.dispatch.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dispatch.common.Result;
import com.dispatch.entity.Job;
import com.dispatch.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping("/page")
    public Result<Page<Job>> page(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location) {
        return Result.success(jobService.pageList(current, size, keyword, location));
    }

    @GetMapping("/{id}")
    public Result<Job> getById(@PathVariable Long id) {
        return Result.success(jobService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody Job job) {
        jobService.save(job);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Job job) {
        jobService.updateById(job);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        jobService.removeById(id);
        return Result.success();
    }
}
