package org.example.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author: Anakhe Ajayi
 * @Date: 29 Dec, 2023
 */
@Configuration
@Import({RouterConfig.class})
@ComponentScan("org.example.*")
public class ApplicationConfig {

}
