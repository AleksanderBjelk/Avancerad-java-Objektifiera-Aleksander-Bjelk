package com.example.demo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.stage.FileChooser;
import javafx.scene.control.TableView;
import com.eclipsesource.json.*;

import java.io.File;
import java.util.Scanner;

import static com.example.demo.TableReader.scene;

public class TableController {
    //observerable list
    //cellvaluefactory
    //Simple



    @FXML
    private TableView<JsonObject> Table;

    @FXML
    void onSaveButton(ActionEvent event) {

    }

    @FXML
    String selectFileFromExplorer(ActionEvent event) {
        FileChooser fc = new FileChooser(); //
        fc.setInitialDirectory(new File("src/main/java/com/example/demo"));
        File file = fc.showOpenDialog(scene.getWindow());
        if (file != null) {

            File temp = new File(file.getPath());
             System.out.println(file.getPath());
            System.out.println(file.getPath());

            Scanner scan = null;
            try {
                scan = new Scanner(temp);
            } catch (Exception e) {
                return null;
            }
            String data = "";
            while (scan.hasNext()){
                data += scan.next();
            }

            JsonValue jv = Json.parse(data);

            JsonArray ja = jv.asArray();


            JsonObject columnNames =ja.get(0).asObject();


            for (String cName : columnNames.names()){
                String val = columnNames.get(cName).asString();
                Table.getColumns().add(new TableColumn<>(val));


            }


            ObservableList<JsonObject>cells= FXCollections.observableArrayList();
            for (int i = 1; i < ja.size(); i++) {
            //String val = ja.get(i).asObject();

                JsonObject jo = ja.get(i).asObject();
                cells.add(jo);

            }

            Table.setItems(cells);

            /*for (int i = 0; i < ja.size()-1; i++) {
                JsonObject j= ja.get(i).asObject();
                System.out.println(j.get("Item"));
                System.out.println(j.get("Amount per unit"));
                System.out.println(j.get("Total amount"));
                System.out.println(j.get("Item"));
                System.out.println(j.get("Amount per unit"));
                System.out.println(j.get("Total amount"));
            }*/


            return data;
        }
        return null;
    }

    public static void processJson(String data){


    }
    public static void processCsv(String data){

    }

}

/* Källhantering, dels för att hitta så man kan läsa filer med Java FX men även hur jackson paketet funkar
https://central.sonatype.com/?smo=true
https://medium.com/javarevisited/using-the-jackson-library-to-persist-my-javafx-todo-list-to-json-8a4b31917c09
https://www.reddit.com/r/JavaFX/comments/wsejr5/read_a_json_file_to_a_tableview_in_javafx/
 */
