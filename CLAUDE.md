# CLAUDE.md — Claude Code entry point

> This is the **priority instruction file for Claude Code**. It imports the shared,
> tool-agnostic conventions from `AGENTS.md` and adds the Claude-Code-specific tooling
> (slash commands, skills, MCP). The GitHub Copilot setup (`.github/`, `.vscode/`)
> remains in place and untouched.

## Shared project conventions

@AGENTS.md

The full responsibilities, **context markers** (always start replies with the right
emoji, default 🍀), **Active Partner rules**, architecture, and development guidelines
are defined in `AGENTS.md` above. They apply identically when working with Claude Code.

---

## Architecture (summary)

Hexagonal architecture (Ports & Adapters), Kotlin + Gradle multi-module:

```
application  ──►  domain  ◄──  infrastructure
```

- **domain** — pure business layer, zero infrastructure dependency.
- **infrastructure** — SPI adapters (persistence, external services) implementing domain ports.
- **application** — REST controllers, DTOs, Spring Boot wiring.

**Closed rule**: no infrastructure dependency may ever leak into `domain`.

---

## Claude Code tooling

### Slash commands (`.claude/commands/`)

TDD workflow, one command per phase. Run them in order on a single scenario:

| Command | Phase | Argument |
|---|---|---|
| `/tdd-red` | 🔴 Write a failing test | scenario description or issue reference |
| `/tdd-green` | 🟢 Minimal code to pass | `<testFilePath> <testMethod>` |
| `/tdd-refactor` | ⚪ Extract/clean, keep green | JSON output from `/tdd-green` |

### Skills (`.claude/skills/`)

| Skill | Purpose |
|---|---|
| `create-issue` | Generate structured, testable issue files (per impacted module) with Gherkin scenarios under `docs/features/{feature}/`. |

### MCP servers (`.mcp.json`)

| Server | Use |
|---|---|
| `context7` | Up-to-date library/framework documentation lookup. |
| `sequential-thinking` | Structured step-by-step reasoning for complex problems. |
| `serena` | IDE-assistant code navigation/editing over the project. |

> Note: `.mcp.json` is the Claude equivalent of the Copilot `.vscode/mcp.json`.
> `serena` is launched with `--project .` (project root) instead of VS Code's `${workspaceFolder}`.

---

## Instruction files — read when applicable

| Context | File |
|---|---|
| All development tasks | `docs/agents/instructions/kotlin-coding-guidelines.md` |
| Writing or reviewing tests | `docs/agents/instructions/testing-guidelines.md` |
| Maintaining agent guidelines | `docs/agents/instructions/agents-md-maintenance.md` |
| Authoring Claude commands/skills | `docs/agents/instructions/claude-authoring-guidelines.md` |
| Authoring Copilot prompts | `docs/agents/instructions/prompt.instructions.md` |

---

## Validation commands

- Full backend test suite: `./gradlew test`
- Per module: `./gradlew :domain:test`, `:application:test`, `:infrastructure:test`
- Single test: `./gradlew :<module>:test --tests '<fqn.TestClass.testMethod>'`

---

## TDD discipline (mandatory)

For every backend feature or fix: **🔴 Red → 🟢 Green → ⚪ Refactor**, never skipped.
Layer order: `domain` → `infrastructure` → `application`. The refactor phase always
includes an explicit SOLID / design-pattern / duplication / naming report — a silent
skip is a defect. Full suite must be green before a task is considered complete.
