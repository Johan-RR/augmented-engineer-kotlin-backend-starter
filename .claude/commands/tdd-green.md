---
description: Make a previously failing test pass with minimal, test-local code (Green step).
argument-hint: <testFilePath> <testMethod>
allowed-tools: Bash, Read, Write, Edit, Grep, Glob
---

# Green TDD step

Input (test file path and test method): $ARGUMENTS

## Goal
Implement the minimal code necessary to make a test written during the Red step pass. Implementation should be added in the test file itself (for example helper classes, test doubles, stubs, or internal helper functions), without modifying the targeted test body or other tests.
Important: iterate as needed — after each minimal change run the targeted test and continue making only minimal, test-local edits (within the prompt constraints) until the test is green.

Crucial clarification: The Green step's purpose is strictly to make the test pass with the minimal, test-local scaffolding required. Do NOT implement production behaviour or create/modify production files under `src/main/kotlin` in this step. If the test requires objects or functions that belong to production, prefer defining small stubs, fakes or helper types directly inside the test file (or test sources). Any extraction of those stubs into production code and full implementation belongs to the Refactor step.

## Instructions
1. Parse the provided input: it must include the test file path (workspace-relative) and the test method name to make pass.
2. Confirm the test file and method exist and that the test currently fails (if possible, run only the targeted test).
3. Implement only the code strictly required for the targeted test to succeed. Mandatory rules:
   - Add code inside the test file itself (e.g., inner classes, companion objects, stubs, helpers defined in the test file).
  - Prefer adding test-local stubs/fakes/helpers inside the test file or test sources. Do NOT create production files or move logic into `src/main/kotlin` in the Green step.
   - Do not modify the body of the targeted test method.
   - Do not modify other tests in the same class/file.
   - Do not introduce functionality beyond what the test requires.
   - Avoid changing production code. If a production change is absolutely necessary and justified by the test, document the reason in the JSON output.
4. Run the targeted test and confirm it passes (green).
  - If the test still fails, iterate: make the next minimal test-local change, run the targeted test again, and repeat until the test is green.

5. Produce a structured JSON output (schema below) so the Refactor step can consume the results.

## Constraints and best practices
- Do the absolute minimum to make the test pass.
- If the test depends on classes or methods that should normally live in production code, prefer to define stubs or test-only implementations inside the test file.
- Keep added helpers readable and clearly named.
- Respect the project's coding style (see `docs/agents/instructions/kotlin-coding-guidelines.md`).

Note: If you determine a production-file change is absolutely unavoidable, explicitly document the exact reason in the JSON `notes` field and avoid making that production change in the Green step unless the user asked for it.

## Application layer specifics

When the target module is the application layer (`application/`), the Green step may use application-layer frameworks and test utilities to make the test pass. Examples of allowed tools: Spring Boot test support (ApplicationContext, `ApplicationContextInitializer`), `MockMvc`, `TestRestTemplate`, `RestAssured`, `WireMock`, or `Testcontainers`.

Rules for application-layer Green steps:
- Use these frameworks only from test sources (`src/test/...`) or test-only fixtures/configuration. Do NOT create or modify production files under `src/main/kotlin` to satisfy tests.
- Implement the strict minimum inside tests: add test-local stubs, spies, fixtures, or test-only beans. Avoid implementing production business logic in production modules during this step.
- Any refactoring that moves test scaffolding into production code belongs to the Refactor step; document such needs in the JSON `notes` field if unavoidable.

### Compilation gate (application layer)

Before reporting the Green step as complete, run the module compilation to confirm nothing is broken at the source level:

```
./gradlew :application:compileKotlin :application:compileTestKotlin
```

If compilation fails, fix the compilation error before reporting `"status": "green"`. Do NOT mark the step green on a test run alone when the module does not compile cleanly. Include the compilation result in the `run` field or add it as a second entry if the test run command is separate.

## Expected input
The input must include at minimum:
- `testFilePath`: workspace-relative path to the test file (e.g. `domain/src/test/kotlin/com/example/UseCaseTest.kt`).
- `testMethod`: the test method name (e.g. `shouldDoX_whenY`) or the Kotlin backtick string if the test uses backtick notation.

Example input:
```
testFilePath: domain/src/test/kotlin/com/example/ContactExportUseCaseTest.kt
testMethod: `should produce export dto when contacts exist`
```

## Expected output (JSON schema)
The final output must be a valid JSON object containing the following fields.

Example of a strictly valid JSON object (no comments inside the JSON block):

```json
{
  "status": "green",
  "testFile": "application/src/test/kotlin/com/example/OrderServiceTest.kt",
  "testMethod": "shouldCreateOrderWithStatusEnAttente_whenOrderingSingleAvailableArticle",
  "changes": [
    {
      "file": "application/src/test/kotlin/com/example/OrderServiceTest.kt",
      "summary": "Added an InMemoryArticleRepository stub as an inner class to satisfy the test",
      "linesAdded": 42,
      "locationHint": "After OrderServiceTest class, before file end"
    }
  ],
  "run": {
    "command": "./gradlew :application:test --tests 'com.example.OrderServiceTest.shouldCreateOrderWithStatusEnAttente_whenOrderingSingleAvailableArticle'",
    "result": "1 tests passed"
  },
  "notes": "No production code changes required"
}
```

Notes on the `run` field:
- `run.command`: a single-line shell command string that was used to run the targeted test. Keep it executable as-is; prefer using single quotes inside the command when quoting test filters to avoid JSON escaping.
- `run.result`: a short human-readable summary (for example: "1 tests passed"). If you need to include more machine-friendly information, add optional fields such as `exitCode` (number) and `rawOutput` (string) alongside `command` and `result`.
- IMPORTANT: Do not include JavaScript/JSON-style comments (// or /* */) inside the JSON code block; comments make the block invalid JSON.

Use this JSON to pass information to the Refactor step.

## Failure rules
- If the test still fails after modifications, return a JSON with `"status": "failed"` and include `notes` explaining why.

## Final notes
This prompt is tailored for the Green step of a controlled TDD workflow: make a failing test pass by adding the minimal code necessary, ideally confined to the test file. The produced JSON will guide the Refactor step about what changed and where to look.
