package com.dispatch.service;

import com.dispatch.dto.MatchResult;
import java.util.List;

public interface MatchService {
    List<MatchResult> matchEmployeesForJob(Long jobId);
    List<MatchResult> matchJobsForEmployee(Long employeeId);
}
