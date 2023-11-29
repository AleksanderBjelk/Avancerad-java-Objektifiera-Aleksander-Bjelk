package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.scene.control.TableView;

import java.io.File;

import static com.example.demo.TableReader.scene;

public class TableController {

    @FXML
    private TableView<?> Table;


    @FXML
    private Label welcomeText;

    @FXML
    void buttonOnclick(ActionEvent event) {
        FileChooser fc = new FileChooser(); //
        fc.setInitialDirectory(new File("src"));
        fc.showOpenDialog(scene.getWindow());

    }


}


