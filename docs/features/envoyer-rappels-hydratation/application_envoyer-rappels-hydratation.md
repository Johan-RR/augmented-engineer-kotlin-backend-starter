# Hydration Reminder Notifications : Application Module

**Context**
The application layer exposes two REST endpoints for the hydration reminder feature:
- `GET /attendees/{id}/notifications` — lets a festival goer poll their pending hydration reminders
- `POST /notifications/hydration-reminder` — lets the bartender manually trigger an immediate hydration reminder campaign outside the scheduled window

It also orchestrates the scheduled invocation of `SendHydrationRemindersUseCase` and maps
domain results to HTTP responses.

**Acceptance Criteria**
Feature: Send hydration reminders to festival goers — Application

Scenario: [1] Festival goer polls and retrieves pending reminders
    Given festival goer "Alice" has one pending hydration reminder stored
    When Alice calls GET /attendees/{alice-id}/notifications
    Then the application returns HTTP 200 with a list containing one notification
    And the notification body contains a friendly hydration message

Scenario: [2] Festival goer polls with no pending reminders
    Given festival goer "Alice" has no pending notifications
    When Alice calls GET /attendees/{alice-id}/notifications
    Then the application returns HTTP 200 with an empty list

Scenario: [3] Unknown festival goer polls for notifications
    Given no festival goer exists with ID "unknown-id"
    When GET /attendees/unknown-id/notifications is called
    Then the application returns HTTP 404

Scenario: [4] Bartender manually triggers an immediate hydration reminder
    Given the current time is 2:00 PM on a festival day
    When the bartender calls POST /notifications/hydration-reminder
    Then the use case is triggered immediately
    And the application returns HTTP 204 No Content

Scenario: [5] Bartender trigger is rejected outside the allowed time window
    Given the current time is 8:00 PM on a festival day
    When the bartender calls POST /notifications/hydration-reminder
    Then the application returns HTTP 422 Unprocessable Entity
    And the response body indicates that the sending window is closed

Scenario: [6] Campaign continues despite an isolated storage failure
    Given 100 festival goers are eligible for a reminder
    And notification storage fails for 1 festival goer due to a transient error
    When the use case executes
    Then the application stores reminders for the other 99 festival goers
    And the campaign result includes a summary of successes and failures

**Notes**
- The polling endpoint returns notifications in chronological order.
- The manual trigger endpoint applies the same domain time-window rules as the scheduler.
- Marking a notification as read / consumed is out of scope for this issue.
