## Description

When selecting a lose condition type that corresponds to a record with no constructor parameters (e.g., LivesBasedConditionRecord), the createSelectedLoseCondition() method fails to instantiate it, resulting in a ViewException or silent fallback behavior.

## Expected Behavior

The method should be able to instantiate a lose condition class that does not require any constructor parameters, such as LivesBasedConditionRecord, and return the corresponding LoseConditionInterface instance.

## Current Behavior

If the selected lose condition has no constructor that accepts an int or String, the method fails to create an instance. In the case of LivesBasedConditionRecord, which has only a no-arg constructor, the fallback logic doesn't account for this constructor type.

## Steps to Reproduce

1. Ensure that getSelectedLoseCondition() returns "LivesBasedConditionRecord".
2. Ensure that the class LivesBasedConditionRecord exists and implements LoseConditionInterface.
3. Call createSelectedLoseCondition().
4. Observe that the method fails with an exception or returns null.

## Failure Logs

java.lang.NoSuchMethodException: oogasalad.engine.records.config.model.losecondition.LivesBasedConditionRecord.<init>(int)

or

java.lang.NoSuchMethodException: oogasalad.engine.records.config.model.losecondition.LivesBasedConditionRecord.<init>(java.lang.String)

followed by:

java.lang.NoSuchMethodException: oogasalad.engine.records.config.model.losecondition.LivesBasedConditionRecord.<init>()

(if unhandled)

## Hypothesis for Fixing the Bug

Add a third fallback in the createSelectedLoseCondition() method to check for a no-argument constructor using:

Constructor<?> noArgConstructor = clazz.getConstructor();
return (LoseConditionInterface) noArgConstructor.newInstance();

Test: The bug is fixed if createSelectedLoseCondition() correctly returns an instance of LivesBasedConditionRecord when selected, without throwing any exceptions.
