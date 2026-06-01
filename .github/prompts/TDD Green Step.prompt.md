---
agent: agent
name: TDD Green step
description: This prompt implements the "Green" step of the TDD cycle: make a previously failing test pass by adding the minimal required code, preferably inside the test file itself.
argument-hint: Make the specified test pass (provide the test file path and the test method name). Implement only the minimal code required inside the test file.
tools: ['execute/getTerminalOutput', 'execute/runInTerminal', 'read/readFile', 'read/problems', 'edit/editFiles', 'edit/createFile', 'search', 'todo']
model: GPT-5 mini (copilot)
---

# Green TDD step prompt

## Goal
Implement the minimal code necessary to make a test written during the Red step pass. Implementation should be added in the test file itself (for example helper classes, test doubles, stubs, or internal helper functions), without modifying the targeted test body or other tests.

Important: iterate as needed — after each minimal change run the targeted test and continue making only minimal, test-local edits (within the prompt constraints) until the test is green.

## Instructions
1. Parse the provided input: it must include the test file path (workspace-relative) and the test method name to make pass.
2. Confirm the test file and method exist and that the test currently fails (if possible, run only the targeted test).
3. Implement only the code strictly required for the targeted test to succeed. Mandatory rules:
   - Add code inside the test file itself (e.g., inner classes, companion objects, stubs, helpers defined in the test file).
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
- Respect the project's coding style.

## Expected input
The prompt input must include at minimum:
- `testFilePath`: workspace-relative path to the test file (e.g. `domain/src/test/kotlin/com/example/UseCaseTest.kt`).
- `testMethod`: the test method name (e.g. `shouldDoX_whenY`) or the Kotlin backtick string if the test uses backtick notation.

Example input:
```
testFilePath: domain/src/test/kotlin/com/example/ContactExportUseCaseTest.kt
testMethod: `should produce export dto when contacts exist`
```

## Expected output (JSON schema)
The final output must be a valid JSON object containing the following fields:

``json
{
  "status": "green",                   // "green" if the test passes
  "testFile": "<relative path>",      // modified test file
  "testMethod": "<method name>",
  "changes": [                           // list of performed changes
    {
      "file": "<relative path>",
      "summary": "<short description>",
      "linesAdded": <number>,
      "locationHint": "<eg: class TestName, after imports>"
    }
  ],
  "run": {
    "command": "<command used to run the test>",
    "result": "<summary output — e.g. 1 passed>"
  },
  "notes": "<optional notes if a production change was required>"
}
```

Use this JSON to pass information to the Refactor step.

## Examples (Input → Output)

Example 1 — input:
```
testFilePath: application/src/test/kotlin/com/example/OrderServiceTest.kt
testMethod: shouldCreateOrderWithStatusEnAttente_whenOrderingSingleAvailableArticle
```

Expected action:
- Add a stub or helper `class InMemoryArticleRepository` inside `OrderServiceTest.kt` (for example as an inner class or companion object) without changing the test method body.
- Run the targeted test and confirm it passes.

Example 1 — sample output JSON:
``json
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

## Failure rules
- If the test still fails after modifications, return a JSON with `"status": "failed"` and include `notes` explaining why.

## Final notes
This prompt is tailored for the Green step of a controlled TDD workflow: make a failing test pass by adding the minimal code necessary, ideally confined to the test file. The produced JSON will guide the Refactor step about what changed and where to look.
