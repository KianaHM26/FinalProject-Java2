package com.champsoft.final_project_kianaandarushi;

import java.util.LinkedList;

public class MCQuestion extends Question {

    private LinkedList<String>options;

    public MCQuestion(LinkedList<String> optionsParam) {
        super();
        this.options = new LinkedList<>();
        options.addAll(optionsParam);
    }

    public MCQuestion() {
        super();
    }

    public MCQuestion(String questionText, String correctAnswer, QuestionType questionType, LinkedList<String> optionsParam) {
        super(questionText, correctAnswer, questionType);
        this.options = new LinkedList<>();
        this.options.addAll(optionsParam);
    }

    public LinkedList<String> getOptions() {
        return options;
    }

    public void setOptions(LinkedList<String> optionsParam) {
        this.options = new LinkedList<>();
        this.options.addAll(optionsParam);
    }

    @Override
    public String toString() {
        return "MCQuestion{" + this.getQuestionText()+"\n"+
                "options=" + options + "\nAND: "+
                this.getCorrectAnswer()+
                '}';
    }
}
