package cam;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        VBox vBox = createMenu();

        //Buttons
        Button helpButton   = createButton("?", 0, 480);
        Button rejectButton = createButton("Reject", 50, 480);
        Button saveButton   = createButton("Save", 100, 480);
        Button upButton     = createButton("Up", 0, 530);
        Button downButton   = createButton("Down", 50, 530);
        Button leftButton   = createButton("Left", 100, 530);
        Button rightButton  = createButton("Right", 150, 530);
        Button inButton     = createButton("Zoom In", 0, 580);
        Button outButton    = createButton("Zoom Out", 50, 580);
        Button rotateLeftButton     = createButton("Rotate Left", 100, 580);
        Button rotateRightButton    = createButton("Rotate Right", 150, 580);
        Button snapButton   = createButton("Snap", 300, 690);

        //Set actions
        inButton.setOnAction(e -> {
            System.out.println("1");
        });
        //Dropdown combo box
        Text filtersText = createText("Filters", 0, 630);
        ComboBox filtersBox = createFiltersList();

        //Sliders
        Text contrastText = createText("Contrast", 400, 530);
        Slider contrastSlider = createSlider(480, 520);

        Text brightnessText = createText("Brightness", 400, 580);
        Slider brightnessSlider = createSlider(480, 570);

        Text ledText = createText("Led", 400, 630);
        Slider ledSlider = createSlider(480, 620);


        //Creating a Group object
        Group root = new Group();
        root.getChildren().addAll(canvas, vBox, helpButton, rejectButton, saveButton, upButton, downButton, leftButton,
                rightButton, inButton, outButton, rotateRightButton, rotateLeftButton, filtersBox, filtersText,
                contrastSlider, contrastText, brightnessSlider, brightnessText, ledText, ledSlider, snapButton);


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
    private void zoomInImage(){
//        int newImageWidth = imageWidth * zoomLevel;
//        int newImageHeight = imageHeight * zoomLevel;
//        BufferedImage resizedImage = new BufferedImage(newImageWidth , newImageHeight, imageType);
//        Graphics2D g = resizedImage.createGraphics();
//        g.drawImage(originalImage, 0, 0, newImageWidth , newImageHeight , null);
//        g.dispose();
    }
    private VBox createMenu(){
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

        return vBox;
    }
    private Button createButton(String name, int x, int y){
        Button button = new Button(name);
        button.setLayoutX(x);
        button.setLayoutY(y);

        return button;
    }
    private Text createText(String name, int x, int y) {
        Text text = new Text();
        text.setText(name);
        text.setLayoutX(x);
        text.setLayoutY(y);
        return text;
    }
    private ComboBox createFiltersList(){

        ObservableList<String> filtersList =
                FXCollections.observableArrayList(
                        "Filter 1",
                        "Filter 2",
                        "Filter 3"
                );
        final ComboBox filtersBox = new ComboBox(filtersList);
        filtersBox.setLayoutX(50);
        filtersBox.setLayoutY(630);
        return filtersBox;
    }
    private Slider createSlider(int x, int y){
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(100);
        slider.setValue(40);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(50);
        slider.setMinorTickCount(5);
        slider.setBlockIncrement(10);
        slider.setLayoutX(x);
        slider.setLayoutY(y);
        return slider;
    }
}
