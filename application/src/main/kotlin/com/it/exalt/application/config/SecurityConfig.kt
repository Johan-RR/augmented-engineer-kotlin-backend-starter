package com.it.exalt.application.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.core.userdetails.User
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.core.userdetails.UserDetailsService

@Configuration
open class SecurityConfig {

    @Bean
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()
            .authorizeHttpRequests { auth ->
                auth.requestMatchers(HttpMethod.POST, "/commandes").authenticated()
                auth.anyRequest().permitAll()
            }
            .httpBasic()

        return http.build()
    }

    @Bean
    open fun userDetailsService(): UserDetailsService {
        val user = User.withUsername("test")
            .password("{noop}test")
            .roles("USER")
            .build()
        return InMemoryUserDetailsManager(user)
    }
}
