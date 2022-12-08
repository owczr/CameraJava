import javafx.application.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;


public class JavaFXApp extends Application {
    Stage stage;

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        //Passing FileInputStream object as a parameter
        //TODO: Change Image to microscope output
        FileInputStream inputstream = new FileInputStream("example.jpg");
        Image image = new Image(inputstream);

        //Setting image view
        ImageView imageView = new ImageView(image);

        //Setting the position of the image
        imageView.setX(0);
        imageView.setY(26);

        //setting the fit height and width of the image view
        imageView.setFitHeight(480);
        imageView.setFitWidth(640);

        //Setting the preserve ratio of the image view
        imageView.setPreserveRatio(true);

        //Menu items
        //TODO: Paste menu bar
        Menu menu1 = new Menu("File");
        MenuItem menuItem1 = new MenuItem("Item 1");
        MenuItem menuItem2 = new MenuItem("Exit");

        menuItem2.setOnAction(e -> {
            System.out.println("Exit Selected");
            exit_dialog();
        });

        menu1.getItems().add(menuItem1);
        menu1.getItems().add(menuItem2);

        //Menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu1);
        VBox vBox = new VBox(menuBar);

        //Buttons
        Button helpButton = new Button("?");
        helpButton.setLayoutX(0);
        helpButton.setLayoutY(480);

        Button rejectButton = new Button("Reject");
        rejectButton.setLayoutX(50);
        rejectButton.setLayoutY(480);

        Button saveButton = new Button("Save");
        saveButton.setLayoutX(100);
        saveButton.setLayoutY(480);

        Button upButton = new Button("Up");
        upButton.setLayoutX(0);
        upButton.setLayoutY(530);

        Button downButton = new Button("Down");
        downButton.setLayoutX(50);
        downButton.setLayoutY(530);

        Button leftButton = new Button("Left");
        leftButton.setLayoutX(100);
        leftButton.setLayoutY(530);

        Button rightButton = new Button("Right");
        rightButton.setLayoutX(150);
        rightButton.setLayoutY(530);

        Button inButton = new Button("Zoom In");
        inButton.setLayoutX(0);
        inButton.setLayoutY(580);

        Button outButton = new Button("Zoom Out");
        outButton.setLayoutX(50);
        outButton.setLayoutY(580);

        Button rotateLeftButton = new Button("Rotate Left");
        rotateLeftButton.setLayoutX(100);
        rotateLeftButton.setLayoutY(580);

        Button rotateRightButton = new Button("Rotate Right");
        rotateRightButton.setLayoutX(150);
        rotateRightButton.setLayoutY(580);

        Button snapButton = new Button("Snap");
        snapButton.setLayoutX(300);
        snapButton.setLayoutY(690);

        //Dropdown combo box
        //TODO: Move to Text section
        Text filtersText = new Text();
        filtersText.setText("Filters");
        filtersText.setLayoutX(0);
        filtersText.setLayoutY(630);

        ObservableList<String> filtersList =
                FXCollections.observableArrayList(
                        "Filter 1",
                        "Filter 2",
                        "Filter 3"
                );
        final ComboBox filtersBox = new ComboBox(filtersList);
        filtersBox.setLayoutX(50);
        filtersBox.setLayoutY(630);

        //Sliders
        //TODO: Move to Text section
        Text contrastText = new Text();
        contrastText.setText("Contrast");
        contrastText.setLayoutX(400);
        contrastText.setLayoutY(530);

        Slider contrastSlider = new Slider();
        contrastSlider.setMin(0);
        contrastSlider.setMax(100);
        contrastSlider.setValue(40);
        contrastSlider.setShowTickLabels(true);
        contrastSlider.setShowTickMarks(true);
        contrastSlider.setMajorTickUnit(50);
        contrastSlider.setMinorTickCount(5);
        contrastSlider.setBlockIncrement(10);
        contrastSlider.setLayoutX(480);
        contrastSlider.setLayoutY(520);

        //TODO: Move to Text section
        Text brightnessText = new Text();
        brightnessText.setText("Brightness");
        brightnessText.setLayoutX(400);
        brightnessText.setLayoutY(580);

        Slider brightnessSlider = new Slider();
        brightnessSlider.setMin(0);
        brightnessSlider.setMax(100);
        brightnessSlider.setValue(40);
        brightnessSlider.setShowTickLabels(true);
        brightnessSlider.setShowTickMarks(true);
        brightnessSlider.setMajorTickUnit(50);
        brightnessSlider.setMinorTickCount(5);
        brightnessSlider.setBlockIncrement(10);
        brightnessSlider.setLayoutX(480);
        brightnessSlider.setLayoutY(570);

        //TODO: Move to Text section
        Text ledText = new Text();
        ledText.setText("Led");
        ledText.setLayoutX(400);
        ledText.setLayoutY(630);

        Slider ledSlider = new Slider();
        ledSlider.setMin(0);
        ledSlider.setMax(100);
        ledSlider.setValue(40);
        ledSlider.setShowTickLabels(true);
        ledSlider.setShowTickMarks(true);
        ledSlider.setMajorTickUnit(50);
        ledSlider.setMinorTickCount(5);
        ledSlider.setBlockIncrement(10);
        ledSlider.setLayoutX(480);
        ledSlider.setLayoutY(620);

        //Creating a Group object
        Group root = new Group();

        root.getChildren().add(imageView);
        root.getChildren().add(vBox);

        root.getChildren().add(helpButton);
        root.getChildren().add(rejectButton);
        root.getChildren().add(saveButton);

        root.getChildren().add(upButton);
        root.getChildren().add(downButton);
        root.getChildren().add(leftButton);
        root.getChildren().add(rightButton);

        root.getChildren().add(inButton);
        root.getChildren().add(outButton);

        root.getChildren().add(rotateRightButton);
        root.getChildren().add(rotateLeftButton);

        root.getChildren().add(filtersBox);
        root.getChildren().add(filtersText);

        root.getChildren().add(contrastSlider);
        root.getChildren().add(contrastText);

        root.getChildren().add(brightnessSlider);
        root.getChildren().add(brightnessText);

        root.getChildren().add(ledText);
        root.getChildren().add(ledSlider);

        root.getChildren().add(snapButton);


        //Scene
        Scene scene = new Scene(root,640, 720);


        //Stage
        stage = primaryStage;
        primaryStage.setTitle("JavaFX App");

        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            exit_dialog();
        });
        primaryStage.show();
    }


    public void item_1() {
        System.out.println("item 1");
    }

    public void exit_dialog() {
        System.out.println("exit dialog");


        Alert alert = new Alert(AlertType.CONFIRMATION,
                "Do you really want to exit the program?.",
                ButtonType.YES, ButtonType.NO);

        alert.setResizable(true);
        alert.onShownProperty().addListener(e -> {
            Platform.runLater(() -> alert.setResizable(false));
        });

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            Platform.exit();
        } else {
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
