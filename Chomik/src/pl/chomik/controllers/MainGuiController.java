package pl.chomik.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import pl.chomik.app.DatabaseHandler;
import pl.chomik.app.Home;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.*;

public class MainGuiController {
    ///////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////
    /////  METODA INICJALIZUJĄCA (ZAMIENNIK KONSTRUKTORA DLA JAVAFX)
    @FXML
    public void initialize(){
        SQLCONNECT();
        INIT_TABLEDATA();

        panelCityValue.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String valid = panelCityValue.getText();
                if (!valid.matches("[ąĄćĆęĘłŁńŃóÓśŚżŻźŹa-zA-Z-\\s]*")){
                    if(valid.length()>0) {
                        panelCityValue.setText(valid.substring(0, valid.length() - 1));
                    } else panelCityValue.setText("");
                    System.out.println("[ APP FAILED ] POLE MOŻE PRZYJMOWAĆ TYLKO LITERY");
                    mainAPPstatus.setText("[ APP FAILED ] Pole może przyjmować tylko litery");
                }
            }
        });

        panelValueMinValue.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String valid = panelValueMinValue.getText();
                if (!valid.matches("\\d+")){
                    if(valid.length()>0) {
                        panelValueMinValue.setText(valid.substring(0, valid.length() - 1));
                    } else panelValueMinValue.setText("");
                    System.out.println("[ APP FAILED ] POLE MOŻE PRZYJMOWAĆ TYLKO WARTOŚCI NUMERYCZNE");
                    mainAPPstatus.setText("[ APP FAILED ] Pole może przyjmować tylko wartości numeryczne");
                }
            }
        });

        panelValueMaxValue.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String valid = panelValueMaxValue.getText();
                if (!valid.matches("\\d+")){
                    if(valid.length()>0) {
                        panelValueMaxValue.setText(valid.substring(0, valid.length() - 1));
                    } else panelValueMaxValue.setText("");
                    System.out.println("[ APP FAILED ] POLE MOŻE PRZYJMOWAĆ TYLKO WARTOŚCI NUMERYCZNE");
                    mainAPPstatus.setText("[ APP FAILED ] Pole może przyjmować tylko wartości numeryczne");
                }
            }
        });

        panelFieldMinValue.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String valid = panelFieldMinValue.getText();
                if (!valid.matches("\\d+")){
                    if(valid.length()>0) {
                        panelFieldMinValue.setText(valid.substring(0, valid.length() - 1));
                    } else panelFieldMinValue.setText("");
                    System.out.println("[ APP FAILED ] POLE MOŻE PRZYJMOWAĆ TYLKO WARTOŚCI NUMERYCZNE");
                    mainAPPstatus.setText("[ APP FAILED ] Pole może przyjmować tylko wartości numeryczne");
                }
            }
        });

        panelFieldMaxValue.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String valid = panelFieldMaxValue.getText();
                if (!valid.matches("\\d+")){
                    if(valid.length()>0) {
                        panelFieldMaxValue.setText(valid.substring(0, valid.length() - 1));
                    } else panelFieldMaxValue.setText("");
                    System.out.println("[ APP FAILED ] POLE MOŻE PRZYJMOWAĆ TYLKO WARTOŚCI NUMERYCZNE");
                    mainAPPstatus.setText("[ APP FAILED ] Pole może przyjmować tylko wartości numeryczne");
                }
            }
        });

        panelRoomsValue.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String valid = panelRoomsValue.getText();
                if (!valid.matches("\\d+")){
                    if(valid.length()>0) {
                        panelRoomsValue.setText(valid.substring(0, valid.length() - 1));
                    } else panelRoomsValue.setText("");
                    System.out.println("[ APP FAILED ] POLE MOŻE PRZYJMOWAĆ TYLKO WARTOŚCI NUMERYCZNE");
                    mainAPPstatus.setText("[ APP FAILED ] Pole może przyjmować tylko wartości numeryczne");
                }
            }
        });

        homeTableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                if(homeTableView.getSelectionModel().getSelectedItem() != null){
                    uiOpenLinkWindow();
                }
            }
        });


        appProgressIndicator.setVisible(false);
    }



    ///////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////
    /////   BAZA DANYCH
    private Connection connect;
    private void SQLCONNECT(){
        appProgressIndicator.setVisible(true);

        connect = DatabaseHandler.Connector();
        if(connect!=null){
            setAppStatusText("[ DBH OK ] Połączono z bazą danych.");
            setDbStatusText("[connect:" + connect + "]");
            homeTableView.getItems().removeAll();
        }
        else{
            setAppStatusText("[ DBH FAILED ] Nieudana próba połączenia z bazą danych.");
            setDbStatusText("---");
        }

        appProgressIndicator.setVisible(false);
    }

    private void SQLCLOSE(){
        appProgressIndicator.setVisible(true);

        boolean isConnectClosed = DatabaseHandler.CloseConnection();
        if(isConnectClosed){
            setAppStatusText("[ DBH Rozłączono z bazą danych.");
            setDbStatusText("---");
        }

        appProgressIndicator.setVisible(false);
    }

    private boolean SQLQUERY(){
        appProgressIndicator.setVisible(true);

        boolean isfilledCity;
        boolean isfilledValueMin;
        boolean isfilledValueMax;
        boolean isfilledFieldMin;
        boolean isfilledFieldMax;
        boolean isfilledRooms;
        boolean firstNoAnd = false;
        int multiCriteria = 0;
        String executesql = "SELECT * FROM \"HOMES\" ";

        //////////////////////////////////////////////
        // SEKCJA SPRAWDZANIA KTORE POLA SA WYPELNIONE

        if(!panelCityValue.getText().equals("")){
            multiCriteria++;
            isfilledCity = true;
        }else isfilledCity = false;

        if(!panelValueMinValue.getText().equals("")){
            multiCriteria++;
            isfilledValueMin = true;
        }else isfilledValueMin = false;

        if(!panelValueMaxValue.getText().equals("")){
            multiCriteria++;
            isfilledValueMax = true;
        }else isfilledValueMax = false;

        if(!panelFieldMinValue.getText().equals("")){
            multiCriteria++;
            isfilledFieldMin = true;
        }else isfilledFieldMin = false;

        if(!panelFieldMaxValue.getText().equals("")){
            multiCriteria++;
            isfilledFieldMax = true;
        }else isfilledFieldMax = false;

        if(!panelRoomsValue.getText().equals("")){
            multiCriteria++;
            isfilledRooms = true;
        }else isfilledRooms = false;

        if(multiCriteria > 0) executesql = executesql + " WHERE ";

        ////////////////////////////////////
        // SEKCJA BUDOWANIA stringa SQLQUERY

        if (isfilledCity){
            if (!firstNoAnd) firstNoAnd = true;
            else executesql = SQLQUERYaddAND(executesql);
            executesql = executesql + " CITY='" + panelCityValue.getText() + "' ";
        }

        if (isfilledValueMin){
            if (!firstNoAnd) firstNoAnd = true;
            else executesql = SQLQUERYaddAND(executesql);
            executesql = executesql + " VALUE>=" + panelValueMinValue.getText() + " ";
        }

        if (isfilledValueMax){
            if (!firstNoAnd) firstNoAnd = true;
            else executesql = SQLQUERYaddAND(executesql);
            executesql = executesql + " VALUE<=" + panelValueMaxValue.getText() + " ";
        }

        if (isfilledFieldMin){
            if (!firstNoAnd) firstNoAnd = true;
            else executesql = SQLQUERYaddAND(executesql);
            executesql = executesql + " FIELD>=" + panelFieldMinValue.getText() + " ";
        }

        if (isfilledFieldMax){
            if (!firstNoAnd) firstNoAnd = true;
            else executesql = SQLQUERYaddAND(executesql);
            executesql = executesql + " FIELD<=" + panelFieldMaxValue.getText() + " ";
        }

        if (isfilledRooms){
            if (!firstNoAnd) firstNoAnd = true;
            else executesql = SQLQUERYaddAND(executesql);
            executesql = executesql + " ROOMS='" + panelRoomsValue.getText() + "' ";
        }

        try{
            System.out.println("[ DEBUG ] SQLEXECUTE '" + executesql + "'");
            ResultSet rs = DatabaseHandler.Query(executesql);

            homeTableView.getItems().clear();

            while(rs.next()){
                System.out.println("[ APP PARSE ] GETITEM -> "  + " " + rs.getString("ITEMID") + " " + rs.getString("CITY") + " " + rs.getInt("VALUE") + " " + rs.getDouble("FIELD") + " " + rs.getString("ROOMS") + " " + rs.getString("URL"));
                homeTableView.getItems().add(new Home(rs.getLong("ADVID"),rs.getString("ITEMID"),rs.getString("CITY"),rs.getInt("VALUE"),rs.getDouble("FIELD"),rs.getString("ROOMS"),rs.getString("URL")));
            }
            appProgressIndicator.setVisible(false);
            return true;
        }
        catch (SQLException e){
            System.out.println("[ DBH FAILED ] SQLException. NIE MOŻNA ZAKTUALIZOWAĆ DANYCH TABELI");
            appProgressIndicator.setVisible(false);
            return false;
        }
        catch (NullPointerException e){
            System.out.println("[ DBH FAILED ] BRAK DANYCH DO PRZETWORZENIA");
            appProgressIndicator.setVisible(false);
            return false;
        }
    }

    private String SQLQUERYaddAND(String executesql){
        return executesql + " AND ";
    }


    ///////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////
    /////   TABELA
    @FXML
    TableView homeTableView = new TableView();
    TableColumn itemidColumn = new TableColumn("Identyfikator ogłoszenia");
    TableColumn cityColumn = new TableColumn("Miejscowość");
    TableColumn valueColumn = new TableColumn("Cena");
    TableColumn fieldColumn = new TableColumn("Powierzchnia [m^2]");
    TableColumn roomsColumn = new TableColumn("Liczba pokoi");

    private void INIT_TABLEDATA(){
        itemidColumn.setCellValueFactory(new PropertyValueFactory<>("ITEMID"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("CITY"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("VALUE"));
        fieldColumn.setCellValueFactory(new PropertyValueFactory<>("FIELD"));
        roomsColumn.setCellValueFactory(new PropertyValueFactory<>("ROOMS"));
        homeTableView.getColumns().addAll(itemidColumn,cityColumn,valueColumn,fieldColumn,roomsColumn);
        itemidColumn.prefWidthProperty().setValue(200);
        cityColumn.prefWidthProperty().setValue(150);
        valueColumn.prefWidthProperty().setValue(100);
        fieldColumn.prefWidthProperty().setValue(170);
        roomsColumn.prefWidthProperty().setValue(170);
    }


    ///////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////
    /////   KONTROLKI MAINGUI
    @FXML
    private TextField panelCityValue = new TextField();     // POLE TEKSTOWE DLA WARTOŚCI -> MIASTO
    @FXML
    private TextField panelValueMinValue = new TextField(); // POLE TEKSTOWE DLA WARTOŚCI -> MINIMALNA CENA
    @FXML
    private TextField panelValueMaxValue = new TextField(); // POLE TEKSTOWE DLA WARTOŚCI ->
    @FXML
    private TextField panelFieldMinValue = new TextField(); // POLE TEKSTOWE DLA WARTOŚCI ->
    @FXML
    private TextField panelFieldMaxValue = new TextField(); // POLE TEKSTOWE DLA WARTOŚCI ->
    @FXML
    private TextField panelRoomsValue = new TextField();    // POLE TEKSTOWE DLA WARTOŚCI ->
    @FXML
    private Label mainAPPstatus = new Label();
    @FXML
    private Label mainDBstatus = new Label();
    @FXML
    private ProgressIndicator appProgressIndicator = new ProgressIndicator(); // POLE TEKSTOWE DLA WARTOŚCI ->


    ///////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////
    /////  METODY KONTROLEK
    @FXML
    private void menuFileExit_onAction(){
        System.out.println("[ DB OK ] ZAMYKANIE POŁĄCZENIA Z BAZĄ DANYCH");
        SQLCLOSE();
        System.out.println("[ APP OK ] ZAMYKANIE APLIKACJI");
        Platform.exit();
    }

    @FXML
    private void menuHelpAbout_onAction(ActionEvent e) throws Exception{
        Stage aboutStage = new Stage();
        Parent aboutGui = FXMLLoader.load(getClass().getResource("/pl/chomik/frames/AboutGuiFXML.fxml"));
        aboutStage.setHeight(400);
        aboutStage.setWidth(512);
        aboutStage.setResizable(false);
        aboutStage.setTitle("O projekcie");
        aboutStage.getIcons().add(new Image(AboutGuiController.class.getResourceAsStream("/pl/chomik/img/icon.png")));
        aboutStage.setScene(new Scene(aboutGui));
        aboutStage.show();
    }

    @FXML
    private void menuDatabaseReconnect_onAction(ActionEvent e){
        SQLCLOSE();
        SQLCONNECT();
    }

    @FXML
    private void menuFileNew_onAction(ActionEvent e){
        panelCityValue.setText("");
        panelValueMinValue.setText("");
        panelValueMaxValue.setText("");
        panelFieldMinValue.setText("");
        panelFieldMaxValue.setText("");
        panelRoomsValue.setText("");
        homeTableView.getItems().clear();
        SQLCLOSE();
        SQLCONNECT();
        setAppStatusText("[ APP OK ] Rozpoczęto nową sesję.");
        System.out.println("[ APP OK ] ROZPOCZĘTO NOWĄ SESJĘ");
    }

    @FXML
    private void menuClear_onAction(ActionEvent e){
        panelCityValue.setText("");
        panelValueMinValue.setText("");
        panelValueMaxValue.setText("");
        panelFieldMinValue.setText("");
        panelFieldMaxValue.setText("");
        panelRoomsValue.setText("");
        setAppStatusText("[ APP OK ] Wyczyszczono kryteria.");
        System.out.println("[ APP OK ] WYCZYSZCZONO KRYTERIA");
    }

    @FXML
    private void panelOpenlink_onAction(ActionEvent e){
        uiOpenLinkWindow();
    }

    private void uiOpenLinkWindow(){
        appProgressIndicator.setVisible(true);

        try{
            if(homeTableView.getSelectionModel().getSelectedItem() != null){
                Stage urlStage = new Stage();
                FXMLLoader urlLoader = new FXMLLoader(getClass().getResource("/pl/chomik/frames/UrlGuiFXML.fxml"));
                UrlGuiController urlController = new UrlGuiController();
                urlLoader.setController(urlController);
                Parent urlGui = urlLoader.load();
                urlStage.setHeight(200);
                urlStage.setWidth(700);
                urlStage.setResizable(false);
                urlStage.setTitle("Dane ogłoszenia");
                urlStage.getIcons().add(new Image(AboutGuiController.class.getResourceAsStream("/pl/chomik/img/icon.png")));
                urlStage.setScene(new Scene(urlGui));
                urlGui.getStylesheets().add("/pl/chomik/css/UrlCSS.css");

                Home home = (Home)homeTableView.getSelectionModel().getSelectedItem();
                String itemid = home.getITEMID();
                System.out.println(itemid);
                ResultSet rs = DatabaseHandler.Query("SELECT * FROM \"HOMES\" WHERE ITEMID='" + itemid + "';");
                urlController.getData(rs);

                urlStage.show();

                mainAPPstatus.setText("[ APP OK ] Otwieranie ogłoszenia");
                System.out.println("[ APP OK ] OTWIERANIE OGŁOSZENIA");
            }
            else{
                mainAPPstatus.setText("[ APP FAILED ] Musisz zaznaczyć ogłoszenie, aby je otworzyć");
                System.out.println("[ APP FAILED ] MUSISZ ZAZNACZYĆ OGŁOSZENIE, ABY JE OTWORZYĆ");
            }
        }
        catch (IOException e1){
            System.out.println("[ APP FAILED ] IOException");
        }


        appProgressIndicator.setVisible(false);
    }

    @FXML
    private void panelSearch_onAction(ActionEvent e){
        appProgressIndicator.setVisible(true);


        System.out.println("[ DBH REQUEST ] PRZETWARZANIE ZAPYTANIA . . .");
        mainAPPstatus.setText("[ DBH REQUEST ] Przetwarzanie zapytania . . .");

        if(SQLQUERY()){
            System.out.println("[ DBH OK ] WYKONANO");
            mainAPPstatus.setText("[ DBH OK ] Wykonano");
        }
        else{
            mainAPPstatus.setText("[ APP FAILED ] Błąd podczas zbierania danych");
        }


        appProgressIndicator.setVisible(false);
    }

    private void setAppStatusText(String text) {mainAPPstatus.setText(text);}
    private void setDbStatusText(String text) {mainDBstatus.setText(text);}
}
