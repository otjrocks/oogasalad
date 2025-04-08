package oogasalad.authoring.view;

import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.model.EntityType;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * A view for editing collision rules between entity types.
 * Uses a grid/table UI to define collision strategies between entity pairs.
 *
 * This is a skeleton implementation that can be expanded with your actual collision rule
 * data model and functionality.
 *
 * @author Angela Predolac
 */
public class CollisionRuleEditorView {

    private static final String TITLE = "Collision Rules Editor";
    private static final double DEFAULT_SPACING = 10;
    private static final double DEFAULT_PADDING = 15;

    private static final String[] COLLISION_STRATEGIES = {
            "NoAction", "LoseLife", "GainPoints", "RemoveEntity", "TriggerEffect"
    };

    private AuthoringController controller;

    // Root node containing the view
    private BorderPane rootNode;

    // UI Components
    private TableView<CollisionRule> rulesTable;
    private ComboBox<String> entityAComboBox;
    private ComboBox<String> entityBComboBox;
    private ComboBox<String> strategyComboBox;

    /**
     * Constructor initializes the view with the given controller
     */
    public CollisionRuleEditorView(AuthoringController controller) {
        this.controller = controller;

        // Create the UI
        this.rootNode = new BorderPane();
        rootNode.getStyleClass().add("collision-editor-view");

        setupUI();
        loadEntityTypes();
    }

    /**
     * Returns the JavaFX node that represents this view
     */
    public Node getNode() {
        return rootNode;
    }

    /**
     * Set up the UI components
     */
    private void setupUI() {
        // Setup the main container with padding
        rootNode.setPadding(new Insets(DEFAULT_PADDING));

        // Create a title label
        Label titleLabel = new Label(TITLE);
        titleLabel.getStyleClass().add("editor-title");

        // Create the rules table
        setupRulesTable();

        // Create a form for adding new rules
        VBox addRuleForm = createAddRuleForm();

        // Add components to the layout
        VBox topSection = new VBox(DEFAULT_SPACING);
        topSection.getChildren().addAll(titleLabel, addRuleForm);

        rootNode.setTop(topSection);
        rootNode.setCenter(rulesTable);
    }

    /**
     * Set up the table view for displaying collision rules
     */
    private void setupRulesTable() {
        rulesTable = new TableView<>();

        // Entity A column
        TableColumn<CollisionRule, String> entityAColumn = new TableColumn<>("Entity A");
        entityAColumn.setCellValueFactory(data -> data.getValue().entityAProperty());
        entityAColumn.setPrefWidth(150);

        // Entity B column
        TableColumn<CollisionRule, String> entityBColumn = new TableColumn<>("Entity B");
        entityBColumn.setCellValueFactory(data -> data.getValue().entityBProperty());
        entityBColumn.setPrefWidth(150);

        // Strategy column
        TableColumn<CollisionRule, String> strategyColumn = new TableColumn<>("Collision Strategy");
        strategyColumn.setCellValueFactory(data -> data.getValue().strategyProperty());
        strategyColumn.setPrefWidth(200);

        // Actions column
        TableColumn<CollisionRule, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(100);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    CollisionRule rule = getTableView().getItems().get(getIndex());
                    deleteRule(rule);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        rulesTable.getColumns().addAll(entityAColumn, entityBColumn, strategyColumn, actionsColumn);

        // Add some sample data (replace with actual data loading)
        loadSampleRules();
    }

    /**
     * Create the form for adding new collision rules
     */
    private VBox createAddRuleForm() {
        VBox formContainer = new VBox(DEFAULT_SPACING);
        formContainer.getStyleClass().add("add-rule-form");

        // Create a grid for form fields
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

        // Entity A selection
        Label entityALabel = new Label("Entity A:");
        entityAComboBox = new ComboBox<>();
        entityAComboBox.setPromptText("Select Entity Type");
        entityAComboBox.setPrefWidth(150);
        formGrid.add(entityALabel, 0, 0);
        formGrid.add(entityAComboBox, 1, 0);

        // Entity B selection
        Label entityBLabel = new Label("Entity B:");
        entityBComboBox = new ComboBox<>();
        entityBComboBox.setPromptText("Select Entity Type");
        entityBComboBox.setPrefWidth(150);
        formGrid.add(entityBLabel, 0, 1);
        formGrid.add(entityBComboBox, 1, 1);

        // Strategy selection
        Label strategyLabel = new Label("Collision Strategy:");
        strategyComboBox = new ComboBox<>(FXCollections.observableArrayList(COLLISION_STRATEGIES));
        strategyComboBox.setPromptText("Select Strategy");
        strategyComboBox.setPrefWidth(150);
        formGrid.add(strategyLabel, 0, 2);
        formGrid.add(strategyComboBox, 1, 2);

        // Add rule button
        Button addButton = new Button("Add Rule");
        addButton.setOnAction(e -> addRule());

        // Form title and container
        Label formTitle = new Label("Add New Collision Rule");
        formTitle.getStyleClass().add("form-title");

        HBox buttonRow = new HBox(10);
        buttonRow.getChildren().add(addButton);

        formContainer.getChildren().addAll(formTitle, formGrid, buttonRow);
        return formContainer;
    }

    /**
     * Load entity types from the model to populate the entity selection dropdowns
     */
    private void loadEntityTypes() {
        List<String> entityTypes = new ArrayList<>();

        // Get entity types from the model
        for (EntityType type : controller.getModel().getEntityTypes()) {
            entityTypes.add(type.getType());
        }

        // Update combo boxes
        entityAComboBox.setItems(FXCollections.observableArrayList(entityTypes));
        entityBComboBox.setItems(FXCollections.observableArrayList(entityTypes));
    }

    /**
     * Load sample collision rules (replace with actual data)
     */
    private void loadSampleRules() {
        // This would be replaced with loading actual rules from your model
        List<CollisionRule> sampleRules = new ArrayList<>();
        sampleRules.add(new CollisionRule("Pacman", "Ghost", "LoseLife"));
        sampleRules.add(new CollisionRule("Pacman", "PowerPellet", "TriggerEffect"));

        rulesTable.setItems(FXCollections.observableArrayList(sampleRules));
    }

    /**
     * Add a new collision rule based on the form inputs
     */
    private void addRule() {
        String entityA = entityAComboBox.getValue();
        String entityB = entityBComboBox.getValue();
        String strategy = strategyComboBox.getValue();

        if (entityA == null || entityB == null || strategy == null) {
            // Show error if any fields are empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select values for all fields.");
            alert.showAndWait();
            return;
        }

        // Create a new rule
        CollisionRule newRule = new CollisionRule(entityA, entityB, strategy);

        // Add to the table
        rulesTable.getItems().add(newRule);

        // Clear selections
        entityAComboBox.getSelectionModel().clearSelection();
        entityBComboBox.getSelectionModel().clearSelection();
        strategyComboBox.getSelectionModel().clearSelection();

        // TODO: Add the rule to your collision rules model
    }

    /**
     * Delete a collision rule
     */
    private void deleteRule(CollisionRule rule) {
        // Remove from the table
        rulesTable.getItems().remove(rule);

        // TODO: Remove the rule from your collision rules model
    }

    /**
     * Update the view to reflect the current model state
     */
    public void updateFromModel() {
        // Re-load entity types
        loadEntityTypes();

        // TODO: Re-load collision rules from your model
    }

    /**
     * Inner class to represent a collision rule in the UI
     * This would be replaced with your actual collision rule model class
     */
    public static class CollisionRule {
        private final javafx.beans.property.SimpleStringProperty entityA;
        private final javafx.beans.property.SimpleStringProperty entityB;
        private final javafx.beans.property.SimpleStringProperty strategy;

        public CollisionRule(String entityA, String entityB, String strategy) {
            this.entityA = new javafx.beans.property.SimpleStringProperty(entityA);
            this.entityB = new javafx.beans.property.SimpleStringProperty(entityB);
            this.strategy = new javafx.beans.property.SimpleStringProperty(strategy);
        }

        public javafx.beans.property.StringProperty entityAProperty() {
            return entityA;
        }

        public javafx.beans.property.StringProperty entityBProperty() {
            return entityB;
        }

        public javafx.beans.property.StringProperty strategyProperty() {
            return strategy;
        }
    }
}
