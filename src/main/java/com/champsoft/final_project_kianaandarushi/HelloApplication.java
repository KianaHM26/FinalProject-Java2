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

import static javafx.application.Application.launch;

public class HelloApplication extends Application {

    private Exam myExam;
    private Label labelShowGrades = new Label("Grade: ");
    private List<ToggleGroup> toggleGroups = new ArrayList<>();
    private Map<Integer, ToggleGroup> questionToggleGroups = new HashMap<>();

    @Override
    public void start(Stage stage) throws IOException {
        // Initialize question bank and exam
        QuestionBank myBank = new QuestionBank();
        myBank.readMCQ("src/main/resources/mcq.txt");
        myBank.readTFQ("src/main/resources/tfq.txt");

        List<Integer> indxesList = new ArrayList<>(Arrays.asList(11, 0, 5, 10, 9, 8, 7, 6));
        for (int i = 0; i < 3; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(1, 15); // 15 is exclusive
            indxesList.add(randomIndex);
        }

        int[] indexes = indxesList.stream().mapToInt(Integer::intValue).toArray();
        LinkedList<Question> examQuestions = myBank.selectRanQuestion(indexes);
        myExam = new Exam(examQuestions);
        myExam.printAllQuestions();

        // Create the main content container
        VBox contentBox = new VBox();
        contentBox.setSpacing(10);
        contentBox.setPadding(new Insets(10));

        // Add components to contentBox
        contentBox.getChildren().add(buildMenuBar());
        contentBox.getChildren().add(buildBanner());
        contentBox.getChildren().add(buildGradeLabel());
        contentBox.getChildren().add(new Separator());

        VBox[] questionBoxes = buildQuestionBoxes();
        contentBox.getChildren().addAll(questionBoxes);
        contentBox.getChildren().add(new Separator());
        contentBox.getChildren().add(buildFooter());

        // Create and configure the ScrollPane
        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);

        // Set the ScrollPane as the root of the scene
        Scene scene = new Scene(scrollPane, 800, 800);
        stage.setTitle("ChampExamen");
        stage.setScene(scene);
        stage.show();
    }

    private VBox buildGradeLabel() {
        HBox gradeBox = new HBox(10, new Label("Grade:"), labelShowGrades);
        gradeBox.setAlignment(Pos.CENTER);
        VBox container = new VBox(gradeBox);
        container.setAlignment(Pos.CENTER);
        return container;
    }

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

    private VBox buildTrueFalseQ(int questionNumber, TFQuestion tfq) {
        Label questionLabel = new Label("Q" + questionNumber + ": " + tfq.getQuestionText());
        RadioButton trueBtn = new RadioButton("True");
        RadioButton falseBtn = new RadioButton("False");

        ToggleGroup group = new ToggleGroup();
        trueBtn.setToggleGroup(group);
        falseBtn.setToggleGroup(group);
        toggleGroups.add(group);
        questionToggleGroups.put(questionNumber, group);

        HBox options = new HBox(10, trueBtn, falseBtn);
        VBox box = new VBox(10, questionLabel, options, new Separator());
        return box;
    }

    private VBox buildMCQ(int questionNumber, MCQuestion mcq) {
        Label questionLabel = new Label("Q" + questionNumber + ": " + mcq.getQuestionText());
        VBox box = new VBox(10, questionLabel);
        ToggleGroup group = new ToggleGroup();
        toggleGroups.add(group);
        questionToggleGroups.put(questionNumber, group);

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
        ImageView logo = new ImageView(new Image("/logo.png"));
        logo.setFitHeight(100);
        logo.setPreserveRatio(true);

        ImageView banner = new ImageView(new Image("/banner.png"));
        banner.setFitHeight(100);
        banner.setPreserveRatio(true);

        HBox hbox = new HBox(10, logo, banner);
        return hbox;
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
        alert.setContentText("Developed by Kiana and Arushi\n\nA JavaFX app for testing multiple-choice and true/false questions.");
        alert.showAndWait();
    }

    private void saveExamAnswers() {
        int total = myExam.getQuestions().size();

        for (int i = 0; i < total; i++) {
            ToggleGroup group = toggleGroups.get(i);
            Toggle selected = group.getSelectedToggle();

            if (selected != null) {
                String answer = ((RadioButton) selected).getText().trim();
                myExam.setSubmittedAnswers(i + 1, answer);
            }
        }

        System.out.println("Answers saved.");
    }

    private void clearExamAnswers() {
        for (ToggleGroup group : toggleGroups) {
            group.selectToggle(null);
        }
        myExam.clear(); // Implement this method in Exam class
        labelShowGrades.setText("Grade: ");
    }

    private class SubmitEventHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            int total = myExam.getQuestions().size();
            int grade = 0;

            for (int i = 0; i < total; i++) {
                ToggleGroup group = toggleGroups.get(i);
                Toggle selected = group.getSelectedToggle();

                if (selected != null) {
                    String answer = ((RadioButton) selected).getText().trim();
                    myExam.setSubmittedAnswers(i + 1, answer);
                }

                Question q = myExam.getQuestion(i + 1);
                String submitted = myExam.getsubmittedAnswer(i + 1);
                String correct = q.getCorrectAnswer();

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
