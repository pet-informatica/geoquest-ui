package br.ufpe.cin.pet.geoquest.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable{
	
	private String question;
    private String exam;
    private ArrayList<String> alternatives;
    private String correctAnswer;
    private int id;
	
	public Question(){}

    public Question(String question, String exam, ArrayList<String> alternatives, String correctAnswer, int id) {
        this.question = question;
        this.exam = exam;
        this.alternatives = alternatives;
        this.correctAnswer = correctAnswer;
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public ArrayList<String> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(ArrayList<String> alternatives) {
        this.alternatives = alternatives;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getId() {
        return this.id;
    }
}
