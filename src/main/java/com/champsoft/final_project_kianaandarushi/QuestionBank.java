package com.champsoft.final_project_kianaandarushi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class QuestionBank {

    protected LinkedList<Question> questions;


    public QuestionBank() {
        this.questions = new LinkedList<>();
    }

    public QuestionBank(LinkedList<Question> questionsParam) {
        this.questions = new LinkedList<>(questionsParam);
    }


    public void printAllQuestions() {
        for (Question q : this.questions) {
            System.out.println(q);
        }
    }


    public void readMCQ(String fname) {

        try {
            File fileObj = new File(fname);
            Scanner scObj = new Scanner(fileObj);

            LinkedList<String> options = new LinkedList<>();
            while (scObj.hasNextLine()) {
                String line = scObj.nextLine().trim();
                String questionText = line.substring(3).trim();

                options.clear();//important otherwise options will be appended to old options
                String opt = "";
                String option = "";

                do {
                    line = scObj.nextLine().trim();
//                    System.out.println("option text: " + option);
                    opt = line.substring(0, 3);
//                    System.out.println("Option from 0 to 2: " + opt);
                    if (!opt.equals("ANS")) {
                        option = line.substring(2).trim();
                        options.add(option);
                    }
                } while (!opt.equals("ANS"));
                String correctAnswer = line.substring(4).trim();
//                System.out.println("correct answer: " + correctAnswer);
                MCQuestion mcQuestion = new MCQuestion(questionText, correctAnswer, QuestionType.MCQ, options);

                this.questions.add(mcQuestion);
//                System.out.println(mcQuestion);
//                    System.out.println(line);
            }


        } catch (FileNotFoundException e) {
            System.out.println("Error: file not found: " + fname);
        }
    }

    public void readTFQ(String fname) {

        try {
            File fileObj = new File(fname);
            Scanner scObj = new Scanner(fileObj);

            while (scObj.hasNext()) {
                String line = scObj.nextLine().trim();
                String questionText = line.substring(3).trim();

                line = scObj.nextLine().trim();
                String correctAnswer = line.substring(4).trim();
//                System.out.println("correct answer: " + correctAnswer);
                TFQuestion tfQuestion = new TFQuestion(questionText, correctAnswer, QuestionType.TFQ);

                this.questions.add(tfQuestion);
//                System.out.println(tfQuestion);
//                    System.out.println(line);
            }


        } catch (FileNotFoundException e) {
            System.out.println("Error: file not found: " + fname);
        }
    }


    public LinkedList<Question> selectRanQuestion(int[] randomIndeces) {
        LinkedList<Question> result = new LinkedList<>();
        int currentIndex = 0;
        try {

            for (int i : randomIndeces) {
                currentIndex = i;
                //get the question
               Question question = this.questions.get( i );
                //put it in the resutl / add it / append it
                result.add( question );
            }}catch(IndexOutOfBoundsException ex) {
                System.out.printf("index is out of bounds." ,this.questions.size(),currentIndex);
            }

        return result;
    }}



