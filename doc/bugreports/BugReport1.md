## Description

Summary of the feature's bug (without describing implementation details): The test suite for the
language manager used hardcoded assumptions for the available languages to only be English and
Italian, which failed when additional languages were added.

## Expected Behavior

Describe the behavior you expect: The test should ensure that the available languages getter method
returns all available languages found, rather than the assumption that only two hardcoded languages
will be present in the program.

## Current Behavior

Describe the actual behavior: The test checked for only "English" and "Italian" as the supported
languages returned by a getter method.

## Steps to Reproduce

Provide detailed steps for reproducing the issue.

1. Run the JUnit test for the LanguageManagerTest class.

## Failure Logs

Include any relevant print/LOG statements, error messages, or stack traces.: An assertion failure
message is provided when the test is run.

## Hypothesis for Fixing the Bug

Describe the test you think will verify this bug and the code fix you believe will solve this issue.

See the below fix to the bug:

```java
Assertions.assertEquals(
    FileUtility.getFileNamesInDirectory("src/main/resources/oogasalad/languages",
".properties"),LanguageManager.

getAvailableLanguages());
```

Now the test checks for all language names from the path provided to be returned instead of a
hardcoded: List.of("English", "Italian");
