package com.convrt.stream.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.concurrent.Callable;

@Slf4j
@EnableAsync
@Configuration
public class AsyncConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(-1);
        configurer.setTaskExecutor(asyncTaskExecutor());
    }

    @Bean
    public AsyncTaskExecutor asyncTaskExecutor() {
        return new SimpleAsyncTaskExecutor("async");
    }


//    @Override
//    @Bean(name = "taskExecutor")
//    public AsyncTaskExecutor getAsyncExecutor() {
//        log.debug("Creating Async Task Executor");
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(5);
//        executor.setMaxPoolSize(10);
//        executor.setQueueCapacity(20);
//        executor.setThreadNamePrefix("async-stream");
//        return executor;
//    }
//
//    @Override
//    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//        return new SimpleAsyncUncaughtExceptionHandler();
//    }
//
//    /** Configure async support for Spring MVC. */
//    @Bean
//    public WebMvcConfigurerAdapter webMvcConfigurerAdapter(AsyncTaskExecutor taskExecutor, CallableProcessingInterceptor callableProcessingInterceptor) {
//        return new WebMvcConfigurerAdapter() {
//            @Override
//            public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
//                configurer.setDefaultTimeout(-1).setTaskExecutor(taskExecutor);
//                configurer.registerCallableInterceptors(callableProcessingInterceptor);
//                super.configureAsyncSupport(configurer);
//            }
//        };
//    }
//
//    @Bean
//    public CallableProcessingInterceptor callableProcessingInterceptor() {
//        return new TimeoutCallableProcessingInterceptor() {
//            @Override
//            public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
//                log.error("timeout!");
//                return super.handleTimeout(request, task);
//            }
//        };
//    }
}
