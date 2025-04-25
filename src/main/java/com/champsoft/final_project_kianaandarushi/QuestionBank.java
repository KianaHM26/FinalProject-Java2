package com.champsoft.final_project_kianaandarushi;

import java.io.File;
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
    public void readMCQ(String fname){

        try {
            File fileObj = new File(fname);
            Scanner scObj = new Scanner(fileObj);

            LinkedList<String> options = new LinkedList<>();
            while (scObj.hasNextLine()) {
                String line = scObj.nextLine().trim();
                String questionText = line.substring(3).trim();

                options.clear();//important otherwise options will be appended to old options
                String opt = "";
                do {
                    line = scObj.nextLine().trim();//
                    String option = line.substring(2).trim();
                    System.out.println("option text: " + option);
                    opt = option.substring(0, 3);
                    System.out.println("Option from 0 to 2: " + opt);
                    if (!opt.equals("ANS"))
                        options.add(option);
                }while(opt.equals("ANS"));
                    String correctAnswer = line.substring(4).trim();
                System.out.println("correct answer: " + correctAnswer);
                    MCQuestion mcQuestion = new MCQuestion(questionText, correctAnswer,QuestionType.MCQ, options);

                    this.questions.add(mcQuestion);
                System.out.println(mcQuestion);
                    System.out.println(line);
                }



        }catch (Exception e){
            System.out.println("Error: file not found: " + fname);
        }
    }
    }

