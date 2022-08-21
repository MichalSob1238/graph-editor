package cause.effect.chain.editor.model.skins.CauseActionModel;

import com.jfoenix.controls.JFXTextField;
import de.tesis.dynaware.grapheditor.GConnectorSkin;
import de.tesis.dynaware.grapheditor.GNodeSkin;
import de.tesis.dynaware.grapheditor.core.skins.defaults.utils.DefaultConnectorTypes;
import de.tesis.dynaware.grapheditor.model.*;
import de.tesis.dynaware.grapheditor.utils.GeometryUtils;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import java.util.*;

import static java.lang.Thread.sleep;


public class CecaDiagramNodeSkin extends GNodeSkin {

    private final Label title = new Label();
    private final Rectangle border = new Rectangle();

    private static final String STYLE_CLASS_BORDER = "default-node-border";
    private static final String STYLE_CLASS_BACKGROUND = "default-node-background";
    private static final String STYLE_CLASS_SELECTION_HALO = "default-node-selection-halo";

    private static final double HALO_OFFSET = 5;
    private static final double HALO_CORNER_SIZE = 10;

    private static final double MINOR_POSITIVE_OFFSET = 2;
    private static final double MINOR_NEGATIVE_OFFSET = -3;

    private final Rectangle selectionHalo = new Rectangle();
    private final Rectangle errorHalo = new Rectangle();
    private final Rectangle background = new Rectangle();
    JFXTextField descriptionEditable = new JFXTextField();

    private static final Map<String, String> colours = new HashMap<String, String>() {{
        put(CecaDiagramConstants.CAUSE_ACTION_ROOT, "#B2BEB5");
        put(CecaDiagramConstants.TARGET_DISADVANTAGE, "#B2BEB5");
        put(CecaDiagramConstants.CONDITION, "#89CFF0");
        put(CecaDiagramConstants.ACTION, "#50C878");
    }};


    private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass("selected");

    private final List<GConnectorSkin> topConnectorSkins = new ArrayList<>();
    private final List<GConnectorSkin> rightConnectorSkins = new ArrayList<>();
    private final List<GConnectorSkin> bottomConnectorSkins = new ArrayList<>();
    private final List<GConnectorSkin> leftConnectorSkins = new ArrayList<>();
    private EventHandler<? super MouseEvent> doubleClickedListener = getDoubleClickedListener();
    private final List<String> issuesWithNode = new ArrayList<>();
    //public boolean isCorrect = false;


    public Tooltip tooltip = new Tooltip("");

    private EventHandler<MouseEvent> getDoubleClickedListener() {
        return event -> {
            if (event.getButton().compareTo(MouseButton.SECONDARY) == 0)
            {
                //System.out.println("State Machine handling rightclick");
                showNodeInformation();
            }
            else
            if (event.getClickCount() >= 2) {
                if (!isCorrect) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Issues with the node detected ");
                    alert.setHeaderText("Node has the following issues that prevent the model from being legal");

                    TextArea textArea = new TextArea();
                    for (String issue : issuesWithNode) {
                        textArea.appendText(issue + ".\n");
                    }
                    textArea.setEditable(false);
                    textArea.setWrapText(true);

                    textArea.setMaxWidth(Double.MAX_VALUE);
                    textArea.setMaxHeight(Double.MAX_VALUE);
                    GridPane.setVgrow(textArea, Priority.ALWAYS);
                    GridPane.setHgrow(textArea, Priority.ALWAYS);

                    alert.getDialogPane().setContent(textArea);

                    alert.showAndWait();
                } else {
                    //System.out.println("handling doubleclick ceca node");
                    ////System.out.println(getNode());
                    descriptionEditable.setPrefSize(-1, -1);
                    descriptionEditable.setMinSize(title.getWidth(), title.getHeight());
                    descriptionEditable.setMaxSize(background.getWidth(), background.getHeight());
                    descriptionEditable.setTranslateX(title.getTranslateX());
                    descriptionEditable.setTranslateY(title.getTranslateY());
                    descriptionEditable.setText(title.getText());
                    title.setVisible(false);
                    getRoot().getChildren().add(descriptionEditable);
                    descriptionEditable.selectAll();
                    Font font = new Font("Arial", 11);
                    descriptionEditable.setFont(font);

                    boolean foc = requestFocusOrDieTrying(descriptionEditable);
                    descriptionEditable.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (foc &&!newValue) {
                            //System.out.println("in focused new value");
                            getNode().setDescription(descriptionEditable.getText());
                            title.setText(descriptionEditable.getText());
                            getRoot().getChildren().remove(descriptionEditable);
                            setDescription();
                            tooltip.setText(descriptionEditable.getText());
                            title.setVisible(true);
                        }
                    });

                }
            }
        };
    }

    private void showNodeInformation() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setResizable(false);

        alert.getDialogPane().setPrefSize(600, 400);
        alert.getDialogPane().setMinSize(500, 300);

        alert.setHeaderText("Information about the node:");

        Label nodeType = new Label("Node type: Cause action perspective node");
        Label nodeSubtype = new Label("Node subtype: " + getNode().getSubtype());

        Label description = new Label("Description: ");
        TextArea descriptionLabel = new TextArea();
        descriptionLabel.setText(getNode().getDescription());


        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5, 5, 5, 60));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(nodeType, 0, 0, 1, 1);
        grid.add(nodeSubtype, 0, 1, 1, 1);
        grid.add(description, 0, 2, 1, 1);
        grid.add(descriptionLabel, 0, 3, 2, 2);

        alert.getDialogPane().setContent(grid);
        alert.getDialogPane().resize(400, 400);
        //alert.getDialogPane().getContent().prefWidth();
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.LEFT);
        ButtonType addOutputConnector = new ButtonType("Add Output Connector", ButtonBar.ButtonData.LEFT);
        ButtonType clearConnectors = new ButtonType("Remove Empty Connectors", ButtonBar.ButtonData.LEFT);

        alert.getDialogPane().getButtonTypes().add(buttonTypeOk);
        alert.getDialogPane().getButtonTypes().add(addOutputConnector);
        alert.getDialogPane().getButtonTypes().add(clearConnectors);

        alert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        final Button btOk = (Button) alert.getDialogPane().lookupButton(buttonTypeOk);
        final Button addOutputConnectorBtn = (Button) alert.getDialogPane().lookupButton(addOutputConnector);
        final Button clearConnectorsBtn = (Button) alert.getDialogPane().lookupButton(clearConnectors);

        btOk.addEventFilter(
                ActionEvent.ACTION,
                action -> {
                    getNode().setDescription(descriptionLabel.getText());
                   // System.out.println("set description to " + descriptionLabel.getText());
                    setDescription();
                }
        );

        addOutputConnectorBtn.addEventFilter(
                ActionEvent.ACTION,
                action -> {
                    addConnector(getNode(), DefaultConnectorTypes.RIGHT_OUTPUT);
                    action.consume();
                }
        );

        clearConnectorsBtn.addEventFilter(
                ActionEvent.ACTION,
                action -> {
                    getNode().getConnectors().removeIf(connector -> connector.getConnections().isEmpty());
                    getGraphEditor().reload();
                    action.consume();
                }
        );

        //System.out.println("alert width: " + alert.getWidth());
        alert.setWidth(1200);
       // System.out.println("alert width: " + alert.getWidth());

        Optional<ButtonType> x = alert.showAndWait();

    }

    public GConnector addConnector(GNode node, String type) {
        final GConnector connector = GraphFactory.eINSTANCE.createGConnector();
        connector.setType(type);
        final GModel model = getGraphEditor().getModel();
        final CompoundCommand command = new CompoundCommand();
        final EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(model);
        final EReference connectors = GraphPackage.Literals.GCONNECTABLE__CONNECTORS;
        command.append(AddCommand.create(editingDomain, node, connectors, connector));
        if (command.canExecute()) {
            editingDomain.getCommandStack().execute(command);
        }
        return connector;
    }

    private boolean requestFocusOrDieTrying(Node node) {
        Platform.runLater(() -> {
            if (!node.isFocused()) {
                node.requestFocus();
                requestFocusOrDieTrying(node);
            }
        });
        return true;
    }

    //TODO: customise
    public void setDescription() {
        Font font = new Font("Arial", 11);

        title.setMaxSize(border.getWidth(), border.getHeight());
        title.setTextAlignment(TextAlignment.CENTER);
        title.setWrapText(true);

        title.setText(Optional.ofNullable(getNode().getDescription()).orElse("!!"));
        title.setFont(font);
    }

    /**
     * Creates a new {@link GNodeSkin}.
     *
     * @param node the {@link GNode} represented by the skin
     */
    public CecaDiagramNodeSkin(GNode node) {
        super(node);

        border.widthProperty().bind(getRoot().widthProperty());
        border.heightProperty().bind(getRoot().heightProperty());
        background.widthProperty().bind(border.widthProperty().subtract(border.strokeWidthProperty().multiply(2)));
        background.heightProperty().bind(border.heightProperty().subtract(border.strokeWidthProperty().multiply(2)));

//        getNode().setHeight(50);
//        getNode().setWidth(100);
        title.setText(node.getDescription());
        //System.out.println("ceca setting title");
        title.setAlignment(Pos.CENTER);
        title.setVisible(true);
        Font font = new Font("Arial", 11);
        title.setFont(font);

        background.getStyleClass().setAll(STYLE_CLASS_BACKGROUND);
        border.getStyleClass().setAll(STYLE_CLASS_BORDER);


        title.setOnMouseClicked(doubleClickedListener);
        background.setOnMouseClicked(doubleClickedListener);
        getRoot().getChildren().addAll(border, background);
        getRoot().getChildren().add(title);
        getRoot().setMinSize(50, 50);

        tooltip.setShowDelay(Duration.seconds(1.0));

        tooltip.setWrapText(true);
        tooltip.setMaxWidth(200);
        tooltip.setMaxWidth(600);
        Font font2 = new Font("Arial", 13);
        tooltip.setFont(font2);

        getRoot().getChildren().forEach(mhm -> Tooltip.install(mhm, tooltip));
        title.setAlignment(Pos.CENTER);

        title.setVisible(true);
        addSelectionHalo();
        addSelectionListener();
        updateColour();

        addErrorHalo();
        addInitialError();
    }

    private void addInitialError() {
        String subtype = getNode().getSubtype();
        List<String> status = new ArrayList<>();
        switch (subtype) {
            case CecaDiagramConstants.ACTION:{
                status.add("Action node must have at least one input.");
                status.add("Action node must have at least one output.");
                break;
            }
            case CecaDiagramConstants.CONDITION:{
                status.add("Condition node must have at least one input.");
                status.add("Condition node must have at least one output.");
                break;
            }
            case CecaDiagramConstants.TARGET_DISADVANTAGE:{
                status.add("Target disadvantage node must have at least one input.");
                break;
            }
            case CecaDiagramConstants.CAUSE_ACTION_ROOT:{
                status.add("Root cause node must have at least one output.");
                break;
            }
        }
        updateStatus(status);
    }

    private void addErrorHalo() {
        getRoot().getChildren().add(errorHalo);

        errorHalo.setManaged(false);
        errorHalo.setMouseTransparent(false);
        errorHalo.setVisible(false);

        errorHalo.setLayoutX(-HALO_OFFSET);
        errorHalo.setLayoutY(-HALO_OFFSET);
        errorHalo.getStyleClass().add("default-node-error-halo");
    }

    private void updateColour() {
        if (issuesWithNode.isEmpty()) {
            //System.out.println("true");
            this.background.setStyle("-fx-fill:" + colours.get(getNode().getSubtype()) + ";");
            this.isCorrect = true;

        } else {
            //System.out.println("false on create status");
            //this.background.setStyle("-fx-fill:#FF4500;");
            this.isCorrect = false;
        }
    }

    @Override
    public void setConnectorSkins(final List<GConnectorSkin> connectorSkins) {

        removeAllConnectors();

        topConnectorSkins.clear();
        rightConnectorSkins.clear();
        bottomConnectorSkins.clear();
        leftConnectorSkins.clear();

        if (connectorSkins != null) {
            for (final GConnectorSkin connectorSkin : connectorSkins) {

                final String connectorType = connectorSkin.getConnector().getType();
                switch (typeOfConnector(connectorType)) {
                    case "top":
                        topConnectorSkins.add(connectorSkin);
                        break;
                    case "bottom":
                        bottomConnectorSkins.add(connectorSkin);
                        break;
                    case "left":
                        leftConnectorSkins.add(connectorSkin);
                        break;
                    case "right":
                        rightConnectorSkins.add(connectorSkin);
                        break;
                }

                getRoot().getChildren().add(connectorSkin.getRoot());
            }
        }
        layoutConnectors();
    }

    private String typeOfConnector(String connectorType) {

        String result = "invalid";
        if (DefaultConnectorTypes.isTop(connectorType)) {
            result = "top";
        } else if (DefaultConnectorTypes.isBottom(connectorType)) {
            result = "bottom";
        } else if (DefaultConnectorTypes.isRight(connectorType)) {
            result = "right";
        } else if (DefaultConnectorTypes.isLeft(connectorType)) {
            result = "left";
        }
        return result;
    }

    private void removeAllConnectors() {

        for (final GConnectorSkin connectorSkin : topConnectorSkins) {
            getRoot().getChildren().remove(connectorSkin.getRoot());
        }

        for (final GConnectorSkin connectorSkin : bottomConnectorSkins) {
            getRoot().getChildren().remove(connectorSkin.getRoot());
        }

        for (final GConnectorSkin connectorSkin : leftConnectorSkins) {
            getRoot().getChildren().remove(connectorSkin.getRoot());
        }

        for (final GConnectorSkin connectorSkin : rightConnectorSkins) {
            getRoot().getChildren().remove(connectorSkin.getRoot());
        }
    }

    @Override
    public void layoutConnectors() {
        layoutAllConnectors();
        layoutSelectionHalo();
        layoutErrorHalo();

    }

    void addSelectionListener() {

        selectedProperty().addListener((v, o, n) -> {

            if (n) {
                selectionHalo.setVisible(true);
                layoutSelectionHalo();
                background.pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, true);
                getRoot().toFront();
            } else {
                selectionHalo.setVisible(false);
                background.pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, false);
            }
        });
    }

    private void layoutAllConnectors() {

        layoutConnectors(topConnectorSkins, false, 0);
        layoutConnectors(rightConnectorSkins, true, getRoot().getWidth());
        layoutConnectors(bottomConnectorSkins, false, getRoot().getHeight());
        layoutConnectors(leftConnectorSkins, true, 0);
    }

    private void layoutConnectors(final List<GConnectorSkin> connectorSkins, final boolean vertical, final double offset) {

        final int count = connectorSkins.size();

        for (int i = 0; i < count; i++) {

            final GConnectorSkin skin = connectorSkins.get(i);
            final Node root = skin.getRoot();

            if (vertical) {

                final double offsetY = getRoot().getHeight() / (count + 1);
                final double offsetX = getMinorOffsetX(skin.getConnector());

                root.setLayoutX(GeometryUtils.moveOnPixel(offset - skin.getWidth() / 2 + offsetX));
                root.setLayoutY(GeometryUtils.moveOnPixel((i + 1) * offsetY - skin.getHeight() / 2));

            } else {

                final double offsetX = getRoot().getWidth() / (count + 1);
                final double offsetY = getMinorOffsetY(skin.getConnector());

                root.setLayoutX(GeometryUtils.moveOnPixel((i + 1) * offsetX - skin.getWidth() / 2));
                root.setLayoutY(GeometryUtils.moveOnPixel(offset - skin.getHeight() / 2 + offsetY));
            }
        }
    }

    @Override
    public Point2D getConnectorPosition(GConnectorSkin connectorSkin) {
        final Node connectorRoot = connectorSkin.getRoot();

        final Side side = DefaultConnectorTypes.getSide(connectorSkin.getConnector().getType());

        // The following logic is required because the connectors are offset slightly from the node edges.
        final double x, y;
        if (side.equals(Side.LEFT)) {
            x = 0;
            y = connectorRoot.getLayoutY() + connectorSkin.getHeight() / 2;
        } else if (side.equals(Side.RIGHT)) {
            x = getRoot().getWidth();
            y = connectorRoot.getLayoutY() + connectorSkin.getHeight() / 2;
        } else if (side.equals(Side.TOP)) {
            x = connectorRoot.getLayoutX() + connectorSkin.getWidth() / 2;
            y = 0;
        } else {
            x = connectorRoot.getLayoutX() + connectorSkin.getWidth() / 2;
            y = getRoot().getHeight();
        }

        return new Point2D(x, y);
    }

    @Override
    public int updateStatus(List<String> status) {
        //System.out.println("updating status");
        if (status.isEmpty()) {
            //System.out.println("true");
            this.background.setStyle("-fx-fill:" + colours.get(getNode().getSubtype()) + ";");
            this.isCorrect = true;
            this.issuesWithNode.clear();
            errorHalo.setVisible(false);
            tooltip.setText(getNode().getDescription());

        } else {
            this.isCorrect = false;
            this.issuesWithNode.clear();
            this.issuesWithNode.addAll(status);
            errorHalo.setVisible(true);
            layoutErrorHalo();
            tooltip.setText(String.join("\n", issuesWithNode));
        }
        return status.size();
    }

    private double getMinorOffsetX(final GConnector connector) {

        final String type = connector.getType();

        if (type.equals(DefaultConnectorTypes.LEFT_INPUT) || type.equals(DefaultConnectorTypes.RIGHT_OUTPUT)) {
            return MINOR_POSITIVE_OFFSET;
        } else {
            return MINOR_NEGATIVE_OFFSET;
        }
    }

    private double getMinorOffsetY(final GConnector connector) {

        final String type = connector.getType();

        if (type.equals(DefaultConnectorTypes.TOP_INPUT) || type.equals(DefaultConnectorTypes.BOTTOM_OUTPUT)) {
            return MINOR_POSITIVE_OFFSET;
        } else {
            return MINOR_NEGATIVE_OFFSET;
        }
    }

    void addSelectionHalo() {

        getRoot().getChildren().add(selectionHalo);

        selectionHalo.setManaged(false);
        selectionHalo.setMouseTransparent(false);
        selectionHalo.setVisible(false);

        selectionHalo.setLayoutX(-HALO_OFFSET);
        selectionHalo.setLayoutY(-HALO_OFFSET);
        selectionHalo.getStyleClass().add(STYLE_CLASS_SELECTION_HALO);


    }

    private void layoutSelectionHalo() {

        if (selectionHalo.isVisible()) {

            selectionHalo.setWidth(border.getWidth() + 2 * HALO_OFFSET);
            selectionHalo.setHeight(border.getHeight() + 2 * HALO_OFFSET);

            final double cornerLength = 2 * HALO_CORNER_SIZE;
            final double xGap = border.getWidth() - 2 * HALO_CORNER_SIZE + 2 * HALO_OFFSET;
            final double yGap = border.getHeight() - 2 * HALO_CORNER_SIZE + 2 * HALO_OFFSET;

            selectionHalo.setStrokeDashOffset(HALO_CORNER_SIZE);
            selectionHalo.getStrokeDashArray().setAll(cornerLength, yGap, cornerLength, xGap);
        }
    }

    private void layoutErrorHalo() {

        if (errorHalo.isVisible()) {

            errorHalo.setWidth(border.getWidth() + 2 * HALO_OFFSET);
            errorHalo.setHeight(border.getHeight() + 2 * HALO_OFFSET);
            final double cornerLength = 2 * HALO_CORNER_SIZE;
            final double xGap = border.getWidth() - 2 * HALO_CORNER_SIZE + 2 * HALO_OFFSET;
            final double yGap = border.getHeight() - 2 * HALO_CORNER_SIZE + 2 * HALO_OFFSET;

            errorHalo.setStrokeDashOffset(-HALO_CORNER_SIZE);
            errorHalo.getStrokeDashArray().setAll(yGap, cornerLength, xGap, cornerLength);
        }
    }

    private void filterMouseDragged(final MouseEvent event) {
        if (event.isPrimaryButtonDown() && !isSelected()) {
            event.consume();
        }
    }
}
