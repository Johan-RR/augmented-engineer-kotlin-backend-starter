package com.it.exalt.application.config

import com.it.exalt.domain.shared.annotation.UseCase
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType

@Configuration
@ComponentScan(
    basePackages = ["com.it.exalt.domain"],
    includeFilters = [ComponentScan.Filter(
        type = FilterType.ANNOTATION,
        classes = [UseCase::class]
    )],
    useDefaultFilters = false
)
open class DomainBeansConfiguration
