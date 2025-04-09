package oogasalad.authoring.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.model.EntityPlacement;

/**
 * View component for displaying and editing properties of a selected entity placement.
 * This component complements the EntityPlacementEditorView and shows when a user
 * selects an entity on the canvas, allowing them to modify its position, mode,
 * and other instance-specific attributes.
 *
 * @author Angela Predolac
 */

public class EntityPlacementView {

    private final AuthoringController controller;
    private EntityPlacement currentPlacement;
    private final VBox rootNode;

    // UI Components
    private Label entityTypeLabel;
    private Label positionValueLabel;
    private ComboBox<String> modeSelector;
    private Button applyButton;
    private Button deleteButton;

    /**
     * Constructs a new view for displaying entity placement details.
     *
     * @param controller the authoring controller managing interactions
     */
    public EntityPlacementView(AuthoringController controller) {
        this.controller = controller;
        this.rootNode = new VBox(10);

        // Setup the UI layout and components
        setupUI();

        // Initially hide until an entity is selected
        this.rootNode.setVisible(false);
    }

    /**
     * Returns the JavaFX node representing this view.
     *
     * @return the root node of this view
     */
    public Node getNode() {
        return rootNode;
    }

    /**
     * Sets the entity placement to be displayed by this view.
     * Updates all fields to reflect the current state of the entity.
     *
     * @param placement the entity placement to display, or null to clear the view
     */
    public void setEntityPlacement(EntityPlacement placement) {
        this.currentPlacement = placement;

        if (placement == null) {
            this.rootNode.setVisible(false);
            return;
        }

        // Update UI fields with values from the placement
        entityTypeLabel.setText(placement.getTypeString());
        positionValueLabel.setText(String.format("X: %.1f, Y: %.1f", placement.getX(), placement.getY()));

        updateModeSelector();

        this.rootNode.setVisible(true);
    }

    /**
     * Initializes and arranges the UI components for the view.
     */
    private void setupUI() {
        rootNode.setPadding(new Insets(10));
        rootNode.getStyleClass().add("entity-placement-view");

        // Title
        Label titleLabel = new Label("Selected Entity");
        titleLabel.getStyleClass().add("section-header");

        // Entity type display (non-editable)
        Label typeLabel = new Label("Type:");
        entityTypeLabel = new Label("[No entity selected]");
        entityTypeLabel.getStyleClass().add("info-value");

        // Position display (non-editable)
        Label positionLabel = new Label("Position:");
        positionValueLabel = new Label("X: 0.0, Y: 0.0");
        positionValueLabel.getStyleClass().add("info-value");

        // Mode selection
        Label modeLabel = new Label("Mode:");
        modeSelector = new ComboBox<>();

        // Action buttons
        applyButton = new Button("Apply Changes");
        applyButton.setOnAction(e -> applyChanges());

        deleteButton = new Button("Delete Entity");
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setOnAction(e -> deleteEntity());

        // Create a grid for the labels and values
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(10);
        infoGrid.setVgap(5);
        infoGrid.add(typeLabel, 0, 0);
        infoGrid.add(entityTypeLabel, 1, 0);
        infoGrid.add(positionLabel, 0, 1);
        infoGrid.add(positionValueLabel, 1, 1);
        infoGrid.add(modeLabel, 0, 2);
        infoGrid.add(modeSelector, 1, 2);

        rootNode.getChildren().addAll(
                titleLabel,
                infoGrid,
                applyButton,
                deleteButton
        );
    }

    /**
     * Updates the mode selector with modes available for the current entity type.
     */
    private void updateModeSelector() {
        if (currentPlacement == null || currentPlacement.getType() == null) {
            modeSelector.getItems().clear();
            return;
        }

        String currentMode = currentPlacement.getMode();
        modeSelector.getItems().clear();
        modeSelector.getItems().addAll(currentPlacement.getType().modes().keySet());

        // Select the current mode
        if (currentMode != null && modeSelector.getItems().contains(currentMode)) {
            modeSelector.setValue(currentMode);
        } else if (!modeSelector.getItems().isEmpty()) {
            modeSelector.setValue(modeSelector.getItems().get(0));
        }
    }

    /**
     * Applies the changes made in the view to the current entity placement.
     */
    private void applyChanges() {
        if (currentPlacement == null) return;

        // Update mode
        String newMode = modeSelector.getValue();
        if (newMode != null && !newMode.equals(currentPlacement.getMode())) {
            currentPlacement.setMode(newMode);

            controller.moveEntity(currentPlacement, currentPlacement.getX(), currentPlacement.getY());
        }
    }

    /**
     * Deletes the current entity placement from the canvas.
     */
    private void deleteEntity() {
        if (currentPlacement == null) return;

        // Confirm deletion
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete Entity");
        confirm.setContentText("Are you sure you want to delete this entity?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            // Remove from the current level
            controller.getModel().getCurrentLevel().removeEntityPlacement(currentPlacement);

            // Update canvas view
            controller.updateCanvas();

            // Clear the view
            setEntityPlacement(null);
        }
    }

    /**
     * Updates the position display when an entity is moved on the canvas.
     */
    public void updatePositionDisplay() {
        if (currentPlacement != null) {
            positionValueLabel.setText(String.format("X: %.1f, Y: %.1f",
                    currentPlacement.getX(), currentPlacement.getY()));
        }
    }

    /**
     * Checks if this view is currently showing the specified entity placement.
     *
     * @param placement the entity placement to check
     * @return true if this is the currently displayed entity
     */
    public boolean isShowingPlacement(EntityPlacement placement) {
        return currentPlacement == placement;
    }

    /**
     * Sets the visibility of this view.
     *
     * @param visible true to show the view, false to hide it
     */
    public void setVisible(boolean visible) {
        rootNode.setVisible(visible);
    }

    /**
     * Gets whether this view is currently visible.
     *
     * @return true if the view is visible
     */
    public boolean isVisible() {
        return rootNode.isVisible();
    }
}