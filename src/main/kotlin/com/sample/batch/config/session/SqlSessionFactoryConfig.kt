package com.sample.batch.config.session


import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionFactoryBean
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import javax.sql.DataSource

@Configuration
class SqlSessionFactoryConfig {

    val mybatisMapperResourcePath = "classpath*:mapper/**/*.xml"

    @Bean(name = ["namuMasterSqlSessionFactory"])
    @Throws(Exception::class)
    fun namuMasterSqlSessionFactory(@Qualifier("namuMasterDataSource") namuMasterDataSource: DataSource): SqlSessionFactory? {
        return this.createSqlSessionFactory(namuMasterDataSource)
    }

    @Bean(name = ["namuSlaveSqlSessionFactory"])
    @Throws(Exception::class)
    fun namuSlaveSqlSessionFactory(@Qualifier("namuSlaveDataSource") namuSlaveDataSource: DataSource): SqlSessionFactory? {
        return this.createSqlSessionFactory(namuSlaveDataSource)
    }

    @Bean(name = ["nproMasterSqlSessionFactory"])
    @Throws(Exception::class)
    fun nproMasterSqlSessionFactory(@Qualifier("nproMasterDataSource") nproMasterDataSource: DataSource): SqlSessionFactory? {
        return this.createSqlSessionFactory(nproMasterDataSource)
    }

    @Bean(name = ["netpathyMasterSqlSessionFactory"])
    @Throws(Exception::class)
    fun netpathyMasterSqlSessionFactory(@Qualifier("netpathyMasterDataSource") netpathyMasterDataSource: DataSource): SqlSessionFactory? {
        return this.createSqlSessionFactory(netpathyMasterDataSource)
    }

    @Throws(Exception::class)
    private fun createSqlSessionFactory(dataSource: DataSource): SqlSessionFactory? {
        var sqlSessionFactory = SqlSessionFactoryBean()
        sqlSessionFactory.setDataSource(dataSource)

        var resolver = PathMatchingResourcePatternResolver()
        sqlSessionFactory.setMapperLocations(resolver.getResources(mybatisMapperResourcePath))

        return sqlSessionFactory.`object`
    }
}
