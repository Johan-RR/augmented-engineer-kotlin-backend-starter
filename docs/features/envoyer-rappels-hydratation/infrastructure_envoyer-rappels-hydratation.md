# Hydration Reminder Notifications : Infrastructure Module

**Context**
The infrastructure layer provides the scheduling, data querying, and notification storage
mechanisms required by the hydration reminder feature.

Responsibilities:
- Schedule execution of the use case every 30 minutes (at :00 and :30 marks) within the allowed window
- Implement `AlcoholConsumptionQueryPort` by querying fulfilled order history
- Implement `NotificationDispatchPort` by persisting generated reminders so that festival goers can poll them via the application layer
- Guarantee idempotency: a given (festivalGoerId, slotTimestamp) pair is never stored twice

**Acceptance Criteria**
Feature: Send hydration reminders to festival goers — Infrastructure

Scenario: [1] Trigger the hourly reminder campaign between 11:00 AM and 7:00 PM
    Given the scheduling mechanism is active
    When the clock reaches a full hour between 11:00 AM and 7:00 PM
    Then the infrastructure triggers one execution of the hydration reminder use case
    And the execution is traced with the slot timestamp

Scenario: [2] Trigger a supplementary slot at 30-minute marks for increased-frequency profiles
    Given the scheduling mechanism is active
    When the clock reaches a half-hour mark between 11:30 AM and 6:30 PM
    Then the infrastructure triggers one execution of the hydration reminder use case
    And the execution is traced with slot type THIRTY_MINUTES

Scenario: [3] Count alcoholic drinks in a rolling one-hour window from order history
    Given festival goer "Alice" has fulfilled orders containing alcoholic drinks with timestamps available
    When the AlcoholConsumptionQueryPort adapter is called for "Alice" at 3:30 PM
    Then only alcoholic drink line items from orders fulfilled between 2:30 PM and 3:30 PM are counted
    And the count is returned to the domain use case

Scenario: [4] Persist a generated hydration reminder for later polling
    Given a hydration reminder has been generated for festival goer "Alice" for slot 4:00 PM
    When the NotificationDispatchPort adapter stores the reminder
    Then the reminder is persisted and retrievable by Alice's ID

Scenario: [5] Prevent duplicate notifications for the same festival goer and slot
    Given a hydration reminder for festival goer "Alice" at slot 4:00 PM is already stored
    When a second execution attempts to store the same reminder for the same slot
    Then the infrastructure blocks the duplicate
    And an idempotency trace is recorded

Scenario: [6] Do not execute any campaign outside the allowed time window
    Given the scheduling mechanism is active
    When the clock reaches 8:00 PM
    Then no campaign execution is triggered
    And no reminder is stored

**Notes**
- Idempotency key: composite of `festivalGoerId` + `slotTimestamp`.
- Festival timezone must be configurable at the infrastructure level and aligned with the domain layer.
- The notification store acts as a persistent outbox that the application layer exposes via polling endpoint.
