package com.it.exalt.infrastructure.article

import com.it.exalt.domain.order.Article
import com.it.exalt.domain.order.ArticleRepository
import com.it.exalt.domain.order.OrderStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ArticleRepositoryIntegrationTest {

    private val articleRepository: ArticleRepository = InMemoryArticleRepository()

    @Test
    fun `should save and find article`() {
        // Given an article
        val article = Article(id = "mojito", name = "Mojito", quantity = 10)

        // When saving via repository (expected to fail because no implementation provided)
        articleRepository.save(article)

        // Then it can be retrieved
        val found = articleRepository.findById(article.id)
        assertThat(found).isNotNull()
        assertThat(found!!.name).isEqualTo("Mojito")
        assertThat(found.quantity).isEqualTo(10)
    }

    @Test
    fun `should update order status to PRETE when updating status`() {
        // Scenario: Mettre à jour le statut d'une commande
        // Given une commande sauvegardée avec le statut "EN_ATTENTE"
        // When on met à jour son statut à "PRETE"
        // Then en retrouvant la commande par son identifiant, elle a le statut "PRETE"

        // Minimal approach: reuse the existing InMemoryArticleRepository as a lightweight
        // backing store for the test. We map an order to an Article for persistence in tests:
        // - Article.id -> orderId
        // - Article.name -> order status (string)
        // This is a test-only shim; production should have a real OrderRepository.

        val repo: ArticleRepository = InMemoryArticleRepository()

        val orderId = "order-1"
        val initialStatus = OrderStatus.EN_ATTENTE
        val updatedStatus = OrderStatus.PRETE

        // Given: saved with EN_ATTENTE
        val asArticle = Article(id = orderId, name = "order", quantity = 0, status = initialStatus)
        repo.save(asArticle)

        // When: update status to PRETE
        repo.changeStatus(orderId, updatedStatus)

        // Then: retrieving by id returns the updated status
        val found = repo.findById(orderId)
        assertThat(found).isNotNull
        assertThat(found!!.status).isEqualTo(updatedStatus)

        // TODO: replace this test with a real OrderRepository integration test when
        // a production adapter is implemented (e.g. OrderEntity + OrderJpaRepository).
    }

}
