package com.dgh.main.admin.mybatisplus.config;

import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

@Configuration
public class MybatisPlusConfig {

	/**
	 *   乐观锁，version仅支持int,Integer,long,Long,Date,Timestamp
	 *   实体类必须添加@Version注解
	 */
	@Bean 
	public OptimisticLockerInterceptor optimisticLockerInterceptor() {
	    return new OptimisticLockerInterceptor();
	}
	
	/**
	 *	 分页插件
	 * @return
	 */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
         paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
         paginationInterceptor.setLimit(500);
        return paginationInterceptor;
    }
    
    
    /**
     * SQL执行效率插件
     */
    @Bean
    @Profile({"dev","test"})// 设置 dev test 环境开启
    public PerformanceMonitorInterceptor performanceInterceptor() {
        return new PerformanceMonitorInterceptor();
    }
    
    
}