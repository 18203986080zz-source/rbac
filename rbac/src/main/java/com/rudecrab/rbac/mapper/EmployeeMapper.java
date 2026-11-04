package com.rudecrab.rbac.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rudecrab.rbac.model.entity.Employee;
import com.rudecrab.rbac.model.vo.EmployeePageVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 员工 Mapper
 *
 * @author RudeCrab
 */
@Repository
public interface EmployeeMapper extends BaseMapper<Employee> {
    /**
     * 查询员工分页信息（关联公司名称）
     * @param page 分页条件
     * @param wrapper 查询条件
     * @return 分页对象
     */
    IPage<EmployeePageVO> selectPage(Page<EmployeePageVO> page, @Param(Constants.WRAPPER) Wrapper<EmployeePageVO> wrapper);
}
