---
agent: agent
name: TDD Red step
description: This prompt is used to implement one test scenario that fails in a TDD workflow for an AI agent
argument-hint: Implement the following test scenario in a TDD workflow for an AI agent: {scenario_description}
tools: ['execute/getTerminalOutput', 'execute/runInTerminal', 'read/problems', 'read/readFile', 'read/terminalSelection', 'read/terminalLastCommand', 'edit/createDirectory', 'edit/createFile', 'edit/editFiles', 'search', 'upstash/context7/*', 'todo']
model: GPT-5 mini (copilot)
---


# Red TDD step prompt

## Instructions
1. Analyze the provided scenario description carefully.
    - If the scenario description is provided as an issue reference, retrieve the issue content and extract the specified scenario.
    - If the scenario description is provided directly, use it as is.
2. Check if a test file already exists for the scope of this test scenario. 
   - If it exists, append the new test case to the existing file.
   - If it does not exist, create a new test file in the appropriate directory structure based on the module (domain, application, infrastructure). 
3. Write the test case so it accurately reflects the scenario and is expected to fail initially. You MUST Follow the testing guidelines for the module you are currently working on : 
    - for the domain module, follow the guidelines in `docs/agents/instructions/domain-testing.instructions.md`
    - for the application module, follow the guidelines in `docs/agents/instructions/application-testing.instructions.md`
    - for the infrastructure module, follow the guidelines in `docs/agents/instructions/infrastructure-testing.instructions.md`
    - Also consult the project agent conventions in `AGENTS.md` (root) and the agent maintenance guide at `docs/agents/instructions/agents-md-maintenance.md` for any agent-specific behaviour or naming rules.
4. Run the test to confirm it fails.

## Requirements
- You **MUST** follow the guidelines for the module you are currently working on.
- **NEVER** implement any production code in this step. Your ONLY goal is to write a failing test.
- You **MUST** ensure the test fails when executed. 
- The name of the test method should be descriptive and follow the naming conventions outlined in the testing guidelines.

## Test naming guidance (examples)

- Naming convention to use: `should<ExpectedBehavior>_when<Context>` (camel case, no spaces) or the Kotlin backtick style with a readable sentence.

- Correct examples:
    - `shouldCreateOrderWithStatusEnAttente_whenOrderingSingleAvailableArticle`
    - `shouldRefuseOrder_whenAtLeastOneArticleOutOfStock`
    - `shouldProduceExportDto_whenContactsExist`

- Incorrect examples (avoid these):
    - `testOrder`  # too vague
    - `should_pass`  # underscores and generic
    - `orderTest1`  # numeric suffix, not descriptive
    - `shouldCreateOrder()`  # parentheses in name, not following convention

When writing Kotlin tests you may also use backtick notation for a readable sentence, for example:
``fun `should create order with status EN_ATTENTE when ordering a single available article`() { ... }``

Explicitly check the repository files `AGENTS.md` (project root) and `docs/agents/instructions/agents-md-maintenance.md` for additional conventions the agent must respect when generating prompts and tests.

## Examples

### Domain test example : file does not yet exist

Input : 
Scenario Description: Scenario: Successfully export contacts
Given a user with 20 contacts
When executing a query to fetch contacts
Then the system retrieves all 20 contacts and generates an export DTO 

Expected Output : 

- a new file `domain/src/test/java/com/example/domain/contact/ContactExportUseCaseTest.java` is created with the following content : 

```kotlin
package com.example.domain.contact

import com.example.domain.test.fixture.ContactExportFixture
import com.example.domain.test.state.TestState
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ContactExportUseCaseTest {

    private lateinit var fixture: ContactExportFixture

    @BeforeEach
    fun setUp() {
        fixture = ContactExportFixture()
    }

    @Test
    fun `should produce export dto when contacts exist`() {
        // Given a user with 20 contacts
        val userTestState: TestState<User> = fixture.getUserTestState()
        val contactTestState: TestState<Contact> = fixture.getContactTestState()
        val handler: UseCaseHandler<ExportContactQuery, ContactExportDto> = fixture.getUseCaseHandler()

        val user = User("user1")
        userTestState.add(user)
        val contacts = (0 until 20).map { i ->
            Contact("Contact $i", "contact$i@example.com")
        }
        contacts.forEach(contactTestState::add)

        // When executing a query to fetch contacts
        val query = ExportContactQuery(user.id)
        val exportDto = handler.execute(query)

        // Then the system retrieves all 20 contacts and generates an export DTO
        assertThat(exportDto).isNotNull()
        assertThat(exportDto.contacts).hasSize(20)
    }
}
```