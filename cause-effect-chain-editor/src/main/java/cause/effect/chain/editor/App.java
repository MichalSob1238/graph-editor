package cause.effect.chain.editor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import cause.effect.chain.editor.controller.CauseEffectChainEditorController;

import java.net.URL;

public class App extends Application {

    private static final String APPLICATION_TITLE = "Cause Efect Chain Editor";
    private static final String DEMO_STYLESHEET = "/css/demo.css";
    private static final String GATE_SKIN_STYLESHEET = "/cause.effect.chain.editor/gateSkin.css";
    private static final String FONT_AWESOME = "/fonts/fontawesome.ttf";

    @Override
    public void start(final Stage stage) throws Exception {

        final URL location = getClass().getResource("/view/GUI.fxml");
        final FXMLLoader loader = new FXMLLoader();
        final Parent root = loader.load(location.openStream());
        final CauseEffectChainEditorController controller = loader.getController();


        final Scene scene = new Scene(root, 830, 630);

        scene.getStylesheets().add(getClass().getResource(DEMO_STYLESHEET).toExternalForm());
        scene.getStylesheets().add(getClass().getResource(GATE_SKIN_STYLESHEET).toExternalForm());
        Font.loadFont(getClass().getResource(FONT_AWESOME).toExternalForm(), 12);

        stage.setScene(scene);
        stage.setTitle(APPLICATION_TITLE);

        stage.show();

        controller.panToCenter();
    }

    public static void main(final String[] args) {
        launch(args);
    }
}