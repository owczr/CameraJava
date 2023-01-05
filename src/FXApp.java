import cam.Frames;
import javafx.application.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.nio.ByteBuffer;
import java.util.*;

import javafx.concurrent.*;

import javafx.beans.value.*;


class P_move
 {
  int x, y;
  
  public P_move()
   {
    x = 10;
    y = 10;
   }
 }
 
class G_task extends Task<P_move>
 {
  P_move p_move;
    
  public G_task()
   {   
    this.p_move = new P_move();     
   }
  
  @Override
  protected P_move call() throws Exception
   {
    int i = 0;
    
    while(true) 
     {
      System.out.println("Task's call method");
   
      p_move.x = 10 + i;
      p_move.y = 10 + i;
            
      updateValue(null); 
      updateValue(p_move);  

      System.out.println("i=" + i);
      
      i++;
      
      System.out.println("x = " +  p_move.x + "y = " +  p_move.y); 
      
      
      if(i == 10) 
       {
        updateValue(null); 
        break;
       }
     
            
      try { Thread.sleep(1000);  System.out.println("sleep method");    }
      catch (InterruptedException ex) 
       {
        System.out.println("catch method");
        break; 
       }
     }
    
    
     return p_move;
    }
 }

 class Game_service extends Service<P_move>
  {
  
   Task t; 
   
   public Game_service()
    {
   
    }
   
   protected Task createTask() 
    {
     t = new G_task();  
   
     return t;
  
    }
 
   }
 
 
 


public class JavaFXApp extends Application implements ChangeListener<P_move> 
 {

  private static final int FRAME_WIDTH  = 640;
  private static final int FRAME_HEIGHT = 480;


  GraphicsContext gc;
  Canvas canvas;
  byte buffer[];
  PixelWriter pixelWriter;
  PixelFormat<ByteBuffer> pixelFormat;

  Frames frames;

  Stage stage;

  ImageView imageView = new ImageView("map.jpg");
   ImageView imageView2 = new ImageView("cars.jpg");

    
  public static void main(String[] args) {
            launch(args);
	        }

@Override
  public void start(Stage primaryStage) {

  primaryStage.setTitle("Microscope");
  
  stage = primaryStage;


//  canvas = new Canvas(FRAME_WIDTH, FRAME_HEIGHT);

//  gc = canvas.getGraphicsContext2D();
//  gc.drawImage(image, 0, 0, FRAME_WIDTH, FRAME_HEIGHT);

//  root.getChildren().add(canvas);

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

  Group root = new Group();

  root.getChildren().add(imageView);
  root.getChildren().add(vBox);

  Scene scene = new Scene(root, FRAME_WIDTH, FRAME_HEIGHT);
							
  primaryStage.setScene(scene);

  //TODO: setOnAction handlers (menu1-menu5)
  snapM.setOnAction(this::second_window);
  startM.setOnAction(this::start_camera);
  deviceM.setOnAction(this::choose_camera);

  zoomMI.setOnAction(this::zoom_in);
  zoomMO.setOnAction(this::zoom_out);

  moveD.setOnAction(this::move_down);
  moveU.setOnAction(this::move_up);
  moveL.setOnAction(this::move_left);
  moveR.setOnAction(this::move_right);

  filter1.setOnAction(this::filter1);
  filter2.setOnAction(this::filter2);

  ledM.setOnAction(this::led);

  docM.setOnAction(this::doc);

//  snapM.setOnAction(e -> {
//                                      System.out.println("Snapshot Selected");
//                                      second_window();
////                              exit_dialog();
//
//                                      });
  
  primaryStage.setOnCloseRequest(e -> {
                                       e.consume();
                                       exit_dialog();
                                      });
  
  primaryStage.show();
  
 }

  //TODO: funkcjonalnosc menu
  //Tools - LED
   private void led(ActionEvent actionEvent) {
   }
  //Help - doc
   private void doc(ActionEvent actionEvent) {
   }

   //Filters
   private void filter2(ActionEvent actionEvent) {
   }
   private void filter1(ActionEvent actionEvent) {
   }

  //Edit - move
   private void move_down(ActionEvent actionEvent) {
   }
   private void move_up(ActionEvent actionEvent) {
   }
   private void move_left(ActionEvent actionEvent) {
   }
   private void move_right(ActionEvent actionEvent) {
   }
   //Edit - zoom
   public void zoom_in(ActionEvent actionEvent){
     System.out.println("Zoom In Selected");
   }
   private void zoom_out(ActionEvent actionEvent) {
     System.out.println("Zoom Out Selected");
   }

   //Main
   private void choose_camera(ActionEvent actionEvent) {
   }

   private void start_camera(ActionEvent actionEvent) {
   }

   public void second_window(ActionEvent actionEvent) {
     final Stage secondaryStage = new Stage();
     secondaryStage.setTitle("Snapshot");

     Menu menu1 = new Menu("File");
     MenuItem saveM = new MenuItem("Save");
     MenuItem dcM = new MenuItem("Discard");
     MenuItem copyM = new MenuItem("Copy");
     menu1.getItems().addAll(saveM,dcM,copyM);

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

     Group root2 = new Group();
     root2.getChildren().add(imageView2);
     root2.getChildren().add(vBox);
     Scene scene2 = new Scene(root2, FRAME_WIDTH, FRAME_HEIGHT);
     secondaryStage.setScene(scene2);


     //TODO: funkcjonalosc (save,discard,copy)
     saveM.setOnAction(e -> {
       System.out.println("Save Selected");
       save_shot();
     });

     dcM.setOnAction(e-> {
       System.out.println("Discard Selected");
       discard_shot(secondaryStage);
     });

     copyM.setOnAction(e-> {
       System.out.println("Copy Selected");
       copy_shot();
     });

     //TODO: przekopiowane setOnAction (menu2-menu5)
     zoomMI.setOnAction(this::zoom_in);
     zoomMO.setOnAction(this::zoom_out);

     moveD.setOnAction(this::move_down);
     moveU.setOnAction(this::move_up);
     moveL.setOnAction(this::move_left);
     moveR.setOnAction(this::move_right);

     filter1.setOnAction(this::filter1);
     filter2.setOnAction(this::filter2);

     ledM.setOnAction(this::led);

     docM.setOnAction(this::doc);

     secondaryStage.show();
   }

   //TODO: Snapshot functions
   public void save_shot()
   {
     FileChooser fileChooser = new FileChooser();
     fileChooser.setTitle("Save snapshot");
     fileChooser.getExtensionFilters().addAll(
         new FileChooser.ExtensionFilter("All Files", "*.*"),
         new FileChooser.ExtensionFilter("JPG", "*.jpg"),
         new FileChooser.ExtensionFilter("PNG", "*.png"));
     //Opening a dialog box
     File file = fileChooser.showSaveDialog(stage);
     //Save snapshot
     if (file != null) {
       try {
         ImageIO.write(SwingFXUtils.fromFXImage(imageView2.getImage(),
             null), "jpg", file);
       } catch (IOException ex) {
         System.out.println(ex.getMessage());
       }
     }
   }

   public void discard_shot(Stage snapStage)
   {
     //TODO: czyszczenie buffora z snapshotem?

     Alert alert = new Alert(AlertType.CONFIRMATION,
         "Do you really want to discard the snapshot?.",
         ButtonType.YES, ButtonType.NO);

     alert.setResizable(true);
     alert.onShownProperty().addListener(e -> {
       Platform.runLater(() -> alert.setResizable(false));
     });

     Optional<ButtonType> result = alert.showAndWait();
     if (result.get() == ButtonType.YES)
     {
       snapStage.close();
       System.out.println("discard snapshot");
     }
     else
     {
     }

   }

   public void copy_shot()
   {
     Clipboard clipboard = Clipboard.getSystemClipboard();
     ClipboardContent content = new ClipboardContent();
     content.putImage(imageView2.getImage()); // the image you want, as javafx.scene.image.Image
     clipboard.setContent(content);
   }


   public void changed(ObservableValue<? extends P_move> observable,
                      P_move oldValue,
                      P_move newValue) 
   {
    if(newValue != null) System.out.println("changed method called, x = " + newValue.x + "y = " + newValue.y);  
    
    
    
   }
 
 

 public void item_1()
  {
   System.out.println("item 1");
  } 
 
 public void exit_dialog()
  {
   System.out.println("exit dialog");
   

   Alert alert = new Alert(AlertType.CONFIRMATION,
                           "Do you really want to exit the program?.", 
 			    ButtonType.YES, ButtonType.NO);

   alert.setResizable(true);
   alert.onShownProperty().addListener(e -> { 
                                             Platform.runLater(() -> alert.setResizable(false)); 
                                            });

  Optional<ButtonType> result = alert.showAndWait();
  if (result.get() == ButtonType.YES)
   {
    Platform.exit();
   } 
  else 
   {
   }

  }

}
