package com.rudecrab.rbac.controller.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rudecrab.rbac.annotation.Auth;
import com.rudecrab.rbac.enums.ResultCode;
import com.rudecrab.rbac.exception.ApiException;
import com.rudecrab.rbac.model.param.EmployeeParam;
import com.rudecrab.rbac.model.vo.EmployeePageVO;
import com.rudecrab.rbac.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * 员工接口
 *
 * @author RudeCrab
 */
@RestController
@RequestMapping("/API/employee")
@Auth(id = 4000, name = "员工管理")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    @Auth(id = 1, name = "新增员工")
    public String createEmployee(@RequestBody @Validated(EmployeeParam.Create.class) EmployeeParam param) {
        employeeService.createEmployee(param);
        return "操作成功";
    }

    @DeleteMapping
    @Auth(id = 2, name = "删除员工")
    public String deleteEmployee(Long[] ids) {
        if (ArrayUtils.isEmpty(ids)) {
            throw new ApiException(ResultCode.VALIDATE_FAILED);
        }
        employeeService.removeByIds(Arrays.asList(ids));
        return "操作成功";
    }

    @PutMapping
    @Auth(id = 3, name = "编辑员工")
    public String updateEmployee(@RequestBody @Validated(EmployeeParam.Update.class) EmployeeParam param) {
        employeeService.updateEmployee(param);
        return "操作成功";
    }

    @GetMapping("/page/{current}")
    @Auth(id = 4, name = "查询员工")
    public IPage<EmployeePageVO> getPage(@PathVariable("current") int current) {
        Page<EmployeePageVO> page = new Page<>();
        OrderItem orderItem = new OrderItem();
        // 按入职时间倒序，和 DataController 保持一致
        orderItem.setColumn("e.create_time");
        orderItem.setAsc(false);
        page.setCurrent(current).addOrder(orderItem);
        return employeeService.selectPage(page);
    }
}
