# Cancel Order : Domain Module impact

**Context**
A festival goer must be able to cancel a pending order. The domain must enforce that only orders
not yet acknowledged by the bartender can be cancelled. Upon cancellation, the tokens used to place
the order must be refunded to the festival goer's balance, and the order status must be updated to
ANNULEE. A new `CancelOrderUseCase` port and `CancelOrderCommand` value must be introduced.
A new `ANNULEE` value must be added to the `OrderStatus` enum.

**Acceptance Criteria**
Feature: Cancel Order

Scenario: Successfully cancel a pending order
    Given a festival goer who owns an order with status EN_ATTENTE
    When the festival goer requests the cancellation of that order
    Then the order status is updated to ANNULEE
    And the festival goer's token balance is refunded with all tokens used for the order
    And a cancellation confirmation is returned

Scenario: Reject cancellation of an acknowledged order
    Given a festival goer who owns an order with status ACQUITTEE
    When the festival goer requests the cancellation of that order
    Then the cancellation is rejected with an OrderCancellationNotAllowedException
    And the order status and the festival goer's token balance remain unchanged

Scenario: Refund drink tokens upon cancellation
    Given a festival goer who placed an order for 1 normal alcoholic drink and 1 premium alcoholic drink
    And the order has status EN_ATTENTE
    When the festival goer cancels the order
    Then 3 drink tokens are refunded to the festival goer's balance (1 for normal + 2 for premium)

Scenario: Refund food tokens upon cancellation
    Given a festival goer who placed an order for 1 meal and 1 snack
    And the order has status EN_ATTENTE
    When the festival goer cancels the order
    Then 4 food tokens are refunded to the festival goer's balance (3 for meal + 1 for snack)

Scenario: Reject cancellation of a non-existent order
    Given an order ID that does not exist in the system
    When the festival goer requests the cancellation of that order
    Then the cancellation is rejected with an OrderNotFoundException

**Notes**
- Add `ANNULEE` to the `OrderStatus` enum in `DomainModels.kt`.
- The refund logic must be the exact mirror of the token-debit logic in `PlaceOrderUseCaseImpl`.
- `CancelOrderCommand(orderId: String, festivalierId: String)` and `CancelOrderResult` must live in `DomainModels.kt`.
