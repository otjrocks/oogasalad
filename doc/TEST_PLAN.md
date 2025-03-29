### **1\. Explicit Parameters and Return Values (No Hidden State)**

We intentionally designed methods in our APIs (e.g., `EntityFactory.createEntity()`, `ConfigLoader.loadFromFile()`, `GameMap.getEntityAt()`) to:

* Take in all necessary parameters explicitly (e.g., `EntityData`, `filepath`)

* Return **concrete outputs** (e.g., `Entity`, `ConfigModel`) rather than mutating internal state

**Why this helps testing:**

* We can easily test methods in isolation by giving them controlled inputs and asserting their return values

* We avoid needing to mock complex internal state or lifecycle behavior

* Makes unit tests pure: `assertEquals(...)` on the result instead of probing side effects

### **2\. Small, Focused Classes and Methods (Single Responsibility)**

We've consistently applied the **Single Responsibility Principle** across our APIs to keep methods and classes narrowly scoped:

* `EntityFactory` only creates entities

* `CollisionStrategy` only defines what happens during a collision

* `ConfigLoader` only reads from files, while `ConfigSaver` writes to them

**Why this helps testing:**

* Each class can be tested independently using a small set of inputs and mocks

* Strategy interfaces can be tested in isolation (e.g., test `LoseLifeStrategy` with a mock `GameState`)

* We don’t need to simulate the full game loop or file system just to test one method

# ConfigSaver

**Test 1**: Save a Valid Config File (Happy Path)

* The user finishes building a game in the Authoring Environment and clicks “Save.” A valid ConfigModel is generated and passed to ConfigSaver.

Steps:

1. Create a minimal ConfigModel with valid metadata, settings, and one entity.  
2. Call saveToFile(config, "test\_output.json").

Expected Outcome:

* A file test\_output.json is created on disk.  
* The file content matches the serialized form of the model (can be checked with a file read \+ parse).  
* No exceptions are thrown.

How Your Design Supports It:

* ConfigSaver has a clear, simple contract: input is a complete ConfigModel, output is a file.

**Test 2:** Save to an Invalid File Path (Sad Path)  
Scenario:

* The user tries to save the file to a directory that doesn’t exist or is inaccessible.

Steps:

1. Create a valid ConfigModel.  
2. Call saveToFile(config, "/invalid/path/output.json").

Expected Outcome:

* A java.io.IOException is thrown.  
* The message explains why the file couldn’t be written.

How Your Design Supports It:

* IOException is clearly declared in the method signature.  
* Makes it easy to write unit tests that catch this and verify robust error handling.

**Test 3**: Save Config With Missing Fields (Negative Structural Input)  
Scenario:

* A user creates a level but forgets to assign a controlType to an entity. The model is technically valid but semantically incomplete.

Steps:

1. Construct a ConfigModel where an EntityData has a null controlType.   
2. Call saveToFile().

Expected Outcome:

* A file is saved with nulls (if schema allows).

How Your Design Supports It:

* You can layer optional pre-validation in the ConfigSaver (or a separate validator).

* This test ensures future-proofing when schemas become stricter.

**Test 4:** Check Supported File Types (Happy \+ Extension Safety)  
Scenario:

* A UI dropdown queries supported file types before saving a level.

Steps:

1. Call getSupportedFileExtensions() on your ConfigSaver instance.

Expected Outcome:

* Returns a list containing \[".json"\] (or more if you support others later).  
* Can be checked in unit tests to ensure consistent output.

How Your Design Supports It:

* The API includes this method for plug-and-play usage by the frontend or file pickers.  
* Lets developers restrict save dialogs to allowed formats.

# GameState:

**Test 1:** Save GameState Successfully Scenario(Happy Path): The user triggers a save operation from the pause menu. 

**Steps:**

1. Create a GameState object with a valid score, level, entity positions, and HUD elements.  
2. Call GameState.saveState("savefile.json").

**Expected Outcome:**

* A file named "savefile.json" is created.  
* The file content matches the serialized GameState.  
* The method returns true, indicating success.

**How Design Supports Testing:**

* The GameState API provides a clear saveState() method that returns a boolean value, allowing straightforward assertions in tests.

**Test 2:** Load Non-Existent Save File Scenario(Sad Path): The user attempts to load a game state from a file that doesn’t exist.

 **Steps:**

1. Call GameState.loadState("nonexistent\_file.json").

**Expected Outcome:**

* The method returns false or throws a FileNotFoundException.  
* An error message is displayed to the user.

**How Design Supports Testing:**

* The GameState API clearly distinguishes between successful and failed load operations, allowing for robust error handling and testing.

**Test 3:** Update Score Scenario (Happy Path): The player earns points during gameplay.

 **Steps:**

1. Call GameState.updateScore(100).

**Expected Outcome:**

* The score is incremented by 100 points.  
* The current score is displayed correctly on the HUD**.**

**How Design Supports Testing:**

* The updateScore() method provides direct feedback through return values or updated GameState attributes, making assertions straightforward.

**Test 4:** Attempt to Save with Incomplete Data Scenario (Sad Path): A GameState object is missing essential components, such as entity positions or score data. 

**Steps:**

1. Create a GameState object with missing or null data fields.  
2. Call GameState.saveState("corrupt\_save.json").

**Expected Outcome:**

* The method returns false or throws a ValidationException.  
* An error message or warning is returned to the user.

**How Design Supports Testing:**

* The API's saveState() method includes validation checks that ensure all necessary data is present before writing to a file.

# SaveEntity

**Positive Scenarios**

**Test \#1: Successful Entity Save**

- Scenario: save a fully configured valid entity.  
- Steps:  
  - Create a complete, valid saved entity  
  - Call saveEntity()  
- Expected outcome:  
  - Returns the unique identifier  
  - The entity successfully persists

**Test \#2: Successful Entity Retrieval**

- Scenario: load a previously saved entity.  
- Steps:  
  - Save an entity  
  - Retrieve the entity using the identifier  
- Expected outcome:  
  - Retrieved entity matches the original  
  - All properties remain unchanged

**Negative Scenarios**

**Test \#3: Save Invalid Entity**

- Scenario: attempt to save an entity that is missing some of the required properties.  
- Steps:  
  - Create entity with incomplete configuration  
  - Call saveEntity()  
- Expected outcome:  
  - EntityValidationException thrown  
  - No storage occurs  
  - A detailed error message pops up

**Test \#4: Storage Capacity Limit**

- Scenario: exceed the storage capacity when trying to save multiple entities.  
- Steps:  
  - Fill storage to near capacity   
  - Attempt to save additional entities  
- Expected outcome:  
  - StorageCapacityException thrown  
  - Partial saves rolled back  
  - A capacity information message pops up 

# EntityFactory

**Test 1**:   
Scenario: Successfully return an entity when all required fields in entity data are provided and valid  
Steps:

1. Create a mock EntityData instance setting valid values for all fields  
2. Call createEntity(entityData) using an instance of EntityFactory  
3. Verify returned entity matches properties defined in entity data

Expected Outcome:

* Entity created successfully with values set in EntityData

How Your Design Supports It:

* The \`createEntity()\` method uses \`EntityData\` to construct an entity.  
*  It extracts values from \`EntityData\` and initializes the entity accordingly.

**Test 2:**   
Scenario:

* Create an entity where a field is null 

Steps:

1. Create a mock EntityData instance setting valid values for all fields  
   1. Set type to null  
2. Call createEntity(entityData) using an instance of EntityFactory  
3. Verify EntityCreationException is thrown

Expected Outcome:

* \`EntityCreationException\` is thrown, indicating that a required field is missing.

How Your Design Supports It:

* The \`createEntity()\` method validates required fields before entity creation.  
* It throws an \`EntityCreationException\` if critical data is missing.


**Test 3**:   
Scenario:

* Attempt to create an entity with an invalid image path, leading to an \`EntityCreationException\`.

Steps:

1. Create a mock EntityData instance setting valid values for all fields  
   1. Set image path to a path, just one without an image  
2. Call createEntity(entityData) using an instance of EntityFactory  
3. Verify EntityCreationException is thrown

Expected Outcome:

* \`EntityCreationException\` is thrown, indicating that the image path is invalid or cannot be loaded.

How Your Design Supports It:

*  The \`createEntity()\` method verifies that the image path is valid and throws an exception if it is not.

**Test 4:**  
Scenario:

* Attempt to create an entity with an unsupported control type (but non null), leading to an \`EntityCreationException\`.

Steps:

1. Create a mock EntityData instance setting valid values for all fields  
   1. Set control to just a bunch of random letters  
2. Call createEntity(entityData) using an instance of EntityFactory  
3. Verify EntityCreationException is thrown

Expected Outcome:

* \`EntityCreationException\` is thrown, indicating that the \`controlType\` is not recognized or supported.

How Your Design Supports It:

* \`createEntity()\` validates the \`controlType\` field and ensures that it only allows predefined control types (e.g., "keyboard", "AI").  
* An unsupported control type triggers an \`EntityCreationException\` to prevent creating entities with invalid behavior.

# GameMap

**Test 1**:   
Scenario: Test for the successful creation of a game map from a configuration file.  
Steps:

1. Use the FileHandler and JSONFileHandler to read a JSON configuration file.  
2. While reading, create a concrete instance of a GameMap and call addEntity() method for each entity in the file.  
3. Write assert statements to ensure that each entity was added correctly.

Expected Outcome:

* The expected outcome is a successfully created map. The test can use the map iterator to ensure all the entities were properly added.

How Your Design Supports It:

* The design provides a simple way to add entities that are created from a file.   
* The class will throw errors in any instance when the game map is not created correctly.  
* The iterator() method allows you to quickly and easily iterate through all the entities on the map.

**Test 2:**   
Scenario: Attempt to add an Entity outside of bounds of the map  
Steps:

1. Create a new Entity object which has an x and y coordinate outside of the map area.  
2. Attempt to call addEntity() with your entity that is out of bounds for the specific implementation of the game map.  
3. Assert that the addEntity call results in an InvalidPositionException being thrown.

Expected Outcome:

* An exception called InvalidPositionException is thrown, indicating that the Entity cannot be added to the game map.

How Your Design Supports It:

* Our design uses custom Exceptions to indicate to the user when method operations fail.   
* The interface does not define how to handle exceptions but allows concrete implementations of the game map to determine what is considered invalid positions for a map.

**Test 3 and 3.5**:   
Scenario: Check successful and unsuccessful removal of an Entity  
Steps:

1. Attempt to add an addEntity() using the GameMap API.  
2. Then, remove the entity from the removeEntity() method on the same object.   
3. Ensure that the removal occurred by trying to obtain the entity.  
4. Try removing the entity again after it has already been removed and ensure that an EntityNotFoundException is thrown.

Expected Outcome:

* While the Entity is still on the map, it should be successfully removed. However, attempting to remove an Entity twice should result in an exception being thrown.

How Your Design Supports It:

* The add and removeEntity() classes are designed to encourage communication with the API caller by properly throwing informative exceptions and ensuring that the intended operation occurs.

**Test 4:**  
Scenario: Try getting an entity at a specific coordinate  
Steps:

1. Create a GameMap instance and add multiple entities to the map using the addEntity method.  
2. Call getEntityAt(int x, int y) method.  
3. Ensure that the entity returned is the correct entity for the location provided.

Expected Outcome:

* A valid Entity should be returned from the GameMap object based on the coordinate provided.

How Your Design Supports It:

* The getEntityAt(int x, int y) searches the internal data structure for a specific Entity in the map. Our design allows flexibility in the implementation of this method to allow for custom behaviors such as edge policies or rounding. For example, getEntityAt could allow for finding the closest element to a coordinate but not requiring exact precision. For example, getEntityAt(1, 1\) could return the closest entity at (1.00001, 1). The specific implementations of getEntityAt can be tested in further tests.

# SaveRules

**Test Scenario 1: Save a Valid Level Rule (Happy Path)**  
Steps:

1. Create a complete LevelRule with all valid parameters  
   * Author: "GameDesigner1"  
   * Level Name: "Mystic Forest"  
   * Description: "A challenging forest level with multiple paths"  
   * Difficulty: 7  
2. Call `saveRule()` on the RuleSaveManager 

Expected Outcome:

* A unique rule ID is returned (not null)  
* Rule is successfully stored in the system  
* No exceptions are thrown How Design Supports It:  
* `saveRule()` method in RuleSaveManager  
* BaseRule generates a UUID if no ID provided  
* Serialization method in LevelRule  
* Optional validation in LevelEditorRuleSaver

**Test Scenario 2: Attempt to Save Invalid Level Rule (Sad Path \- Validation Failure)**   
Steps:

1. Try to create a LevelRule with invalid parameters  
   * Empty level name  
   * Difficulty outside valid range (0 or 11\)  
2. Call `saveRule()` on the RuleSaveManager 

Expected Outcome:

* RuleSaveException is thrown  
* Exception contains a meaningful error message explaining the validation failure  
* No rule is saved to the system How Design Supports It:  
* Custom `RuleSaveException`  
* Validation method in LevelEditorRuleSaver  
* Checks for:  
  * Non-empty level name  
  * Difficulty within 1-10 range

**Test Scenario 3: Update an Existing Level Rule (Modification Path)**   
Steps:

1. Save an initial level rule  
2. Retrieve the rule using its ID  
3. Modify specific attributes of the rule  
   * Change author name  
   * Update level description  
   * Adjust difficulty  
4. Call `updateRule()` with the modified rule 

Expected Outcome:

* `updateRule()` returns true  
* Retrieved rule reflects all updated attributes  
* Original rule ID remains unchanged How Design Supports It:  
* `updateRule()` method in RuleSaveManager  
* Setter methods in LevelRule  
* ID preservation during updates  
* Optional loading and verification of updated rule

**Test Scenario 4: Delete a Non-Existent Rule (Error Handling Path)**   
Steps:

1. Generate a random, non-existent rule ID  
2. Call `deleteRule()` with the invalid ID 

Expected Outcome:

* `deleteRule()` returns false  
* No exception is thrown  
* No unexpected system state changes How Design Supports It:  
* `deleteRule()` returns Boolean instead of throwing exception  
* Allows for graceful handling of non-existent rules  
* Provides clear indication of deletion success/failure

# ConfigParser

**Test Scenario 1: Successfully Parse a Valid Configuration File (Happy)**  
Steps:

1. Create configuration file with a variety of content  
2. Call parseConfig on the properly formatted config file  
3. Retrieve values using the getSetting method  
4. Assert that the retrieved values match the expected ones

Expected Outcome:

- All values returned by getSetting should match those in the config file  
- No exceptions should be thrown

Test Support:

- The parseConfig method will read the file and populate a Map\<String, String\> internally  
- getSetting will allow for the verification of data

**Test Scenario 2: Retrieve a Missing Configuration Key (Edge Case)**  
Steps:

1. Create and parse a generic configuration file  
2. Call getSetting(“thisKeyDoesNotExist”)

Expected Outcome:

- getSetting should return Optional.empty()

Test Support:

- The Optional return type prevents null values and avoids NullPointerExceptions

**Test Scenario 3: Validate an Incomplete Configuration File (Sad)**  
Steps:

1. Parse a configuration file missing required keys  
2. Call validateConfig on the config file  
3. Assert that the correct exception is thrown  
   

Expected Outcome:

- validateConfig should throw an InvalidConfigException

Test Support:

- The validateConfig method checks for required keys and throws an exception if they are missing

**Test Scenario 4: Parse a Malformed Configuration File (Sad)**  
Steps:

1. Parse a malformed configuration file (missing values, wrong data type, etc.)  
2. Call validateConfig on the config file   
3. Call isValidValue on all keys in the config file  
4. Try to call getSetting on any incorrect data type  
   

Expected Outcome:

- validateConfig should throw an InvalidConfigException for any missing values  
- isValidValue should return false for any invalid/incorrect data types  
- getSetting should either return a default or throw an exception

Test Support:

- The isValidValue method ensures that values are in the correct format  
- The getSetting method should provide a default value to fall back on for initialization errors

# StrategyLoader:

### **Test 1: Retrieve Available Movement Strategies Successfully (Happy Path)**

#### **Scenario: The system successfully retrieves a list of available movement strategies.**

#### **Steps:**

1. Create a mock implementation of StrategyLoader.  
2. Ensure that getAvailableMovementStrategies() returns a valid list of classes implementing MovementStrategy.  
3. Verify the returned list contains expected strategy classes.

#### **Expected Outcome:**

* The method returns a non-empty list of valid MovementStrategy classes.  
* No exceptions are thrown.

#### **How Design Supports Testing:**

* The StrategyLoader interface clearly defines getAvailableMovementStrategies() to return a list, which can be easily validated in a unit test.  
* Mocking allows for testing without requiring real implementations of all strategies.

### **Test 2: Retrieve Available Collision Strategies Successfully (Happy Path)**

#### **Scenario: The system successfully retrieves a list of available collision strategies.**

#### **Steps:**

* Create a mock implementation of StrategyLoader.  
* Ensure getAvailableCollisionStrategies() returns a valid list of classes implementing CollisionStrategy.  
* Validate that the returned list contains expected strategy classes.

#### **Expected Outcome:**

* The method returns a non-empty list of valid CollisionStrategy classes.  
* No exceptions are thrown.

#### **How Design Supports Testing:**

* The clear return type (List\<Class\<? extends CollisionStrategy\>\>) enables straightforward assertions.  
* Dependency injection or mock frameworks can be used to isolate testing.

### **Test 3: Attempt to Retrieve Strategies When None Are Available (Sad Path)**

#### **Scenario: The system tries to retrieve movement strategies when none are available.**

#### **Steps:**

1. Create a mock implementation of StrategyLoader where getAvailableMovementStrategies() returns an empty list.  
2. Call getAvailableMovementStrategies().  
3. Check if an empty list is returned or a LoadStrategyException is thrown.

#### **Expected Outcome:**

* The method either returns an empty list or throws a LoadStrategyException.

#### **How Design Supports Testing:**

* The API allows handling empty strategy lists, making it easy to check edge cases.  
* The LoadStrategyException provides a clear mechanism for failure scenarios.

### **Test 4: Error When Strategy Loading Fails (Sad Path)**

#### **Scenario: A failure occurs while loading effect strategies, causing an exception.**

#### **Steps:**

1. Create a mock implementation of StrategyLoader that throws LoadStrategyException when calling getAvailableEffectStrategies().  
2. Attempt to retrieve effect strategies.  
3. Verify that the exception is thrown and contains an appropriate error message.

#### **Expected Outcome:**

* A LoadStrategyException is thrown with a relevant message indicating why loading failed.

#### **How Design Supports Testing:**

* The exception-based design ensures failure cases can be handled and tested explicitly.  
* The LoadStrategyException class provides structured error handling.

