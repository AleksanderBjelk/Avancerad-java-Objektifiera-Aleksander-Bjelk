package com.example.demo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.stage.FileChooser;
import javafx.scene.control.TableView;
import com.eclipsesource.json.*;
import javafx.util.Callback;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;


import static com.example.demo.TableReader.scene;

public class TableController {

    @FXML
    private TableView< JsonObject> table; //en tableview som visar Json data

    @FXML
    private TableView<String[]> csvTable;


    //en knapp för att spara data
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

    //metod för att skriva data till en fil
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

    //metod för att välja en fil genom filutforskaren.
    public static String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

    //behandlar då den filen som en Json fil eller csv fil
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
            //en temporär fil
            File temp = new File(file.getPath());
            Scanner scan = new Scanner(temp);
            String data = "";
            while (scan.hasNext()){
                data += scan.next();
            }

            //parsar
            JsonValue jv = Json.parse(data);
            JsonArray ja = jv.asArray();

            //hämtar dem första namnen för att få dem som kolumn
            JsonObject columnNames = ja.get(0).asObject();

            //skapar kolumner för TableView baserat på namnen i JSON
            for (String columnNameKey : columnNames.names()){
                String columnNameValue = columnNames.get(columnNameKey).asString();
                TableColumn tc = new TableColumn(columnNameValue);

                //definierar hur cellvärden ska hämtas och läggas till i kolumnenn
                tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<JsonObject, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<JsonObject, String> cellData) {
                        String value = cellData.getValue().get(columnNameKey).asString();
                        return new SimpleStringProperty(value);
                    }
                });
                //lägger till kolumnen i tableView
                table.getColumns().add(tc);
            }

            //skapar listan med alla json för att tableViewn
            ObservableList<JsonObject> cells = FXCollections.observableArrayList();
            for (int i = 1; i < ja.size(); i++) {
                JsonObject jo = ja.get(i).asObject();
                cells.add(jo);
            }

            //sätter in raderna
            table.setItems(cells);
            return columnNames;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }




    public  void processCsv(File file) {
        BufferedReader reader = null; //en buffer för att läsa innehålllet i filen
        String line = ""; //en sträng för att lagra varje rad i filen

        try {
            reader = new BufferedReader(new FileReader(file)); //öppnar en läsare
            String [] firstHeader = reader.readLine().split(",");
            for ( int j = 0; j< firstHeader.length; j++){
                int i = j;
                String columnNameKey=firstHeader[i];

                TableColumn tc = new TableColumn(columnNameKey);

                tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> cellData) {
                          String s=cellData.getValue()[i];
                        return new SimpleStringProperty(s);
                    }
                });
                //lägger till kolumnen i tableView
                csvTable.getColumns().add(tc);
            }


            System.out.println(Arrays.deepToString(firstHeader));
            ObservableList<String[]> cells = FXCollections.observableArrayList();

            //loopar geom hela filen rad för rad
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",",firstHeader.length); //splitar vid varje komma och lägger det i en array
                System.out.println(Arrays.deepToString(row));
                //loopar igenom varje värde i raden och skriver ut det på konsole
              /*  for (String index : row) {
                    System.out.printf("%-10s", index);
                }*/
                System.out.println();


                System.out.println(Arrays.deepToString(row) + " "+ row.length);
                    //cells.add(new String[]{"Alrik", "Aleksander"});
                    cells.add(row);


                    //System.err.println(Arrays.deepToString(row));


                //sätter in raderna
                csvTable.setItems(cells);

            }
        } catch (Exception e) {
            e.printStackTrace(); //hanterar eventuella fel genom att skriva ut dem på konsolen
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
