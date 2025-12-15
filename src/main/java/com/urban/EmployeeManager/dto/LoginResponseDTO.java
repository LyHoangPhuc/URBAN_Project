// LoginResponseDTO.java
package com.urban.EmployeeManager.dto;

import com.urban.EmployeeManager.enums.Role;
import lombok.Data;

@Data
public class LoginResponseDTO {
    private String message;
    private EmployeeDTO employee;
    private String sessionId;
}

