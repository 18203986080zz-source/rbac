package com.rudecrab.rbac.model.param;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * 员工相关参数
 *
 * @author RudeCrab
 */
@Data
public class EmployeeParam {

    @NotNull(message = "员工id不能为空", groups = Update.class)
    private Long id;

    @NotBlank(message = "员工姓名不能为空", groups = {Create.class, Update.class})
    private String name;

    @NotBlank(message = "手机号不能为空", groups = {Create.class, Update.class})
    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式不正确", groups = {Create.class, Update.class})
    private String phone;

    @Email(message = "邮箱格式不正确", groups = {Create.class, Update.class})
    private String email;

    @Min(value = 0, message = "年龄不能小于0", groups = {Create.class, Update.class})
    @Max(value = 150, message = "年龄不能大于150", groups = {Create.class, Update.class})
    private Integer age;

    @NotNull(message = "薪资不能为空", groups = {Create.class, Update.class})
    @DecimalMin(value = "0", message = "薪资不能为负数", groups = {Create.class, Update.class})
    private BigDecimal salary;

    @NotNull(message = "所属公司不能为空", groups = {Create.class, Update.class})
    private Long companyId;

    public interface Create {}

    public interface Update {}
}
