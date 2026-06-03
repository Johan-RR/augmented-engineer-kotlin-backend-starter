---
description: Move production code out of tests and clean it up while keeping tests green (Refactor step).
argument-hint: <JSON output from the /tdd-green step>
allowed-tools: Bash, Read, Write, Edit, Grep, Glob
---

# TDD Refactor step

Input (JSON from the Green step): $ARGUMENTS

## Objective

Take the JSON output produced by the "Green" step and perform the minimal iterative refactorings needed to:
- Extract production code that was accidentally placed inside the test file into appropriate production classes/objects;
- Clean the code (remove duplications, rename variables/methods for clarity, improve responsibility separation);
- Align with the team's conventions and coding best practices (see `docs/agents/instructions/kotlin-coding-guidelines.md`);
- Preserve the observable behaviour covered by tests;
- Keep all tests green throughout the process.

Important clarification: The Refactor step is the place to move test-local scaffolding into proper production code and to implement the production behaviour required by the targeted feature. Refactor work MUST create or modify production sources under `src/main/kotlin/...` (in the proper module: `domain`, `application`, or `infrastructure`) when extracting code from tests. Do not perform those production extractions during the Green step.

## Expected input

The input must be the exact JSON output from the "TDD Green step". Minimal expected schema (excerpt):

{
  "status": "green",
  "testFile": "<relative path>",
  "testMethod": "<method name>",
  "changes": [ ... ],
  "run": { "command": "<command>", "result": "<summary>" },
  "notes": "..."
}

The agent must validate that `status` is `green` before starting.

## Key rules

- Work in micro-steps: perform exactly one single modification per iteration. Each micro-step must contain one, and only one, logical change (for example: extract ONE class, move ONE constant, or rename ONE method).
- Do NOT combine multiple logical changes in the same micro-step — e.g., do not move several classes at once, do not extract multiple helpers in one commit, and do not rename and extract in the same step.
- After each micro-step, run only the necessary tests (the targeted test and, if needed, a wider subset) and verify they remain green.
- Do not change behaviour: test assertions must remain valid and test results must not change.
- Prefer creating production classes in the correct module (`domain`, `application`, or `infrastructure`) under `src/main/kotlin/...`.
  - Focus scope: When implementing production code, limit changes to the single relevant layer (domain, application, or infrastructure). Do not alter unrelated layers or broaden scope. If production code needs collaborators in other layers, provide minimal interfaces or use fakes/stubs to satisfy those dependencies rather than modifying other modules.
- If a production code change is absolutely required and justified, explain it in the final `notes` field.
- Keep changes small and readable; follow the repository's Kotlin style conventions. Avoid large-scale rewrites.
- Always report which files were modified, a short summary, and the number of lines added/removed.

## Guardrails: avoid scope creep

- Do not drift into improving unrelated code or pursuing broad "quality improvements" that are outside the narrow goal of extracting test-embedded production code. If a desirable improvement is discovered, note it in `notes` and postpone actual changes until after the refactor micro-steps are complete (or create a separate issue).
- Follow the strict micro-step discipline: MODIFY -> TEST -> VALIDATE -> CONTINUE. That means:
  1. MODIFY: Make a single, well-scoped code change. This means only one modification per micro-step (see Key rules). For example, extract exactly one class — do not extract multiple classes in the same step.
 2. TEST: Run the minimal set of tests needed (targeted test or small subset).
 3. VALIDATE: Confirm tests remain green and the behavior is unchanged. If failures occur, revert or fix immediately to restore green before proceeding.
 4. CONTINUE: Only then propose or apply the next micro-step.

Breaching this discipline (for example making multiple unrelated changes in one step) is not allowed.

## Recommended process (micro-steps)

1. Inspect the `testFile` referenced and locate production code embedded in the test.
2. Propose and apply the first micro-step (for example, extract a small class or interface) by creating the necessary production files.
  - When creating production files, place them under the appropriate module's `src/main/kotlin/...` path and choose the package that matches the project's conventions. Implement just enough production behaviour to preserve the tests' assertions; if external collaborators are required, depend on interfaces and provide test-only fakes where appropriate.
3. Run the targeted test(s) — use the provided command or an adapted command such as `./gradlew :<module>:test --tests '<fqn.TestClass.testName>'`.
4. If tests pass, mark the step complete and propose the next micro-step.
5. Repeat until all production code has been moved out of tests and the codebase is clean.

## Constraints

- Minimize modifications: prefer local extractions and small helper classes rather than global refactorings.
- Do not change test bodies (except imports if necessary for compilation) and do not delete tests.
- Every change must be accompanied by a test run proving nothing broke.

Additional constraint: Avoid scope creep across layers. Each micro-step must keep modifications inside the target layer; do not refactor or implement cross-layer features in the same micro-step. If a cross-layer change is unavoidable, document the justification in the `notes` field and perform it as a separate, explicitly-approved micro-step.

## Application layer specifics

When extracting test-local scaffolding that concerns the application layer (`application/`), the Refactor step MUST ensure business logic is stubbed or implemented correctly and moved out of tests. In particular:
- If the Green step used test-only spies/stubs inside tests (for example a `PlaceOrderFixture` with a `PlaceOrderUseCaseSpy`), the Refactor step must either:
  - extract a reusable test fixture under `application/src/test/kotlin/...` if the behaviour is only relevant for tests, or
  - implement a minimal, well-named production stub/adapter in `application/src/main/kotlin/...` or `domain/src/main/kotlin/...` that implements the proper interface (for example `PlaceOrderUseCase`) so the behaviour is explicit and reusable.
- Do NOT leave business behaviour implemented solely inside test files. The goal of Refactor is to make the production boundary explicit and to place business logic where it belongs.
- Prefer extracting interfaces and small adapters rather than copying full test implementations into production. Keep changes minimal and follow module boundaries.

## Refactor quality checklist (mandatory, per project conventions)

After reaching green, the Refactor step MUST explicitly report on:
- SOLID principles (SRP, OCP, LSP, ISP, DIP).
- Design pattern opportunities — describe the problem, name the pattern, and **wait for confirmation** before applying.
- Duplication removal, naming and readability.
A silent skip of this report is a defect.

## Continuation proposal (mandatory self-evaluation)

After the quality checklist, you **MUST** self-evaluate whether another refactoring pass is warranted.

**Rule:** If the quality checklist reveals any actionable improvement — SOLID violation, applicable design pattern, duplication, or naming issue — you **MUST** propose a follow-up pass with a concrete, numbered list of what would be applied.

Format the proposal as:

```
## Passe de refactoring supplémentaire disponible

Les points suivants ont été identifiés et pourraient être appliqués lors d'une prochaine passe :

1. **[Pattern/Principe]** — [description courte du problème et de la solution]
2. **[Pattern/Principe]** — [description courte du problème et de la solution]
...

Souhaites-tu qu'une nouvelle passe de refactoring applique ces améliorations ?
```

If the quality checklist finds nothing actionable, state:

```
## Aucune passe supplémentaire nécessaire

Le code est conforme SOLID, aucune duplication identifiée, aucun pattern applicable à ce stade.
```

**Never skip this section.**

## Expected output (JSON)

The final result must be a valid JSON object containing at least:

{
  "status": "refactored",
  "greenTestFile": "<relative path to test (if modified)>",
  "refactorSteps": [
    {
      "file": "<file modified/added>",
      "summary": "<short description>",
      "linesAdded": <n>,
      "linesRemoved": <n>,
      "locationHint": "<where in the tree>"
    }
  ],
  "runLog": [
    { "command": "<command>", "result": "<summary result after this step>" }
  ],
  "notes": "<remarks, justification for any production changes>"
}

If the agent had to modify production code to make the refactor possible, document the precise reason in `notes`.

## Failure

If a micro-refactor turns tests red and the agent cannot immediately fix it, return:

{
  "status": "failed",
  "reason": "<short description of the blockage>",
  "runLog": [...],
  "notes": "<suggestions to resolve or reason for limitation>"
}

---

Reminder: the agent must proceed incrementally, keep tests green, and document each micro-step in the output JSON.
