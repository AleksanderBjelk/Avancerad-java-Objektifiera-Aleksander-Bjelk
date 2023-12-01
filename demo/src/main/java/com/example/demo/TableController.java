package com.example.demo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.scene.control.TableView;
import com.eclipsesource.json.*;
import javafx.util.Callback;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


import static com.example.demo.TableReader.scene;

public class TableController {

    @FXML
    private TableView< JsonObject> Table;

    @FXML
    void onSaveButton(ActionEvent event) {

        System.out.println("The user is saving a file.");
        FileChooser fileC = new FileChooser();
        fileC.setInitialDirectory(new File("src/main/java/com/example/demo"));
        fileC.setTitle("Save File");

        fileC.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV", "*.csv"),
                new FileChooser.ExtensionFilter("Json", "*.json"),
                new FileChooser.ExtensionFilter("ALL FILES", "*.*")
        );

        File file = fileC.showSaveDialog(scene.getWindow());
        if (file != null) {
            writeFile(file);
        } else  {
            System.out.println("save canceled!"); // or something else
        }
    }

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

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

    @FXML
    public String selectFileFromExplorer(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("src/main/java/com/example/demo"));
        File file = fc.showOpenDialog(scene.getWindow());
        if (file != null) {
            String fileExtension = getFileExtension(file);

            if ("json".equalsIgnoreCase(fileExtension)) {
                JsonObject columnNames = processJson(file);
                if (columnNames != null) {
                    // Skapa TableColumn och lägg till i tabellen
                    // ...
                }
            } else if ("csv".equalsIgnoreCase(fileExtension)) {
                processCsv(file);
            }
        }
        return null;
    }


    //Straight up cancer. 11 timmar bara för denna metoden.
    public  JsonObject processJson(File file) {
        try {
            File temp = new File(file.getPath());
            Scanner scan = new Scanner(temp);
            String data = "";
            while (scan.hasNext()){
                data += scan.next();
            }

            JsonValue jv = Json.parse(data);
            JsonArray ja = jv.asArray();

            JsonObject columnNames = ja.get(0).asObject();
            for (String columnNameKey : columnNames.names()){
                String columnNameValue = columnNames.get(columnNameKey).asString();
                TableColumn tc = new TableColumn(columnNameValue);

                tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<JsonObject, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<JsonObject, String> cellData) {
                        String value = cellData.getValue().get(columnNameKey).asString();
                        return new SimpleStringProperty(value);
                    }
                });
                Table.getColumns().add(tc);
            }

            ObservableList<JsonObject> cells = FXCollections.observableArrayList();
            for (int i = 1; i < ja.size(); i++) {
                JsonObject jo = ja.get(i).asObject();
                cells.add(jo);
            }
            Table.setItems(cells);
            return columnNames;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }




    public static void processCsv(File file) {
           BufferedReader reader = null;
           String line = "";

           try {
               reader = new BufferedReader(new FileReader(file));
               while ((line = reader.readLine()) != null) {
                   String[] row = line.split(",");
                   for (String index : row) {
                       System.out.printf("%-10s", index);
                   }
                   System.out.println();
               }
           } catch (Exception e) {
               e.printStackTrace();
           } finally {
               try {
                   if (reader != null) {
                       reader.close();
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }
}

/* Källhantering, dels för att hitta så man kan läsa filer med Java FX men även hur jackson paketet funkar
https://central.sonatype.com/?smo=true
https://medium.com/javarevisited/using-the-jackson-library-to-persist-my-javafx-todo-list-to-json-8a4b31917c09
https://www.reddit.com/r/JavaFX/comments/wsejr5/read_a_json_file_to_a_tableview_in_javafx/

https://www.youtube.com/watch?v=zKDmzKaAQro brocode om CSV hantering eftersom mina paket jag ladda ner funkade inte oavsett hur jag gjorde
 */
