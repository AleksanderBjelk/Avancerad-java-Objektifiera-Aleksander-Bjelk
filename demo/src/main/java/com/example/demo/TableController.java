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
    void buttonOnclick(ActionEvent event) {
        FileChooser fc = new FileChooser(); //
        fc.setInitialDirectory(new File("src/main/java/com/example/demo"));
        fc.showOpenDialog(scene.getWindow());
    File file = fc.showOpenDialog(scene.getWindow());
        if (file != null) {
        System.out.println(file.getPath());

    } else  {
        System.out.println("Error");
        }
    }
}

/* Källhantering, dels för att hitta så man kan läsa filer med Java FX men även hur jackson paketet funkar
https://central.sonatype.com/?smo=true
https://medium.com/javarevisited/using-the-jackson-library-to-persist-my-javafx-todo-list-to-json-8a4b31917c09
https://www.reddit.com/r/JavaFX/comments/wsejr5/read_a_json_file_to_a_tableview_in_javafx/
 */
