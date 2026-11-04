package com.rudecrab.rbac.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 员工分页展示对象
 *
 * @author RudeCrab
 */
@Data
public class EmployeePageVO {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private Integer age;
    private BigDecimal salary;
    private Long companyId;
    /**
     * 公司名称，关联查询，不在员工表中冗余存储
     */
    private String companyName;
    private LocalDateTime createTime;
}
