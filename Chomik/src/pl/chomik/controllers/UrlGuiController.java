package pl.chomik.controllers;

import java.awt.Desktop;
import javafx.fxml.FXML;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import pl.chomik.app.DatabaseHandler;

public class UrlGuiController {
    @FXML
    private Label urlID = new Label();
    @FXML
    private Label urlCity = new Label();
    @FXML
    private Label urlValue = new Label();
    @FXML
    private Label urlField = new Label();
    @FXML
    private Label urlRooms = new Label();
    @FXML
    private TextArea urlContent = new TextArea();

    @FXML
    private Button urlClose = new Button();

    public void getData(ResultSet rs){
        try{
            while(rs.next()){
                urlID.setText(rs.getString("ITEMID"));
                urlCity.setText(rs.getString("CITY"));
                urlValue.setText(Integer.toString(rs.getInt("VALUE")));
                urlField.setText(Double.toString(rs.getDouble("FIELD")));
                urlRooms.setText(rs.getString("ROOMS"));
                urlContent.setText(rs.getString("URL"));
            }
        }catch (SQLException e){
            System.out.println("[ APP FAILED ] SQLException");
        }
    }

    @FXML
    private void urlBrowse_onAction(ActionEvent e){
        System.out.println("[ APP OK ] Otweranie adresu URL: '" + urlContent.getText() + "'");

        try{
            Desktop.getDesktop().browse(new URI(urlContent.getText()));
        }
        catch (URISyntaxException e1){
            System.out.println("[ APP FAILED ] URISyntaxException");
        }
        catch (java.io.IOException e1){
            System.out.println("[ APP FAILED ] IOException");
        }

        System.out.println("testbrowse");
    }

    @FXML
    private void urlClose_onAction(ActionEvent e){
        Stage stage = (Stage)urlClose.getScene().getWindow();
        stage.close();
    }
}
