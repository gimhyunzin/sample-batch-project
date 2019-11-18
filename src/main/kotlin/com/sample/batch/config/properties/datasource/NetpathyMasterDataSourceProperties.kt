package com.sample.batch.config.properties.datasource

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "spring.datasource.netpathy.master")
class NetpathyMasterDataSourceProperties: DataSourceProperties()