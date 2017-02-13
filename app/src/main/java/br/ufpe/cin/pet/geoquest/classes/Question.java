package br.ufpe.cin.pet.geoquest.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable{
	
	private String question;
    private String exam;
    private ArrayList<Alternative> alternatives;
    private String correctAnswer;
    private String id;
    private String image;
	
	public Question(){}

    public Question(String question, String exam, ArrayList<Alternative> alternatives, String correctAnswer, String id, String image) {
        this.question = question;
        this.exam = exam;
        this.alternatives = alternatives;
        this.correctAnswer = correctAnswer;
        this.id = id;
        this.image = image;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getExam() {
        return this.exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public ArrayList<Alternative> getAlternatives() {
        return this.alternatives;
    }

    public void setAlternatives(ArrayList<Alternative> alternatives) {
        this.alternatives = alternatives;
    }

    public String getCorrectAnswer() {
        return this.correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getId() {
        return this.id;
    }

    public String getImage() { return this.image;}

    public void setImage(String Image){this.image = Image;}
}
