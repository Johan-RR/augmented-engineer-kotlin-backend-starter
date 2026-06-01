package com.it.exalt.infrastructure.article

import com.it.exalt.domain.order.Article
import com.it.exalt.domain.order.OrderStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [ArticleRepositoryJpaAdapterIntegrationTest.TestConfig::class])
class ArticleRepositoryJpaAdapterIntegrationTest {

    @Autowired
    private lateinit var jpa: SpringDataArticleRepository

    private lateinit var adapter: ArticleRepositoryJpaAdapter

    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
        adapter = ArticleRepositoryJpaAdapter(jpa)
    }

    @SpringBootConfiguration
    @EnableAutoConfiguration
    open class TestConfig

    @Test
    fun `should save and find article via adapter`() {
        val article = Article(id = "festivalier-99:order-1", name = "Test", quantity = 5, status = OrderStatus.EN_ATTENTE)
        adapter.save(article)

        val found = adapter.findById(article.id)
        assertThat(found).isNotNull()
        assertThat(found!!.name).isEqualTo("Test")
        assertThat(found.quantity).isEqualTo(5)
        assertThat(found.status).isEqualTo(OrderStatus.EN_ATTENTE)
    }

    @Test
    fun `should change status and persist`() {
        val id = "festivalier-99:order-2"
        val article = Article(id = id, name = "Order2", quantity = 0, status = OrderStatus.EN_ATTENTE)
        adapter.save(article)

        adapter.changeStatus(id, OrderStatus.PRETE)

        val found = adapter.findById(id)
        assertThat(found).isNotNull()
        assertThat(found!!.status).isEqualTo(OrderStatus.PRETE)
    }

    @Test
    fun `should find by festivalier id and status`() {
        val a1 = Article(id = "festivalier-42:order-a", name = "o1", quantity = 0, status = OrderStatus.EN_ATTENTE)
        val a2 = Article(id = "festivalier-42:order-b", name = "o2", quantity = 0, status = OrderStatus.EN_ATTENTE)
        val a3 = Article(id = "festivalier-42:order-c", name = "o3", quantity = 0, status = OrderStatus.PRETE)

        adapter.save(a1)
        adapter.save(a2)
        adapter.save(a3)

        val found = adapter.findByFestivalierIdAndStatus("festivalier-42", OrderStatus.EN_ATTENTE)
        assertThat(found).hasSize(2)
    }
}
