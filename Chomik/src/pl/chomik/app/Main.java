package pl.chomik.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage mainStage) throws Exception{
        System.out.println("[ APP OK ] START > URUCHAMIANIE APLIKACJI");
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/pl/chomik/frames/MainGuiFXML.fxml"));

        //pl.chomik.controllers.MainGuiController mainGuiController = new pl.chomik.controllers.MainGuiController();

        //fxmlloader.setController(mainGuiController);
        Parent rootGui = fxmlloader.load();

        mainStage.setMinHeight(550);
        mainStage.setMinWidth(1000);
        mainStage.setHeight(550);
        mainStage.setWidth(1000);
        mainStage.setTitle("Chomik [otodom]");
        mainStage.getIcons().add(new Image(Main.class.getResourceAsStream("/pl/chomik/img/icon.png")));
        mainStage.setScene(new Scene(rootGui, 1000,550));
        rootGui.getStylesheets().add("pl/chomik/css/MainCSS.css");

        System.out.println("[ APP OK ] START > TWORZENIE GUI");
        mainStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
