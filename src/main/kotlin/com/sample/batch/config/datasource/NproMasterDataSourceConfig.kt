package com.sample.batch.config.datasource

import com.sample.batch.config.mapper.NproMasterMapper
import com.sample.batch.config.properties.datasource.NproMasterDataSourceProperties
import org.mybatis.spring.annotation.MapperScan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@MapperScan(basePackages = ["com.sample.batch"], annotationClass = NproMasterMapper::class, sqlSessionFactoryRef = "nproMasterSqlSessionFactory")
@EnableTransactionManagement
class NproMasterDataSourceConfig : DataSourceConfig() {

    @Autowired
    lateinit var nproMasterDatasourceProperties: NproMasterDataSourceProperties

    @Bean(name = ["nproMasterDataSource"])
    fun nproMasterDataSource(): DataSource {
        return createDatasource(nproMasterDatasourceProperties)
    }
}
