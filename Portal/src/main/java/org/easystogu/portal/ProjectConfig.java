package org.easystogu.portal;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//this is the main configuration for As mode application.

@Configuration
@ComponentScan(basePackages = "org.easystogu.portal")
@EnableWebMvc
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableScheduling
@Lazy(false)
public class ProjectConfig {

}
