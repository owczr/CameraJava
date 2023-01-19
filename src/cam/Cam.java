package src.cam;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
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
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class Cam extends Application {
    private static final int FRAME_WIDTH = 640;
    private static final int FRAME_HEIGHT = 480;
    GraphicsContext gc;
    Canvas canvas;
    byte[] buffer;
    PixelWriter pixelWriter;
    PixelFormat<ByteBuffer> pixelFormat;

    double ZOOM = 1;
    int X = 0;
    int Y = 0;
    double BRIGHTNESS = 1;
    double CONTRAST = 1;
    double LED = 1;

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
        VBox vBox = createMenu(false);

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
        Button applyButton   = createButton("Apply", 0, 660);

        //Dropdown combo box
        Text filtersText = createText("Filters", 0, 630);
        ComboBox filtersBox = createFiltersList();

        //Set button actions
        helpButton.setOnAction(this::documentationWindow);
        rejectButton.setOnAction(this::discard_shot);
        saveButton.setOnAction(this::save_shot);
        upButton.setOnAction(this::move_up);
        downButton.setOnAction(this::move_down);
        leftButton.setOnAction(this::move_left);
        rightButton.setOnAction(this::move_right);
        inButton.setOnAction(this::zoom_in);
        outButton.setOnAction(this::zoom_out);
        rotateLeftButton.setOnAction(this::rotateLeft);
        rotateRightButton.setOnAction(this::rotateRight);
        snapButton.setOnAction(this::second_window);
        applyButton.setOnAction(e -> {
            filtersListAction(filtersBox.getValue().toString());
        });


        //Sliders
        Text contrastText = createText("Contrast", 400, 530);
        Slider contrastSlider = createSlider(480, 520);

        Text brightnessText = createText("Brightness", 400, 580);
        Slider brightnessSlider = createSlider(480, 570);

        Text ledText = createText("Led", 400, 630);
        Slider ledSlider = createSlider(480, 620);

        //Slider actions
        contrastSlider.setOnMouseDragged(e->{
            change_contrast(contrastSlider);
        });
        brightnessSlider.setOnMouseDragged(e->{
            change_brightness(brightnessSlider);
        });
        ledSlider.setOnMouseDragged(e->{
            change_led(ledSlider);
        });


        //Creating a Group object
        Group root = new Group();
        root.getChildren().addAll(canvas, vBox, helpButton, rejectButton, saveButton, upButton, downButton, leftButton,
                rightButton, inButton, outButton, rotateRightButton, rotateLeftButton, filtersBox, filtersText,
                contrastSlider, contrastText, brightnessSlider, brightnessText, ledText, ledSlider, snapButton,
                applyButton);

        timeline = new Timeline(new KeyFrame(Duration.millis(130), e -> {
            try {
                disp_frame(primaryStage, root);
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

    private void change_led(Slider slider) {
        System.out.println("Led slider");
        LED = slider.getValue();
        System.out.println(LED);
    }

    private void change_contrast(Slider slider) {
        System.out.println("Contrast slider");
        CONTRAST = slider.getValue();
        System.out.println(CONTRAST);
    }

    private void change_brightness(Slider slider) {
        System.out.println("Brightness slider");
        BRIGHTNESS = slider.getValue();
        System.out.println(BRIGHTNESS);
    }


    private void rotateRight(ActionEvent actionEvent) {
        System.out.println("rotateRight");
    }

    private void rotateLeft(ActionEvent actionEvent) {
        System.out.println("rotateLeft");
    }

    public void documentationWindow(javafx.event.ActionEvent actionEvent) {
        final Stage secondaryStage = new Stage();

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

        //Text
        Text text = createText("Welcome to documentation!", 10, 10);

        //Group
        Group root = new Group();
        root.getChildren().addAll(text);

        //Scene
        Scene scene = new Scene(root, 640, 720);

        //Stage
        secondaryStage.setTitle("Help");

        secondaryStage.setScene(scene);
        secondaryStage.show();
    }

    private void disp_frame(Stage primaryStage, Group root) throws IOException {
        pixelWriter = gc.getPixelWriter();
        pixelFormat = PixelFormat.getByteRgbInstance();

        // buffer = frames.get_frame();
        Path path = Paths.get("example.rgb");
        byte[] byteImage = Files.readAllBytes(path);
        buffer = byteImage;

        // Load frame to BufferedImage
        BufferedImage originalImage = create3ByteRGBImage(FRAME_WIDTH, FRAME_HEIGHT, new int[] {8, 8, 8},
                new int[] {0, 1, 2});
        originalImage.setData(Raster.createRaster(originalImage.getSampleModel(),
                new DataBufferByte(buffer, buffer.length), new Point() ) );

        // Set image brightness
        set_brightness(originalImage, (int) BRIGHTNESS);

        // Zoom image
        int newImageWidth = (int)(FRAME_WIDTH * ZOOM);
        int newImageHeight = (int)(FRAME_HEIGHT * ZOOM);
        BufferedImage resizedImage = new BufferedImage(newImageWidth , newImageHeight, 	TYPE_INT_RGB);
        BufferedImage croppedImage = resizedImage.getSubimage(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

        // Move image
        Graphics2D g = croppedImage.createGraphics();
        g.drawImage(originalImage, X, Y, newImageWidth , newImageHeight , null);
        g.dispose();

        // Add image to Group object
        Image image = SwingFXUtils.toFXImage(croppedImage, null);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        root.getChildren().add(imageView);
        }
    private BufferedImage create3ByteRGBImage(int width, int height, int[] nBits, int[] bOffs) {
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel colorModel =
                new ComponentColorModel(cs, nBits,
                        false, false,
                        Transparency.OPAQUE,
                        DataBuffer.TYPE_BYTE);
        WritableRaster raster =
                Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                        width, height,
                        width*3, 3,
                        bOffs, null);
        return new BufferedImage(colorModel, raster, false, null);
    }
    private BufferedImage set_brightness(BufferedImage originalImage, int brightnessValue){
        int[] rgb;
        for (int i = 0; i < originalImage.getWidth(); i++) {
            // Inner loop for height of image
            for (int j = 0; j < originalImage.getHeight(); j++) {
                rgb = originalImage.getRaster().getPixel(
                        i, j, new int[3]);
                // Using(calling) method 1
                int red = truncate(rgb[0] + brightnessValue);
                int green = truncate(rgb[1] + brightnessValue);
                int blue = truncate(rgb[2] + brightnessValue);
                int[] arr = { red, green, blue };
                // Using setPixel() method
                originalImage.getRaster().setPixel(i, j, arr);
            }
        }
    return originalImage;
    }
    private static int truncate(int value) {
        if (value < 0) {
            value = 0;
        }
        else if (value > 255) {
            value = 255;
        }
        return value;
    }
//private void disp_frame(Stage stage) throws IOException {
//    pixelWriter = gc.getPixelWriter();
//    pixelFormat = PixelFormat.getByteRgbInstance();
//
//    // buffer = frames.get_frame();
//
//    Image image = new Image("C:\\Users\\codete\\Studies\\mikroskop_java\\example.png");
//
//    ImageView imageView = new ImageView();
//    imageView.setImage(image);
//
//    Group root = new Group();
//    root.getChildren().add(imageView);
//
//    Scene scene = new Scene(root,640, 720);
//    stage.setScene(scene);
//    stage.show();
//    }
    private VBox createMenu(Boolean snapshot){
        Menu menu1;
        if(!snapshot) {
            menu1 = new Menu("Main");
            MenuItem snapM = new MenuItem("Snapshot");
            snapM.setOnAction(this::second_window);
            MenuItem startM = new MenuItem("Start");
            startM.setOnAction(this::start_camera);
            MenuItem deviceM = new MenuItem("Device");
            deviceM.setOnAction(this::choose_camera);
            menu1.getItems().addAll(snapM, startM, deviceM);
        }
        else{
            menu1 = new Menu("File");
            MenuItem saveM = new MenuItem("Save");
            MenuItem dcM = new MenuItem("Discard");
            MenuItem copyM = new MenuItem("Copy");
            menu1.getItems().addAll(saveM,dcM,copyM);
            saveM.setOnAction(this::save_shot);
            dcM.setOnAction(this::discard_shot);
            copyM.setOnAction(this::copy_shot);
        }

        Menu menu2 = new Menu("Edit");
        Menu zoomM = new Menu("Zoom");
        Menu moveM = new Menu("Move");
        menu2.getItems().addAll(zoomM,moveM);

        MenuItem zoomMI = new MenuItem("Zoom in");
        zoomMI.setOnAction(this::zoom_in);
        MenuItem zoomMO = new MenuItem("Zoom out");
        zoomMO.setOnAction(this::zoom_out);
        zoomM.getItems().addAll(zoomMI,zoomMO);

        MenuItem moveU = new MenuItem("Move up");
        moveU.setOnAction(this::move_up);
        MenuItem moveD = new MenuItem("Move down");
        moveD.setOnAction(this::move_down);
        MenuItem moveL = new MenuItem("Move left");
        moveL.setOnAction(this::move_left);
        MenuItem moveR = new MenuItem("Move right");
        moveR.setOnAction(this::move_right);
        moveM.getItems().addAll(moveU,moveD,moveL,moveR);

        Menu menu3 = new Menu("Filters");
        CheckMenuItem filter1 = new CheckMenuItem("Black&white");
        filter1.setOnAction(e -> {filter1();});
        CheckMenuItem filter2 = new CheckMenuItem("Negative");
        filter2.setOnAction(e -> {filter2();});
        menu3.getItems().addAll(filter1,filter2);

        Menu menu4 = new Menu("Tools");
        CheckMenuItem ledM = new CheckMenuItem("LED");
        ledM.setOnAction(this::led);
        menu4.getItems().addAll(ledM);

        Menu menu5 = new Menu("Help");
        MenuItem docM = new MenuItem("Documentation");
        docM.setOnAction(this::documentationWindow);
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
                        "Black&white",
                        "Negative"
                );
        final ComboBox filtersBox = new ComboBox(filtersList);
        filtersBox.setLayoutX(50);
        filtersBox.setLayoutY(630);
        return filtersBox;
    }
    private void filtersListAction(String listItem) {
        switch (listItem) {
            case "Black&white":
                filter1();
                break;
            case "Negative":
                filter2();
                break;
            default:
                break;
        }
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

    private void led(javafx.event.ActionEvent actionEvent) {
        System.out.println("led");
    }

    //Filters
    private void filter2() {
        System.out.println("filter2");
    }
    private void filter1() {
        System.out.println("filter1");
    }

    //Edit - move
    private void move_down(javafx.event.ActionEvent actionEvent) {
        System.out.println("move_down");
        Y += 5;
    }
    private void move_up(javafx.event.ActionEvent actionEvent) {
        System.out.println("move_up");
        Y -= 5;
    }
    private void move_left(javafx.event.ActionEvent actionEvent) {
        System.out.println("move_left");
        X -= 5;
    }
    private void move_right(javafx.event.ActionEvent actionEvent) {
        System.out.println("move_right");
        X += 5;
    }
    //Edit - zoom
    public void zoom_in(javafx.event.ActionEvent actionEvent){
        ZOOM += 0.1;
        System.out.println("Zoom In Selected");
//        BufferedImage originalImage = new BufferedImage(FRAME_WIDTH, FRAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
//        originalImage.setData(Raster.createRaster(originalImage.getSampleModel(), new DataBufferByte(buffer, buffer.length), new Point() ) );
//
//        int newImageWidth = FRAME_WIDTH * 2;
//        int newImageHeight = FRAME_HEIGHT * 2;
//
//        BufferedImage resizedImage = new BufferedImage(newImageWidth , newImageHeight, 	TYPE_INT_RGB);
//        Graphics2D g = resizedImage.createGraphics();
//        g.drawImage(originalImage, 0, 0, newImageWidth , newImageHeight , null);
//        g.dispose();
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        try {
//            ImageIO.write(resizedImage, "rgb", baos);
//        }
//        catch (java.io.IOException e){
//            e.printStackTrace();
//            return;
//        }
//        buffer = baos.toByteArray();

        //        int newImageWidth = imageWidth * zoomLevel;
//        int newImageHeight = imageHeight * zoomLevel;
//        BufferedImage resizedImage = new BufferedImage(newImageWidth , newImageHeight, imageType);
//        Graphics2D g = resizedImage.createGraphics();
//
//
    }
    private void zoom_out(javafx.event.ActionEvent actionEvent) {
        System.out.println("Zoom Out Selected");
        ZOOM -= 0.1;
    }

    //Main
    private void choose_camera(javafx.event.ActionEvent actionEvent) {
        System.out.println("choose_camera");
    }

    private void start_camera(javafx.event.ActionEvent actionEvent) {
        System.out.println("start_camera");
    }

    public void second_window(ActionEvent actionEvent) {
        FileInputStream inputstream;
        try {
            inputstream = new FileInputStream("example.jpg");
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        Image image = new Image(inputstream);

        //Setting image view
        ImageView imageView2 = new ImageView(image);


        final Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Snapshot");

        VBox vBox = createMenu(true);

        Group root2 = new Group();
        root2.getChildren().add(imageView2);
        root2.getChildren().add(vBox);
        Scene scene2 = new Scene(root2, FRAME_WIDTH, FRAME_HEIGHT);
        secondaryStage.setScene(scene2);

        //TODO: przekopiowane setOnAction (menu2-menu5)
//        zoomMI.setOnAction(this::zoom_in);
//        zoomMO.setOnAction(this::zoom_out);
//
//        moveD.setOnAction(this::move_down);
//        moveU.setOnAction(this::move_up);
//        moveL.setOnAction(this::move_left);
//        moveR.setOnAction(this::move_right);
//
//        filter1.setOnAction(this::filter1);
//        filter2.setOnAction(this::filter2);
//
//        ledM.setOnAction(this::led);
//
//        docM.setOnAction(this::documentationWindow);

        secondaryStage.show();
    }

    //TODO: Snapshot functions
    public void save_shot(ActionEvent actionEvent){
        System.out.println("save_shot");
    }

    public void discard_shot(ActionEvent actionEvent){
        System.out.println("discard_shot");
    }

    public void copy_shot(ActionEvent actionEvent){
        System.out.println("copy_shot");
    }

}
