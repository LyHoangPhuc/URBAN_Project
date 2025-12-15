package com.urban.EmployeeManager.dto;

import com.urban.EmployeeManager.enums.Gender;
import com.urban.EmployeeManager.enums.Position;
import com.urban.EmployeeManager.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeCreateDTO {
    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotBlank(message = "Username không được để trống")
    private String username;

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    private String phoneNumber;
    private Gender gender;
    private String address;

    @NotNull(message = "Chức vụ không được để trống")
    private Position position;

    @NotNull(message = "Văn phòng không được để trống")
    private Long officeId;

    private String avatar;
    private Role role = Role.USER;
}

