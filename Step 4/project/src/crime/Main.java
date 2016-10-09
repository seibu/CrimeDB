package crime;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private Controller c = null;
    private BorderPane mainPane;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        primaryStage.setTitle(Config.APP_TITLE);
        primaryStage.setOnCloseRequest(event -> {
            // stop the propagation of the event
            event.consume();
            close();
        });

        // load the root.fxml with the main.fxml
        initMainPane();
    }

    private void initMainPane() {
        try {
            // init FXMLLoader
            FXMLLoader loader = new FXMLLoader();
            // set location to main.fxml
            loader.setLocation(getClass().getResource(Config.FXML_ROOT));
            // load
            mainPane = (BorderPane) loader.load();

            // singleton (we will only use one controller
            if (c == null) {
                c = loader.getController();
                c.setMain(this);
            }

            // set the scene
            primaryStage.setScene(new Scene(mainPane));
            showFXML(Config.FXML_MAIN);

            //show the stage
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  * Shows a specific fxml-files elements in the center of the given BorderPane
     *
     * @param path to fxml file relative to the main.class
     * @return returns the controller specified in the fxml
     */
    public void showFXML(String path) {
        try {
            // initilize a FXMLLoader
            FXMLLoader loader = new FXMLLoader();
            // set the given path to the fxml
            loader.setLocation(getClass().getResource(path));
            // load the fxml
            Pane pane = (Pane) loader.load();

            mainPane.setCenter(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        close();
    }

    /**
     * Terminate all threads and close the app
     */
    private void close() {
        System.exit(0);
    }

    public static void main (String[] args) {
        launch(args);
    }
}
