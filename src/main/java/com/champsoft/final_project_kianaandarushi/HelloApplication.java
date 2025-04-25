package com.champsoft.final_project_kianaandarushi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


import java.io.IOException;
import java.io.PrintWriter;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        QuestionBank myBank = new QuestionBank();
        myBank.readMCQ("C:\\Users\\Kiana\\IdeaProjects\\Final_Project_KianaAndArushi\\src\\main\\resources\\mcq.txt");



        Label lb1 = new Label("Hello World!!!");
        Scene scene = new Scene(lb1, 320, 240);
        stage.setTitle("Hello World!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}