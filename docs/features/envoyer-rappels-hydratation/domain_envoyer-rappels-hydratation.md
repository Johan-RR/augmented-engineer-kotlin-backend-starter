# Hydration Reminder Notifications : Domain Module

**Context**
The bartender wants regular water-drinking reminders sent to festival goers during hot weather.
The domain is responsible for the business rules that govern when a reminder is due and at what
frequency: every hour for all festival goers between 11:00 AM and 7:00 PM, but every 30 minutes
for festival goers who have consumed more than 3 alcoholic drinks in the past hour (derived from
order history).

The domain defines:
- `SendHydrationRemindersUseCase` — the primary use case port
- `HydrationReminder` — value object carrying the target festival goer and the selected frequency
- `NotificationFrequency` — enum: `HOURLY` / `THIRTY_MINUTES`
- `AlcoholConsumptionQueryPort` — secondary port to read alcoholic drink count from order history
- `NotificationDispatchPort` — secondary port to dispatch generated reminders

**Acceptance Criteria**
Feature: Send hydration reminders to festival goers

Scenario: [1] Send an hourly reminder to all festival goers during the allowed time window
    Given the current time is 2:00 PM on a festival day
    And the allowed sending window is 11:00 AM to 7:00 PM
    When the domain evaluates which reminders are due for this slot
    Then every registered festival goer is eligible for a hydration reminder
    And the frequency applied is HOURLY

Scenario: [2] Apply a 30-minute frequency for a festival goer who exceeded the alcohol threshold
    Given the current time is 2:30 PM on a festival day
    And festival goer "Alice" has consumed 4 alcoholic drinks in the past hour
    When the domain evaluates which reminders are due for this slot
    Then "Alice" is eligible for a hydration reminder
    And the frequency applied to "Alice" is THIRTY_MINUTES

Scenario: [3] Keep standard frequency when the threshold is exactly met but not exceeded
    Given the current time is 3:30 PM on a festival day
    And festival goer "Bob" has consumed exactly 3 alcoholic drinks in the past hour
    When the domain evaluates which reminders are due for this slot
    Then "Bob" is not marked as requiring increased frequency
    And the frequency applied to "Bob" is HOURLY

Scenario: [4] Skip all reminders when outside the allowed time window (too late)
    Given the current time is 8:00 PM on a festival day
    And the allowed sending window is 11:00 AM to 7:00 PM
    When the domain evaluates which reminders are due for this slot
    Then no hydration reminders are generated

Scenario: [5] Skip all reminders when outside the allowed time window (too early)
    Given the current time is 10:00 AM on a festival day
    And the allowed sending window is 11:00 AM to 7:00 PM
    When the domain evaluates which reminders are due for this slot
    Then no hydration reminders are generated

**Notes**
- The "more than 3" comparison is strict: exactly 3 alcoholic drinks does NOT trigger increased frequency.
- Festival timezone must be centralised at the domain rules level to avoid ambiguity.
- Alcohol consumption is always derived from order history via `AlcoholConsumptionQueryPort`; no separate counter is maintained.
