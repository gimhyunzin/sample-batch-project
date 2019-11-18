package com.sample.batch.config.datasource

import com.sample.batch.config.properties.datasource.BatchDataSourceProperties
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.batch.BatchProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Configuration
@EnableBatchProcessing
class BatchDataSourceConfig : DataSourceConfig() {

    @Autowired
    lateinit var batchDataSourceProperties: BatchDataSourceProperties

    @Bean(name = ["batchDataSource"])
    @Primary
    fun batchDataSource(): DataSource {
        return createDatasource(batchDataSourceProperties)
    }
}