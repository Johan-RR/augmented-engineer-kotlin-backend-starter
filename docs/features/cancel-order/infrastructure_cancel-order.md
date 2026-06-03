# Cancel Order : Infrastructure Module impact

**Context**
The infrastructure layer must persist two side-effects triggered by a cancellation: updating the
order status to ANNULEE in the database, and crediting the refunded tokens back to the festival
goer's balance. Both operations must be executed within a single transaction by adapters implementing
the domain repository ports.

**Acceptance Criteria**
Feature: Cancel Order

Scenario: Persist order status change to ANNULEE
    Given an existing order with status EN_ATTENTE in the database
    When the CancelOrderUseCase is executed for that order
    Then the order's persisted status is updated to ANNULEE

Scenario: Persist drink token refund after cancellation
    Given a festival goer whose drink token balance was debited when the order was placed
    And the order has status EN_ATTENTE in the database
    When the order is cancelled
    Then the festival goer's drink token balance in the database is incremented by the refunded amount

Scenario: Persist food token refund after cancellation
    Given a festival goer whose food token balance was debited when the order was placed
    And the order has status EN_ATTENTE in the database
    When the order is cancelled
    Then the festival goer's food token balance in the database is incremented by the refunded amount

**Notes**
- `ArticleRepositoryJpaAdapter.changeStatus` must handle the new `ANNULEE` enum value.
- If a `FestivalierRepositoryJpaAdapter` does not yet exist, it must be introduced to support the token balance update.
- Both the status update and the token refund must be executed in a single transaction.
