package com.dgh.main.admin;

import java.util.concurrent.Executor;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan("com.dgh.main.admin.mapper")
@ComponentScan(basePackages = {"com.dgh.main.admin"})
@EnableAsync
@DubboComponentScan("com.dgh.main.admin.dubbo.service")
@EnableTransactionManagement
//缓存注解
@EnableCaching
public class AdminApplication {
 
	public static void main(String[] args) {
		new EmbeddedZooKeeper(2181, false).start();
		SpringApplication.run(AdminApplication.class, args);
	}

	@Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }
}
