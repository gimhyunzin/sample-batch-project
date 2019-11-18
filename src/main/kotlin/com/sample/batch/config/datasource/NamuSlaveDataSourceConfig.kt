package com.sample.batch.config.datasource

import com.sample.batch.config.mapper.NamuSlaveMapper
import com.sample.batch.config.properties.datasource.NamuSlaveDataSourceProperties
import org.mybatis.spring.annotation.MapperScan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@MapperScan(basePackages = ["com.sample.batch"], annotationClass = NamuSlaveMapper::class, sqlSessionFactoryRef = "namuSlaveSqlSessionFactory")
@EnableTransactionManagement
class NamuSlaveDataSourceConfig : DataSourceConfig() {

    @Autowired
    lateinit var namuSlaveDatasourceProperties: NamuSlaveDataSourceProperties

    @Bean(name = ["namuSlaveDataSource"])
    fun namuSlaveDataSource(): DataSource {
        return createDatasource(namuSlaveDatasourceProperties)
    }
}