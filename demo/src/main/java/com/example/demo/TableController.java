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
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Scanner;

import static com.example.demo.TableReader.scene;

public class TableController {
    //observerable list
    //cellvaluefactory
    //Simple

    private static int rows = 0;


    @FXML
    private TableView<String> Table;

    @FXML
    void onSaveButton(ActionEvent event) {

        System.out.println("The user is saving a file."); // or something else
        FileChooser fileC = new FileChooser();
        fileC.setInitialDirectory(new File("src/main/java/com/example/demo")); // init path annars C
        fileC.setTitle("Save File");

        fileC.getExtensionFilters().addAll(
                // new FileChooser.ExtensionFilter("TABLE FILES", "*.csv", "*.json"),
                new FileChooser.ExtensionFilter("CSV", "*.csv"),
                new FileChooser.ExtensionFilter("Json", "*.json"),
                new FileChooser.ExtensionFilter("ALL FILES", "*.*")
        );

        File file = fileC.showSaveDialog(scene.getWindow());
        if (file != null) {
            System.out.println(file.getPath());
            writeFile(file);
        } else  {
            System.out.println("save canceled!"); // or something else
        }
    }
    private static ArrayList<String> columnFileValues= new ArrayList<>(), dataFileValues = new ArrayList<>() ;
    private void writeFile(File file) {

        try {
            System.out.println(file.getName());
            String[] sArray= file.getName().split("\\.");

            //String  fileExtention = file.getName().split("\\.")[1];
            String  fileExtention = file.getName().split("\\.")[sArray.length-1];
            System.out.println(fileExtention);

            file.createNewFile(); //empty file med namn

             if(fileExtention.equals("csv")){

            } else if (fileExtention.equals("json")) {

            }

            switch (fileExtention) {
                case "json":
                    System.out.println("Json file");
                    break;
                case "csv":
                    System.out.println("CSV file");
                    break;
                default:
                    System.out.println("Other files");
            }

            switch (fileExtention){
                case "csv"-> {
                    System.out.println("Please wirte CSV here:");
                    PrintWriter w = new PrintWriter(file);
                    w.println("Förnamn,Efternamn,age");
                    w.close();
                }
                case "json"-> System.out.println("Please write Json here:");
                case "txt"-> {
                    //fyller i data
                    PrintWriter writer = new PrintWriter(file.getPath(), "UTF-8");
                    writer.println("detta är en text fil");
                    writer.println("by: Aleksander Bjelk, credit due to Alrik He for the code.");
                    writer.close();
                }
            }
        }catch (IOException io){
            System.out.println(io);
        }
    }




    @FXML
    String selectFileFromExplorer(ActionEvent event) {
        FileChooser fc = new FileChooser(); //
        fc.setInitialDirectory(new File("src/main/java/com/example/demo"));
        File file = fc.showOpenDialog(scene.getWindow());
        rows = 0;
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



            columnFileValues.addAll(columnNames.names());
            ObservableList<String> cells = FXCollections.observableArrayList();

            for (int i = 1; i < ja.size(); i++) {
                JsonObject jo = ja.get(i).asObject();
                //StringBuilder rowData = new StringBuilder();
                String row = "";
                for (int j = 0; j < columnFileValues.size(); j++) {

                    String val = String.valueOf(jo.get(columnFileValues.get(j)));
                    row += val;
                    row += ",";
                    System.out.println(val);
                    //rowData.append(val).append(", ");
                    cells.add(val);// Or any separator you prefer
                }
                cells.add(row);
                System.out.println(row);
            }
            System.out.println(cells);
            Table.setItems(cells);

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
