// DataSeedingConfig.java
package com.urban.EmployeeManager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.data")
public class DataSeedingConfig {
    private boolean seedEnabled = true;
}

