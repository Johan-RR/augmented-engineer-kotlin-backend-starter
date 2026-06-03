---
description: Orchestrate a complete TDD cycle (Red → Green → Refactor) for a given scenario, invoking each phase as a dedicated subagent.
argument-hint: <scenario description or issue reference>
allowed-tools: Agent
---

# TDD Cycle Orchestrator

Scenario / feature: $ARGUMENTS

## Instructions

1. **Gather context.**
   - If `$ARGUMENTS` is an issue reference, locate and read the issue file under `docs/features/` and extract the targeted scenario.
   - If it is a free-form description or Gherkin block, use it as-is.
   - If no argument is provided, ask the user for the scenario before proceeding.

2. **Red step** — Spawn the `tdd-red` subagent with the following prompt:

   ```
   Scenario to implement: <scenario description>
   ```

   Wait for it to complete. Present the JSON output block to the user and ask for confirmation before proceeding to the next step.

3. **Green step** — Once the Red step JSON is confirmed, spawn the `tdd-green` subagent with the following prompt:

   ```
   <JSON output from tdd-red, verbatim>
   ```

   Wait for it to complete. Present the JSON output block to the user and ask for confirmation before proceeding.

4. **Refactor step** — Once the Green step JSON is confirmed, spawn the `tdd-refactor` subagent with the following prompt:

   ```
   <JSON output from tdd-green, verbatim>
   ```

   Wait for it to complete.

5. **Cycle summary** — After the Refactor step completes, present a structured summary:

   - **🔴 Red** — test file + method name added
   - **🟢 Green** — minimal scaffolding added (test-local stubs / helpers)
   - **⚪ Refactor** — production code extracted, SOLID/pattern report

   Then relay the `next_pass_proposal` from the Refactor JSON output verbatim to the user.
   If `next_pass_proposal` is non-empty, present it as a numbered list and ask:

   > "Souhaites-tu qu'une nouvelle passe de refactoring applique ces améliorations, ou préfères-tu démarrer un nouveau cycle TDD ?"

   - **Option (a) — nouvelle passe refacto :** Spawn `tdd-refactor` again, passing the current JSON output and the confirmed `next_pass_proposal` items as explicit tasks.
   - **Option (b) — nouveau cycle TDD :** Restart from step 1 with the new scenario provided by the user.

   If `next_pass_proposal` is empty, simply ask:

   > "Le code est propre. Souhaites-tu démarrer un nouveau cycle TDD ?"

## Rules

- **Never skip the user confirmation** between Red and Green, and between Green and Refactor.
- **Pass JSON verbatim** between steps — do not paraphrase or summarise the structured output.
- **Do not merge steps** — Red, Green, and Refactor are always separate subagent invocations.
- Each subagent accesses the codebase directly via its own tools; you do not need to pass file lists.
- Project constraints (architecture, coding style, testing rules) are embedded in `CLAUDE.md` and `AGENTS.md`, which each subagent reads independently.
