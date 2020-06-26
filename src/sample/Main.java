package sample;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();
        Stage stage = new Stage();
        URL location = getClass().getResource("/ui/MainFXML.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fxmlLoader.load();
        stage.setTitle("IP数据报抓取");
        Scene scene = new Scene(root, 700, 534);
        stage.setScene(scene);
        MainController controller = fxmlLoader.getController();   //获取Controller的实例对象
        controller.Init();
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
