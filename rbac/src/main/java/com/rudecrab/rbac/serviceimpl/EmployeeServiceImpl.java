package com.rudecrab.rbac.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rudecrab.rbac.exception.ApiException;
import com.rudecrab.rbac.mapper.EmployeeMapper;
import com.rudecrab.rbac.model.entity.Employee;
import com.rudecrab.rbac.model.param.EmployeeParam;
import com.rudecrab.rbac.model.vo.EmployeePageVO;
import com.rudecrab.rbac.service.CompanyService;
import com.rudecrab.rbac.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 员工服务实现
 *
 * @author RudeCrab
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private CompanyService companyService;

    @Override
    public IPage<EmployeePageVO> selectPage(Page<EmployeePageVO> page) {
        QueryWrapper<EmployeePageVO> queryWrapper = new QueryWrapper<>();
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public void createEmployee(EmployeeParam param) {
        checkPhone(param.getPhone(), null);
        checkCompany(param.getCompanyId());
        Employee employee = new Employee();
        employee.setName(param.getName());
        employee.setPhone(param.getPhone());
        employee.setEmail(param.getEmail());
        employee.setAge(param.getAge());
        employee.setSalary(param.getSalary());
        employee.setCompanyId(param.getCompanyId());
        // createTime 不设置，由数据库 DEFAULT CURRENT_TIMESTAMP 生成
        save(employee);
    }

    @Override
    public void updateEmployee(EmployeeParam param) {
        if (getById(param.getId()) == null) {
            throw new ApiException("员工不存在");
        }
        checkPhone(param.getPhone(), param.getId());
        checkCompany(param.getCompanyId());
        Employee employee = new Employee();
        // 注意：id 的 setter 在 BaseEntity 上（无 @Accessors(chain=true)，返回 void），不能链式调用
        employee.setId(param.getId());
        employee.setName(param.getName());
        employee.setPhone(param.getPhone());
        employee.setEmail(param.getEmail());
        employee.setAge(param.getAge());
        employee.setSalary(param.getSalary());
        employee.setCompanyId(param.getCompanyId());
        // createTime 不更新（实体字段为 null 时 MyBatis-Plus updateById 跳过该列）
        updateById(employee);
    }

    /**
     * 校验手机号唯一。excludeId 用于编辑时排除自身
     */
    private void checkPhone(String phone, Long excludeId) {
        Employee one = lambdaQuery()
                .eq(Employee::getPhone, phone)
                .ne(excludeId != null, Employee::getId, excludeId)
                .one();
        if (one != null) {
            throw new ApiException("手机号已被使用");
        }
    }

    /**
     * 校验公司存在，防止脏数据写入（参考 data 表公司名冗余错误的教训，公司名称不做冗余）
     */
    private void checkCompany(Long companyId) {
        if (companyService.getById(companyId) == null) {
            throw new ApiException("所属公司不存在");
        }
    }
}
