package cam;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
//        inButton.setOnAction(e -> {
//            System.out.println("1");
//        });
//        helpButton.setOnAction(this::documentationWindow(primaryStage)
//        );
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
    public void documentationWindow(Stage primaryStage) {
        //Passing FileInputStream object as a parameter
        //TODO: Change Image to microscope output

        FileInputStream inputstream;
        try {
            inputstream = new FileInputStream("example.jpg");
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
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
        Scene scene = new Scene(root, 640, 720);

        //Stage
        Stage stage = primaryStage;
        primaryStage.setTitle("JavaFX App");

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
