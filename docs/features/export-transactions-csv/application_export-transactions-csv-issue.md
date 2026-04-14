# Export Transactions to CSV: Application Module Impact
**Context**
The user wants to export their transactions to a CSV file from the application interface.

**Acceptance Criteria**
Feature: Export transactions via API
  In order to download my transactions
  As an authenticated user
  I want an application endpoint that returns a CSV file

Scenario: Download transactions as CSV
  Given an authenticated user with transactions on the selected account
  When the user calls the transactions export endpoint
  Then the application returns a successful response with content type text/csv and a downloadable file

Scenario: Export with date range filter
  Given an authenticated user with transactions across multiple months
  When the user calls the transactions export endpoint with start and end dates
  Then only transactions inside the requested date range are exported

Scenario: Reject export for unauthorized access
  Given a user who is not allowed to access the target account
  When the user calls the transactions export endpoint
  Then the application returns an authorization error and no CSV content
