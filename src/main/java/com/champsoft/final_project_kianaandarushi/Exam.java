package com.champsoft.final_project_kianaandarushi;

import java.util.HashMap;
import java.util.LinkedList;

public class Exam {


    private HashMap<Integer, Question> questions;
    private HashMap<Integer,String>submittedAnswers;

    public Exam() {
        this.questions = new HashMap<>();
        this.submittedAnswers = new HashMap<>();
    }
    public Exam (LinkedList<Question> qList){
        this.questions = new HashMap<>();
        this.submittedAnswers = new HashMap<>();
        for(int i =0 ; i<qList.size(); i++){
            Question question = qList.get(i);
            this.questions.put(i+1, question);
        }
    }
    public void clear(){
        this.questions.clear();
        this.submittedAnswers.clear();
    }
    public Question getQuestion(int i){
        return this.questions.get(i);
    }
    public String getsubmittedAnswer(int i){
        return this.submittedAnswers.get(i+1);
    }
    public void printAllQuestions(){
        for(int key: this.questions.keySet()){
            Question q = this.questions.get(key);
            System.out.println(key + "-->"+ q);
        }
    }
    public void setAnswerForOneQuestion(int questionNumber, String answer){
        this.submittedAnswers.put(questionNumber, answer);
    }


    public Exam(HashMap<Integer, Question> questions, HashMap<Integer, String> submittedAnswers) {
        this.questions = new HashMap<>();
        this.questions.putAll(questions);
        this.submittedAnswers =new HashMap<>();
    }

    public HashMap<Integer, Question> getQuestions() {
        return questions;
    }

    public void setQuestions(HashMap<Integer, Question> questions) {
        this.questions = questions;
    }

    public HashMap<Integer, String> getSubmittedAnswers() {
        return submittedAnswers;
    }

    public void setSubmittedAnswers(HashMap<Integer, String> submittedAnswers) {
        this.submittedAnswers = submittedAnswers;
    }

    public int getNumberOfQuestions(){
        return this.questions.size()+1;
    }
}
