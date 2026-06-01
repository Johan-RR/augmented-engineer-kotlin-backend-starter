package com.it.exalt.infrastructure.article

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SpringDataArticleRepository : JpaRepository<ArticleEntity, String>
