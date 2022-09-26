package com.xuzekun.backend.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@MapperScan("com.mojor.server.entities")
public class MybatisPlusConfiguration implements MetaObjectHandler {

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
        return interceptor;
    }

    private static final String CREATED = "created";
    private static final String CREATED_BY = "createdBy";
    private static final String MODIFIED = "modified";
    private static final String MODIFIED_BY = "modifiedBy";
    private static final String DELETED = "deleted";
    private static final String USERNAME = "system";

    @Override
    public void insertFill(MetaObject metaObject) {
        String username = USERNAME;
        this.strictInsertFill(metaObject, CREATED, LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, CREATED_BY, String.class, username);
        this.strictInsertFill(metaObject, MODIFIED, LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, MODIFIED_BY, String.class, username);
        this.strictInsertFill(metaObject, DELETED, Boolean.class, false);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String username = USERNAME;
        this.strictUpdateFill(metaObject, MODIFIED, LocalDateTime::now, LocalDateTime.class);
        this.strictUpdateFill(metaObject, MODIFIED_BY, String.class, username);
    }

}
