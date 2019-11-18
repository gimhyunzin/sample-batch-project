package com.sample.batch.config.session

import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionTemplate
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SqlSessionTemplateConfig {

    @Bean(name = ["namuMasterSqlSessionTemplate"])
    @Throws(Exception::class)
    fun namuMasterSessionTemplate(@Qualifier("namuMasterSqlSessionFactory") namuMasterSqlSessionFactory: SqlSessionFactory): SqlSessionTemplate {
        return this.createSessionTemplate(namuMasterSqlSessionFactory)
    }

    @Bean(name = ["namuSlaveSqlSessionTemplate"])
    @Throws(Exception::class)
    fun namuSlaveSessionTemplate(@Qualifier("namuSlaveSqlSessionFactory") namuSlaveSessionFactory: SqlSessionFactory): SqlSessionTemplate {
        return this.createSessionTemplate(namuSlaveSessionFactory)
    }

    @Bean(name = ["nproMasterSqlSessionTemplate"])
    @Throws(Exception::class)
    fun nproMasterSessionTemplate(@Qualifier("nproMasterSqlSessionFactory") nproMasterSqlSessionFactory: SqlSessionFactory): SqlSessionTemplate {
        return this.createSessionTemplate(nproMasterSqlSessionFactory)
    }

    @Bean(name = ["netpathyMasterSqlSessionTemplate"])
    @Throws(Exception::class)
    fun netpathyMasterSessionTemplate(@Qualifier("netpathyMasterSqlSessionFactory") netpathyMasterSqlSessionFactory: SqlSessionFactory): SqlSessionTemplate {
        return this.createSessionTemplate(netpathyMasterSqlSessionFactory)
    }

    private fun createSessionTemplate(sqlSessionFactory: SqlSessionFactory): SqlSessionTemplate {
        return SqlSessionTemplate(sqlSessionFactory)
    }
}
