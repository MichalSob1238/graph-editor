package suite;

import cause.effect.chain.editor.App;
import de.tesis.dynaware.grapheditor.core.DefaultGraphEditor;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.util.concurrent.TimeoutException;

public class BaseTest extends ApplicationTest {

    @Before
    public void setupClass() throws Exception{
        System.getProperties().put("testfx.robot", "glass");
        ApplicationTest.launch(App.class);
    }



    @Override
    public void start(Stage stage) throws Exception {
        stage.setX(0.0);
        stage.setY(0.0);
        stage.setMinWidth(1900);
        stage.setMaxWidth(1900);
        stage.setMinHeight(1080);
        stage.setMaxHeight(1080);
        stage.show();
        stage.toFront();
    }

    @After
    public void afterEachTest() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

}
