package com.champsoft.final_project_kianaandarushi;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class HelloApplication extends Application {
    private Exam myExam;
    @Override
    public void start(Stage stage) throws IOException {

        QuestionBank myBank = new QuestionBank();
        myBank.readMCQ("src/main/resources/mcq.txt");
        myBank.readTFQ("src/main/resources/tfq.txt");

        List<Integer> indxesList = new ArrayList<>(Arrays.asList(11, 0, 5, 10, 9, 8, 7, 6));


        for (int i = 0; i < 3; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(1, 15); // 15 is exclusive
            indxesList.add(randomIndex);
        }

        int[] indxes = indxesList.stream().mapToInt(Integer::intValue).toArray();
        LinkedList<Question> exam = myBank.selectRanQuestion(indxes);
        myExam = new Exam(exam);
        myExam.printAllQuestions();

        VBox root = new VBox();

        MenuBar menuBarMain = buildMenuBar();
        root.getChildren().add(menuBarMain);

        HBox hboxGrade = new HBox();
        hboxGrade.setAlignment(Pos.CENTER);
        hboxGrade.getChildren().add(new Label("Grade:"));

        VBox[] vboxQuestions = buildQuestionsVboxes();


        HBox hBoxBanner = buildBanner();
        root.getChildren().add(hBoxBanner);

        root.getChildren().add(hboxGrade);
        root.getChildren().add(new Separator());
        root.getChildren().addAll(vboxQuestions);
        root.getChildren().add(new Separator());

        HBox hBoxButtons = buildFooter();
        hBoxButtons.setAlignment(Pos.CENTER);
        root.getChildren().add(hBoxButtons);


        Label helloWorldLabel = new Label("Hello World");
        Scene scene = new Scene(root, 800, 800);
        stage.setTitle("ChampExamen (TM) application (C)");
        stage.setScene(scene);
        stage.show();
    }

    private VBox[] buildQuestionsVboxes() {
        int numberOfQuestionsInExam = myExam.getQuestions().size();
        VBox[] vBoxes = new VBox[numberOfQuestionsInExam];

        for (int i = 0; i<numberOfQuestionsInExam; i++){
            Question question = myExam.getQuestion(i+1);
            if (question.getQuestionType()==QuestionType.TFQ){
                vBoxes[i] = buildTrueFalseQ(1, (TFQuestion) question);
            } else { // it is MCQ
                vBoxes[i] = buildMCQ(2, (MCQuestion) question);
            }
        }
        return vBoxes;
    }

    public VBox buildTrueFalseQ(int questionNumber, TFQuestion tfQuestion1){
        Label labelQuestionText = new Label(tfQuestion1.getQuestionText());
        RadioButton radioButtonTrue = new RadioButton("True ");
        RadioButton radioButtonFalse = new RadioButton("False");
        HBox hBox = new HBox(10, radioButtonTrue, radioButtonFalse);
        VBox vBox = new VBox(labelQuestionText, hBox);
        vBox.getChildren().add(new Separator());
        return vBox;
    }

    public VBox buildMCQ(int questionNumber, MCQuestion mcqQuestion){
        Label labelQuestionText = new Label(mcqQuestion.getQuestionText());
        VBox vBox = new VBox(labelQuestionText);

        LinkedList<String> options = mcqQuestion.getOptions();
        ToggleGroup toggleGroup = new ToggleGroup();
        for (String s : options){
            RadioButton radioButton = new RadioButton(s);
            radioButton.setToggleGroup(toggleGroup);
            vBox.getChildren().add(radioButton);

        }
        vBox.getChildren().add(new Separator());
        return vBox;
    }

    private HBox buildBanner() {
        Image logoObj = new Image("/logo.png");
        ImageView imageViewLogo = new ImageView(logoObj);

        imageViewLogo.setPreserveRatio(true);
        imageViewLogo.setFitHeight(100);
        Image bannerObj = new Image("/banner.png");
        ImageView imageViewBanner = new ImageView(bannerObj);
        imageViewBanner.setPreserveRatio(true);
        imageViewBanner.setFitHeight(100);
        HBox hboxBanner = new HBox();
        hboxBanner.getChildren().addAll(imageViewLogo, imageViewBanner);
        return hboxBanner;
    }

    private HBox buildFooter() {
        HBox hboxFooter = new HBox(10);
        Button buttonClear = new Button("Clear");
        Button buttonSave = new Button("Save");
        Button buttonSubmit = new Button("Submit");

        // register to actions
        buttonClear.setOnAction(e -> clearExamAnswers());
        buttonSave.setOnAction(e -> saveExamAnswers());
        buttonSubmit.setOnAction(new SubmitEventHandler());

        hboxFooter.getChildren().addAll(buttonClear, buttonSave, buttonSubmit);
        return hboxFooter;
    }

    private void saveExamAnswers() {
    }

    private void clearExamAnswers() {
    }

    private MenuBar buildMenuBar() {
        MenuBar menuBarMain = new MenuBar();
        Menu menuFile = new Menu("File");
        Menu menuEdit = new Menu("Edit");
        Menu menuQuiz = new Menu("Quiz");
        Menu menuExtras = new Menu("Extras");
        Menu menuHelp = new Menu("Help");
        menuBarMain.getMenus().addAll(menuFile, menuEdit, menuQuiz, menuExtras, menuHelp);
        return menuBarMain;
    }

    public static void main(String[] args) {
        launch();

    }
    public static class SubmitEventHandler implements EventHandler<ActionEvent> {


        @Override
        public void handle(ActionEvent actionEvent) {
            System.out.println("Submit button clicked");
        }
    }
}