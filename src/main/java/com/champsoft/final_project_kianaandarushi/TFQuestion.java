package com.champsoft.final_project_kianaandarushi;

public class TFQuestion extends Question {

    public TFQuestion() {
    }

    public TFQuestion(String questionText, String correctAnswer, QuestionType questionType) {
        super(questionText, correctAnswer, questionType);
    }

    @Override
    public String toString() {
        return "TFQuestion{" +  this.getQuestionText() + "}";
    }

}
