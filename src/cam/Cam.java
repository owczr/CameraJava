package cam;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import javax.imageio.ImageIO;

public class Cam extends Application {
    private static final int FRAME_WIDTH = 640;
    private static final int FRAME_HEIGHT = 480;
    GraphicsContext gc;
    Canvas canvas;
    byte[] buffer;
    PixelWriter pixelWriter;
    PixelFormat<ByteBuffer> pixelFormat;

    //Frames frames;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        int result;

        Timeline timeline;

        //frames = new Frames();
        //result = frames.open_shm("/frames");

        canvas = new Canvas(650, 490);
        gc = canvas.getGraphicsContext2D();

        primaryStage.setTitle("Camera");

        //Menu items
        Menu menu1 = new Menu("Main");
        MenuItem snapM = new MenuItem("Snapshot");
        MenuItem startM = new MenuItem("Start");
        MenuItem deviceM = new MenuItem("Device");
        menu1.getItems().addAll(snapM,startM,deviceM);

        Menu menu2 = new Menu("Edit");
        Menu zoomM = new Menu("Zoom");
        Menu moveM = new Menu("Move");
        menu2.getItems().addAll(zoomM,moveM);

        MenuItem zoomMI = new MenuItem("Zoom in");
        MenuItem zoomMO = new MenuItem("Zoom out");
        zoomM.getItems().addAll(zoomMI,zoomMO);

        MenuItem moveU = new MenuItem("Move up");
        MenuItem moveD = new MenuItem("Move down");
        MenuItem moveL = new MenuItem("Move left");
        MenuItem moveR = new MenuItem("Move right");
        moveM.getItems().addAll(moveU,moveD,moveL,moveR);

        Menu menu3 = new Menu("Filters");
        CheckMenuItem filter1 = new CheckMenuItem("Black&white");
        CheckMenuItem filter2 = new CheckMenuItem("Negative");
        menu3.getItems().addAll(filter1,filter2);

        Menu menu4 = new Menu("Tools");
        CheckMenuItem ledM = new CheckMenuItem("LED");
        menu4.getItems().addAll(ledM);

        Menu menu5 = new Menu("Help");
        MenuItem docM = new MenuItem("Documentation");
        menu5.getItems().addAll(docM);


        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu1,menu2,menu3,menu4,menu5);
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
        root.getChildren().add(canvas);

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


        timeline = new Timeline(new KeyFrame(Duration.millis(130), e -> {
            try {
                disp_frame();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        //Scene
        Scene scene = new Scene(root,640, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void disp_frame() throws IOException {
        pixelWriter = gc.getPixelWriter();
        pixelFormat = PixelFormat.getByteRgbInstance();

        //buffer = frames.get_frame();
        Path path = Paths.get("example.rgb");

        byte[] image = Files.readAllBytes(path);

        buffer = image;
        pixelWriter.setPixels(0, 25, FRAME_WIDTH, FRAME_HEIGHT, pixelFormat, buffer, 0, FRAME_WIDTH * 3);
    }
}
