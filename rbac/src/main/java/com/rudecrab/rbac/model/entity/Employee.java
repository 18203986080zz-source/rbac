package com.rudecrab.rbac.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 员工实体
 *
 * @author RudeCrab
 */
@lombok.Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("employee")
public class Employee extends BaseEntity {
    /**
     * 员工姓名
     */
    private String name;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱（可为空）
     */
    private String email;
    /**
     * 年龄（可为空）
     */
    private Integer age;
    /**
     * 薪资
     */
    private BigDecimal salary;
    /**
     * 所属公司id（公司名称查询时关联查出，不做冗余存储）
     */
    private Long companyId;
    /**
     * 入职时间（新增时由数据库默认值生成）
     */
    private LocalDateTime createTime;
}
