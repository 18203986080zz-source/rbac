package com.rudecrab.rbac.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rudecrab.rbac.model.entity.Employee;
import com.rudecrab.rbac.model.param.EmployeeParam;
import com.rudecrab.rbac.model.vo.EmployeePageVO;

/**
 * 员工服务
 *
 * @author RudeCrab
 */
public interface EmployeeService extends IService<Employee> {
    /**
     * 分页查询员工（含公司名称）
     * @param page 分页参数
     * @return 分页对象
     */
    IPage<EmployeePageVO> selectPage(Page<EmployeePageVO> page);

    /**
     * 新增员工
     * @param param 入参
     */
    void createEmployee(EmployeeParam param);

    /**
     * 编辑员工
     * @param param 入参
     */
    void updateEmployee(EmployeeParam param);
}
