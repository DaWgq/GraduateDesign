package com.dispatch.service.impl;

import com.dispatch.dto.MatchResult;
import com.dispatch.entity.Employee;
import com.dispatch.entity.Job;
import com.dispatch.mapper.EmployeeMapper;
import com.dispatch.mapper.JobMapper;
import com.dispatch.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final JobMapper jobMapper;
    private final EmployeeMapper employeeMapper;

    @Override
    public List<MatchResult> matchEmployeesForJob(Long jobId) {
        Job job = jobMapper.selectById(jobId);
        if (job == null) return Collections.emptyList();

        Set<String> jobSkills = parseSkills(job.getSkills());
        List<Employee> employees = employeeMapper.selectList(null);

        return employees.stream()
                .map(emp -> {
                    MatchResult result = new MatchResult();
                    result.setId(emp.getId());
                    result.setName(emp.getName());
                    result.setSkills(emp.getSkills());
                    result.setSalary(emp.getExpectedSalary());

                    Set<String> empSkills = parseSkills(emp.getSkills());
                    int skillMatch = calculateSkillMatch(jobSkills, empSkills);
                    int salaryMatch = calculateSalaryMatch(emp.getExpectedSalary(), job.getSalaryMin(), job.getSalaryMax());
                    int score = (skillMatch * 7 + salaryMatch * 3) / 10;

                    result.setMatchScore(score);
                    result.setMatchReason(buildMatchReason(skillMatch, salaryMatch));
                    return result;
                })
                .filter(r -> r.getMatchScore() > 30)
                .sorted(Comparator.comparing(MatchResult::getMatchScore).reversed())
                .limit(20)
                .collect(Collectors.toList());
    }

    @Override
    public List<MatchResult> matchJobsForEmployee(Long employeeId) {
        Employee employee = employeeMapper.selectById(employeeId);
        if (employee == null) return Collections.emptyList();

        Set<String> empSkills = parseSkills(employee.getSkills());
        List<Job> jobs = jobMapper.selectList(null);

        return jobs.stream()
                .filter(job -> job.getStatus() == 1)
                .map(job -> {
                    MatchResult result = new MatchResult();
                    result.setId(job.getId());
                    result.setName(job.getTitle());
                    result.setSkills(job.getSkills());
                    result.setSalary(job.getSalaryMax());
                    result.setLocation(job.getLocation());

                    Set<String> jobSkills = parseSkills(job.getSkills());
                    int skillMatch = calculateSkillMatch(jobSkills, empSkills);
                    int salaryMatch = calculateSalaryMatch(employee.getExpectedSalary(), job.getSalaryMin(), job.getSalaryMax());
                    int score = (skillMatch * 7 + salaryMatch * 3) / 10;

                    result.setMatchScore(score);
                    result.setMatchReason(buildMatchReason(skillMatch, salaryMatch));
                    return result;
                })
                .filter(r -> r.getMatchScore() > 30)
                .sorted(Comparator.comparing(MatchResult::getMatchScore).reversed())
                .limit(20)
                .collect(Collectors.toList());
    }

    private Set<String> parseSkills(String skills) {
        if (skills == null || skills.isBlank()) return Collections.emptySet();
        return Arrays.stream(skills.split("[,，、\\s]+"))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    private int calculateSkillMatch(Set<String> required, Set<String> owned) {
        if (required.isEmpty()) return 50;
        long matched = required.stream().filter(owned::contains).count();
        return (int) (matched * 100 / required.size());
    }

    private int calculateSalaryMatch(java.math.BigDecimal expected, java.math.BigDecimal min, java.math.BigDecimal max) {
        if (expected == null || min == null || max == null) return 50;
        if (expected.compareTo(min) >= 0 && expected.compareTo(max) <= 0) return 100;
        if (expected.compareTo(min) < 0) return 80;
        return 40;
    }

    private String buildMatchReason(int skillMatch, int salaryMatch) {
        StringBuilder sb = new StringBuilder();
        if (skillMatch >= 80) sb.append("技能高度匹配; ");
        else if (skillMatch >= 50) sb.append("技能部分匹配; ");
        else sb.append("技能匹配度较低; ");

        if (salaryMatch >= 80) sb.append("薪资符合预期");
        else sb.append("薪资有差距");
        return sb.toString();
    }
}
