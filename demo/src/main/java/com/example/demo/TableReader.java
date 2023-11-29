package com.example.demo;

//import com.eclipsesource.json*:

//JFileChooser j = new JFileChosser(currentDirectoryPath:"src")
//j.showOpenDialog (parent:null);
// sout (j.getSelectedFile().getPath());

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class TableReader extends Application {
    public static Scene scene;

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TableReader.class.getResource("hello-view.fxml"));
         scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.getIcons().add(new Image("https://media.licdn.com/dms/image/C4D0BAQF-qsaAE6rWUQ/company-logo_200_200/0/1631013397893/grit_academy_logo?e=2147483647&v=beta&t=X2Ylcpx_-DHZfjA8ZQ7niGbLIdogv8m7XnrImAxU1i4"));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}