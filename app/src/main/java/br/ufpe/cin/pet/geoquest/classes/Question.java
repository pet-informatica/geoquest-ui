package br.ufpe.cin.pet.geoquest.classes;

import java.util.Vector;

public class Question {
	
	public String title;
	public String description;
	public Vector<String> alternatives;
	public int rightAnswer;
	
	public Question(){}
	
	public Question(String title, String description,
			Vector<String> alternatives, int rightAnswer) {
		this.title = title;
		this.description = description;
		this.alternatives = alternatives;
		this.rightAnswer = rightAnswer;
	}

}
