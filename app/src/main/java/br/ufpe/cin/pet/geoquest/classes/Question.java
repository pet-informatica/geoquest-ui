package br.ufpe.cin.pet.geoquest.classes;

import java.util.ArrayList;
import java.util.Vector;

public class Question {
	
	private String question;
    private String exam;
    private ArrayList<String> alternatives;
    private String correctAnswer;
	
	public Question(){}

    public Question(String question, String exam, ArrayList<String> alternatives, String correctAnswer) {
        this.question = question;
        this.exam = exam;
        this.alternatives = alternatives;
        this.correctAnswer = correctAnswer;
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
}
