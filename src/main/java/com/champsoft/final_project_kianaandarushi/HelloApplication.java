package com.champsoft.final_project_kianaandarushi;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;



//Start of my code:

public class HelloApplication extends Application {

    private Exam myExam;
    private Label labelShowGrades = new Label("Grade: ");
    private List<ToggleGroup> toggleGroups = new ArrayList<>();

    @Override
    public void start(Stage stage) throws IOException {
        QuestionBank myBank = new QuestionBank();
        myBank.readMCQ("src/main/resources/mcq.txt");
        myBank.readTFQ("src/main/resources/tfq.txt");

        List<Integer> indxesList = new ArrayList<>();
        // Randomly select 10 question indexes from 1 to 14
        for (int i = 0; i < 10; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(1, 15);
            indxesList.add(randomIndex);
        }

        int[] indexes = indxesList.stream().mapToInt(Integer::intValue).toArray();
        LinkedList<Question> examQuestions = myBank.selectRanQuestion(indexes);
        myExam = new Exam(examQuestions);

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(10));
        contentBox.getChildren().add(buildMenuBar());
        contentBox.getChildren().add(buildBanner());
        contentBox.getChildren().add(buildGradeLabel());
        contentBox.getChildren().add(new Separator());

        // Add questions
        VBox[] questionBoxes = buildQuestionBoxes();
        contentBox.getChildren().addAll(questionBoxes);
        contentBox.getChildren().add(new Separator());
        contentBox.getChildren().add(buildFooter());


        // Wrap the VBox in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);

        Scene scene = new Scene(scrollPane, 800, 800);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setTitle("ChampExamen");
        stage.setScene(scene);
        stage.show();
    }

    private VBox buildGradeLabel() {
        HBox gradeBox = new HBox(10, new Label(""), labelShowGrades);
        gradeBox.setAlignment(Pos.CENTER);
        VBox container = new VBox(gradeBox);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    // Builds VBox containers for each question
    private VBox[] buildQuestionBoxes() {
        int numQuestions = myExam.getQuestions().size();
        VBox[] questionVBoxes = new VBox[numQuestions];

        for (int i = 0; i < numQuestions; i++) {
            Question q = myExam.getQuestion(i + 1);
            if (q.getQuestionType() == QuestionType.TFQ) {
                questionVBoxes[i] = buildTrueFalseQ(i + 1, (TFQuestion) q);
            } else {
                questionVBoxes[i] = buildMCQ(i + 1, (MCQuestion) q);
            }
        }
        return questionVBoxes;
    }


    // Build UI for a TF question
    private VBox buildTrueFalseQ(int questionNumber, TFQuestion tfq) {
        Label questionLabel = new Label("Q" + questionNumber + ": " + tfq.getQuestionText());
        RadioButton trueBtn = new RadioButton("T");
        RadioButton falseBtn = new RadioButton("F");

        ToggleGroup group = new ToggleGroup();
        trueBtn.setToggleGroup(group);
        falseBtn.setToggleGroup(group);
        toggleGroups.add(group);

        HBox options = new HBox(10, trueBtn, falseBtn);
        VBox box = new VBox(10, questionLabel, options, new Separator());
        return box;
    }

    private VBox buildMCQ(int questionNumber, MCQuestion mcq) {
        Label questionLabel = new Label("Q" + questionNumber + ": " + mcq.getQuestionText());
        VBox box = new VBox(10, questionLabel);
        ToggleGroup group = new ToggleGroup();
        toggleGroups.add(group);

        for (String option : mcq.getOptions()) {
            RadioButton rb = new RadioButton(option);
            rb.setToggleGroup(group);
            box.getChildren().add(rb);
        }

        box.getChildren().add(new Separator());
        return box;
    }

    private HBox buildFooter() {
        Button clearBtn = new Button("Clear");
        Button saveBtn = new Button("Save");
        Button submitBtn = new Button("Submit");

        clearBtn.setOnAction(e -> clearExamAnswers());
        saveBtn.setOnAction(e -> saveExamAnswers());
        submitBtn.setOnAction(new SubmitEventHandler());

        HBox box = new HBox(20, clearBtn, saveBtn, submitBtn);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private HBox buildBanner() {
        //images given on the top of the page
        Image logo = new Image("logo.png");
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(200);
        logoView.setPreserveRatio(true);
        logoView.setFitHeight(200);

        Image banner = new Image("banner.png");
        ImageView viewBanner = new ImageView(banner);

        viewBanner.setFitHeight(2000);
        viewBanner.setPreserveRatio(true);
        viewBanner.setFitWidth(300);
        HBox hBox = new HBox(10, logoView, viewBanner);

        return hBox;
    }

    private MenuBar buildMenuBar() {
        MenuBar menuBar = new MenuBar();

        // File Menu
        Menu fileMenu = new Menu("File");
        MenuItem saveItem = new MenuItem("Save Answers");
        MenuItem clearItem = new MenuItem("Clear Answers");
        MenuItem exitItem = new MenuItem("Exit");

        saveItem.setOnAction(e -> saveExamAnswers());
        clearItem.setOnAction(e -> clearExamAnswers());
        exitItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().addAll(saveItem, clearItem, new SeparatorMenuItem(), exitItem);

        // Edit Menu (disabled placeholder items)
        Menu editMenu = new Menu("Edit");
        MenuItem undoItem = new MenuItem("Undo");
        MenuItem redoItem = new MenuItem("Redo");
        undoItem.setDisable(true);
        redoItem.setDisable(true);
        editMenu.getItems().addAll(undoItem, redoItem);

        // Quiz Menu
        Menu quizMenu = new Menu("Quiz");
        MenuItem submitItem = new MenuItem("Submit Quiz");
        submitItem.setOnAction(new SubmitEventHandler());
        quizMenu.getItems().add(submitItem);

        // Extras Menu (placeholder)
        Menu extrasMenu = new Menu("Extras");
        MenuItem shuffleItem = new MenuItem("Shuffle Questions");
        shuffleItem.setDisable(true);
        extrasMenu.getItems().add(shuffleItem);

        // Help Menu
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAboutDialog());
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, editMenu, quizMenu, extrasMenu, helpMenu);
        return menuBar;
    }

    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About ChampExamen");
        alert.setHeaderText("ChampExamen v1.0");
        alert.setContentText("Developed by Kiana and Arushi (if you have questions ask Haikel, cuz we have no idea(IT IS A JOKE))\n\nA JavaFX app for " +
                "testing multiple-choice and true/false questions.");
        alert.showAndWait();
    }

    private void saveExamAnswers() {
        for (int i = 0; i < myExam.getQuestions().size(); i++) {
            ToggleGroup group = toggleGroups.get(i);
            Toggle selected = group.getSelectedToggle();
            if (selected != null) {
                String answer = ((RadioButton) selected).getText().trim();
                myExam.setSubmittedAnswers(i + 1, answer);
            }
        }
    }

    private void clearExamAnswers() {
        for (ToggleGroup group : toggleGroups) {
            group.selectToggle(null);
        }
        myExam.clear();
        labelShowGrades.setText("Grade: ");
    }

    private class SubmitEventHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            int grade = 0;
            int total = myExam.getQuestions().size();

            for (int i = 0; i < total; i++) {
                ToggleGroup group = toggleGroups.get(i);
                Toggle selected = group.getSelectedToggle();

                if (selected != null) {
                    String answer = ((RadioButton) selected).getText().trim();
                    myExam.setSubmittedAnswers(i + 1, answer);
                }

                String submitted = myExam.getSubmittedAnswers().get(i + 1);
                String correct = myExam.getQuestion(i + 1).getCorrectAnswer();

                if (submitted != null && submitted.equalsIgnoreCase(correct)) {
                    grade++;
                }
            }

            myExam.setGrade(grade);
            labelShowGrades.setText(String.format("%d out of %d (%.2f%%)", grade, total, (100.0 * grade / total)));
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
