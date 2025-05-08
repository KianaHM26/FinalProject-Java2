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

public class HelloApplication extends Application {

    private Exam myExam;
    private Label labelShowGrades = new Label("Grade: ");
    private List<ToggleGroup> toggleGroups = new ArrayList<>();

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
                questionVBoxes[i] = buildTrueFalseQ((TFQuestion) q);
            } else {
                questionVBoxes[i] = buildMCQ((MCQuestion) q);
            }
        }

        return questionVBoxes;
    }

    private VBox buildTrueFalseQ(TFQuestion tfq) {
        Label questionLabel = new Label(tfq.getQuestionText());
        RadioButton trueBtn = new RadioButton("True");
        RadioButton falseBtn = new RadioButton("False");

        ToggleGroup group = new ToggleGroup();
        trueBtn.setToggleGroup(group);
        falseBtn.setToggleGroup(group);
        toggleGroups.add(group);

        HBox options = new HBox(10, trueBtn, falseBtn);
        VBox box = new VBox(10, questionLabel, options, new Separator());
        return box;
    }

    private VBox buildMCQ(MCQuestion mcq) {
        Label questionLabel = new Label(mcq.getQuestionText());
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
        menuBar.getMenus().addAll(
                new Menu("File"),
                new Menu("Edit"),
                new Menu("Quiz"),
                new Menu("Extras"),
                new Menu("Help")
        );
        return menuBar;
    }

    private void saveExamAnswers() {
        // Implement saving functionality as needed
        System.out.println("Answers saved (functionality not implemented)");
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
