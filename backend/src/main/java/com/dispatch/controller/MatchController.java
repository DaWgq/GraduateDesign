package com.dispatch.controller;

import com.dispatch.common.Result;
import com.dispatch.dto.MatchResult;
import com.dispatch.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/employees/{jobId}")
    public Result<List<MatchResult>> matchEmployeesForJob(@PathVariable Long jobId) {
        return Result.success(matchService.matchEmployeesForJob(jobId));
    }

    @GetMapping("/jobs/{employeeId}")
    public Result<List<MatchResult>> matchJobsForEmployee(@PathVariable Long employeeId) {
        return Result.success(matchService.matchJobsForEmployee(employeeId));
    }
}
