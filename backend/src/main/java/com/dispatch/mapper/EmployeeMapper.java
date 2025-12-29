package com.dispatch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dispatch.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
