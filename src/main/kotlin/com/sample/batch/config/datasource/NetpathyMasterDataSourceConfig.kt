package com.sample.batch.config.datasource

import com.sample.batch.config.mapper.NetpathyMasterMapper
import com.sample.batch.config.properties.datasource.NetpathyMasterDataSourceProperties
import org.mybatis.spring.annotation.MapperScan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@MapperScan(basePackages = ["com.sample.batch"], annotationClass = NetpathyMasterMapper::class, sqlSessionFactoryRef = "netpathyMasterSqlSessionFactory")
@EnableTransactionManagement
class NetpathyMasterDataSourceConfig : DataSourceConfig() {

    @Autowired
    lateinit var netpathyMasterDataSourceProperties: NetpathyMasterDataSourceProperties

    @Bean(name = ["netpathyMasterDataSource"])
    fun nproMasterDataSource(): DataSource {
        return createDatasource(netpathyMasterDataSourceProperties)
    }
}
