package com.dgh.main.admin.druid;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.support.http.StatViewServlet;

@Configuration
public class DruidViewConfig {

	@Bean
	public ServletRegistrationBean<StatViewServlet> druidStatViewServlet() {
		ServletRegistrationBean<StatViewServlet> registrationBean = new ServletRegistrationBean<StatViewServlet>(new StatViewServlet(),  "/druid/*");
		registrationBean.addInitParameter("allow", "127.0.0.1");// IP白名单 (没有配置或者为空，则允许所有访问)
		registrationBean.addInitParameter("deny", "");// IP黑名单 (存在共同时，deny优先于allow)
		registrationBean.addInitParameter("loginUsername", "root");
		registrationBean.addInitParameter("loginPassword", "123456");
		registrationBean.addInitParameter("resetEnable", "false");
		return registrationBean;
	}
}