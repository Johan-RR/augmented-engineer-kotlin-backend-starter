# Claude Code Authoring Guidelines (Commands & Skills)

Guidelines for authoring **Claude Code** slash commands (`.claude/commands/`) and
skills (`.claude/skills/`). This is the Claude-Code counterpart of the Copilot-oriented
`prompt.instructions.md`; both are kept so the repository supports both tools.

## Scope and principles
- Audience: maintainers authoring reusable Claude Code commands and skills.
- Goals: predictable behaviour, clear expectations, least-privilege tools, portability.
- Keep the *body* of a command/skill tool-agnostic where possible, so the same logic
  can be mirrored in the Copilot `.github/prompts` files.

---

## Slash commands — `.claude/commands/<name>.md`

A command file is a Markdown prompt with optional YAML frontmatter. The filename
(kebab-case) becomes the command name, e.g. `tdd-red.md` → `/tdd-red`.

### Frontmatter fields

| Field | Required | Description |
|---|---|---|
| `description` | Recommended | Single actionable sentence shown in the command list. |
| `argument-hint` | Recommended | Hint shown after the command name to guide input. |
| `allowed-tools` | Optional | Least-privilege list of tools the command may use. |
| `model` | Optional | Override the model for this command. Omit to inherit. |

> Do **not** use Copilot-only fields (`agent`, `model: GPT-5 mini (copilot)`,
> `tools: ['execute/runInTerminal', ...]`). They are ignored by Claude Code.

### Arguments
- Use `$ARGUMENTS` for the full argument string.
- Use `$1`, `$2`, … for positional arguments.
- Do **not** use Copilot's `${input:variableName}` syntax.

### Tool mapping (Copilot → Claude)

| Copilot tool | Claude tool(s) |
|---|---|
| `execute/runInTerminal`, `execute/getTerminalOutput` | `Bash` |
| `read/readFile` | `Read` |
| `edit/createFile`, `edit/createDirectory`, `edit/editFiles` | `Write`, `Edit` |
| `search` | `Grep`, `Glob` |
| `read/problems` | (no direct equivalent — omit) |
| `todo` | (Claude manages todos natively — omit) |
| `upstash/context7/*` | `mcp__context7__*` |

### Body structure
- Start with an `#` heading matching the command intent.
- Recommended sections: purpose/goal, instructions (numbered), constraints,
  examples, expected output.
- Reference instruction files with workspace-relative paths
  (e.g. `docs/agents/instructions/testing-guidelines.md`).

---

## Skills — `.claude/skills/<name>/SKILL.md`

A skill is a folder containing `SKILL.md` plus optional supporting files
(templates, scripts, references).

### Frontmatter fields

| Field | Required | Description |
|---|---|---|
| `name` | ✅ | Skill identifier (kebab-case, matches the folder name). |
| `description` | ✅ | When to use the skill (drives automatic discovery). Be specific. |
| `allowed-tools` | Optional | Least-privilege tool list. |

### Supporting files
- Reference supporting files **relative to the skill folder**, e.g. `issue.md`,
  `validate_issue_format.py` — not `templates/issue.md` or `scripts/...` unless those
  subfolders actually exist.
- Scripts run via `Bash` (e.g. `python .claude/skills/<name>/validate_issue_format.py <args>`).

---

## Quality checklist
- [ ] Frontmatter complete, accurate, least-privilege.
- [ ] Arguments use `$ARGUMENTS` / `$1` (not `${input:...}`).
- [ ] Tool names are Claude tools (not Copilot tool ids).
- [ ] All referenced files/paths actually exist.
- [ ] Output format and validation steps are explicit.
- [ ] Command discovered via `/` and skill discovered by its `description`.

## Keeping Copilot and Claude in sync
When a TDD prompt or skill changes, update **both** the Copilot file
(`.github/prompts/*` or `.github/skills/*`) and the Claude file
(`.claude/commands/*` or `.claude/skills/*`) so the two toolchains stay aligned.
