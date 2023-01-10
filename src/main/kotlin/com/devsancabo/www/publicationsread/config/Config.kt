package com.devsancabo.www.publicationsread.config

import com.devsancabo.www.publicationsread.populator.Populator
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {
    @Bean
    @ConditionalOnMissingBean
    fun getPopulator() : Populator<Any>  = TODO("No bean of type Populator has been implemented")
}