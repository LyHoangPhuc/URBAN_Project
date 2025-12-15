package com.urban.EmployeeManager.util;

import java.util.UUID;

public class SecurityUtil {
    public static String generateRandomToken(){
        return UUID.randomUUID().toString();
    }
}