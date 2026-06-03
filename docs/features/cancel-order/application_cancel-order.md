# Cancel Order : Application Module impact

**Context**
The application layer must expose an HTTP endpoint that allows an authenticated festival goer to
cancel a pending order. It maps the incoming request to a `CancelOrderCommand`, delegates to the
`CancelOrderUseCase`, and returns a structured HTTP response with a cancellation confirmation.
Domain exceptions are mapped to the appropriate HTTP status codes.

**Acceptance Criteria**
Feature: Cancel Order

Scenario: Successfully cancel a pending order via HTTP
    Given an authenticated festival goer who owns an order with status EN_ATTENTE
    When calling DELETE /commandes/{id}
    Then the application returns HTTP 200 with a CancelOrderResponse confirming the cancellation

Scenario: Reject cancellation of an acknowledged order via HTTP
    Given an authenticated festival goer who owns an order with status ACQUITTEE
    When calling DELETE /commandes/{id}
    Then the application returns HTTP 409 Conflict with an error message indicating the order cannot be cancelled

Scenario: Reject cancellation of a non-existent order via HTTP
    Given an authenticated festival goer
    When calling DELETE /commandes/{id} with an order ID that does not exist
    Then the application returns HTTP 404 Not Found

Scenario: Reject unauthenticated cancellation request
    Given an unauthenticated caller
    When calling DELETE /commandes/{id}
    Then the application returns HTTP 401 Unauthorized

**Notes**
- Endpoint: `DELETE /commandes/{id}` on `OrderController`.
- Map `OrderCancellationNotAllowedException` → HTTP 409.
- Map `OrderNotFoundException` → HTTP 404.
- `CancelOrderResponse` DTO must include the order ID and the new status.
