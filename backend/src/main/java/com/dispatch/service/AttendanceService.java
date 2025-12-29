package com.dispatch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dispatch.entity.Attendance;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService extends IService<Attendance> {
    Page<Attendance> pageList(int current, int size, Long employeeId, LocalDate startDate, LocalDate endDate);
    void batchSave(List<Attendance> attendances);
    List<Attendance> getMonthlyAttendance(Long dispatchId, Integer year, Integer month);
    Page<Attendance> getByUserId(Long userId, int current, int size, LocalDate startDate, LocalDate endDate);
}
