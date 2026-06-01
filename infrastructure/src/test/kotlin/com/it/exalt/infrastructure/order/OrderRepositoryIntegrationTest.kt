package com.it.exalt.infrastructure.order

import com.it.exalt.domain.order.Article
import com.it.exalt.domain.order.ArticleRepository
import com.it.exalt.domain.order.OrderStatus
import com.it.exalt.infrastructure.article.InMemoryArticleRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OrderRepositoryIntegrationTest {

    // Using the production in-memory repository implementation extracted during refactor.

    @Test
    fun `shouldReturnExactly2PendingOrders_whenSearchingPendingForFestivalier42`() {
        // Scenario: Retrouver les commandes en attente d'un festivalier
        // Given 3 commandes pour le festivalier "festivalier-42" :
        //   | statut     |
        //   | EN_ATTENTE |
        //   | EN_ATTENTE |
        //   | PRETE      |

        val repo: ArticleRepository = InMemoryArticleRepository()

        val o1 = Article(id = "festivalier-42:order-1", name = "order-1", quantity = 0, status = OrderStatus.EN_ATTENTE)
        val o2 = Article(id = "festivalier-42:order-2", name = "order-2", quantity = 0, status = OrderStatus.EN_ATTENTE)
        val o3 = Article(id = "festivalier-42:order-3", name = "order-3", quantity = 0, status = OrderStatus.PRETE)

        repo.save(o1)
        repo.save(o2)
        repo.save(o3)

        // When on cherche les commandes avec le statut "EN_ATTENTE" pour "festivalier-42"
        val found = repo.findByFestivalierIdAndStatus("festivalier-42", OrderStatus.EN_ATTENTE)

        // Then on obtient exactement 2 commandes
        assertThat(found).hasSize(2)

        // TODO: implement ArticleRepository.findByFestivalierIdAndStatus in production adapters
        // (e.g. add OrderEntity + OrderJpaRepository and implement query in ArticleRepositoryJpaAdapter)
    }

}
