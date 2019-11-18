package com.sample.batch.config.datasource

import com.sample.batch.config.mapper.NamuMasterMapper
import com.sample.batch.config.properties.datasource.NamuMasterDataSourceProperties
import org.mybatis.spring.annotation.MapperScan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@MapperScan(basePackages = ["com.sample.batch"], annotationClass = NamuMasterMapper::class, sqlSessionFactoryRef = "namuMasterSqlSessionFactory")
@EnableTransactionManagement
class NamuMasterDataSourceConfig : DataSourceConfig() {

    @Autowired
    lateinit var namuMasterDatasourceProperties: NamuMasterDataSourceProperties

    @Bean(name = ["namuMasterDataSource"])
    fun namuMasterDataSource(): DataSource {
        return createDatasource(namuMasterDatasourceProperties)
    }
}