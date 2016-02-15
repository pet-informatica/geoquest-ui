package br.ufpe.cin.pet.geoquest;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import br.ufpe.cin.pet.geoquest.classes.Question;

public class QuestionFragment extends Fragment{

    ArrayList<Question> questions;

    private int currentQuestion;
	
	TextView questionExam;
	TextView questionDescription;
	LinearLayout layout_ans1;
	LinearLayout layout_ans2;
	LinearLayout layout_ans3;
	LinearLayout layout_ans4;
	LinearLayout layout_ans5;
	TextView answer1;
	TextView answer2;
	TextView answer3;
	TextView answer4;
	TextView answer5;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.fragment_question, container, false);

        getActivity().getActionBar().setTitle("GeoQuest");
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		
		questionExam = (TextView) rootView.findViewById(R.id.questionTitle);
		questionDescription = (TextView) rootView.findViewById(R.id.questionDescription);
		answer1 = (TextView) rootView.findViewById(R.id.answer1);
		answer2 = (TextView) rootView.findViewById(R.id.answer2);
		answer3 = (TextView) rootView.findViewById(R.id.answer3);
		answer4 = (TextView) rootView.findViewById(R.id.answer4);
		answer5 = (TextView) rootView.findViewById(R.id.answer5);
		
		layout_ans1 = (LinearLayout) rootView.findViewById(R.id.answer1Layout);
		layout_ans2 = (LinearLayout) rootView.findViewById(R.id.answer2Layout);
		layout_ans3 = (LinearLayout) rootView.findViewById(R.id.answer3Layout);
		layout_ans4 = (LinearLayout) rootView.findViewById(R.id.answer4Layout);
		layout_ans5 = (LinearLayout) rootView.findViewById(R.id.answer5Layout);

        requestQuestions(rootView);

        currentQuestion = 0;

		return rootView;
		
	}

    private void updateUI(){

        Question quest = questions.get(currentQuestion);

        questionExam.setText(quest.getExam());
        questionDescription.setText(quest.getQuestion());
        answer1.setText(quest.getAlternatives().get(0));
        answer2.setText(quest.getAlternatives().get(1));
        answer3.setText(quest.getAlternatives().get(2));
        answer4.setText(quest.getAlternatives().get(3));
        answer5.setText(quest.getAlternatives().get(4));

        setOnClickListeners(getView(), quest.getCorrectAnswer());
    }

    private void requestQuestions(final View rootView){
        Log.i("QuestionFragment", "Fetching questions...");

        String categoryId = getArguments().getString("category_id");

        String url = R.string.base_url + "questions/?category=" + categoryId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                try {

                    questions = new ArrayList<Question>();

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject question = response.getJSONObject(i);

                        String title = question.getString("question");
                        String exam = question.getString("exam");
                        String correctAnswer = question.getString("correct_answer");

                        ArrayList<String> alternatives = new ArrayList<String>();
                        alternatives.add( question.getString("option_a"));
                        alternatives.add( question.getString("option_b"));
                        alternatives.add( question.getString("option_c"));
                        alternatives.add( question.getString("option_d"));
                        alternatives.add( question.getString("option_e"));


                        questions.add(new Question(title, exam, alternatives, correctAnswer));

                    }

                    Log.i("QuestionFragment", "Fetched " + response.length() + " questions.");

                    updateUI();

                }catch(Exception e){
                    Log.e("QuestionFragment", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("QuestionFragment", error.getMessage());
            }
        });

        // Add the request to the RequestQueue.
        RequestSingleton.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest);
    }
	
	private void setOnClickListeners(View rootView, final String rightAnswer){
		layout_ans1 = (LinearLayout) rootView.findViewById (R.id.answer1Layout);
		layout_ans1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAnswer("A", rightAnswer);
			}
		});

		layout_ans2 = (LinearLayout) rootView.findViewById (R.id.answer2Layout);
		layout_ans2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAnswer("B", rightAnswer);
			}
		});

		layout_ans3 = (LinearLayout) rootView.findViewById (R.id.answer3Layout);
		layout_ans3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAnswer("C", rightAnswer);
			}
		});
		
		layout_ans4 = (LinearLayout) rootView.findViewById (R.id.answer4Layout);
		layout_ans4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAnswer("D", rightAnswer);
			}
		});

		layout_ans5 = (LinearLayout) rootView.findViewById (R.id.answer5Layout);
		layout_ans5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAnswer("E", rightAnswer);
			}
		});
	}
	
	private void checkAnswer(String ans, String rightAnswer){
		Dialog dialog = createDialog();
		
		if(!ans.equals(rightAnswer)){
			LinearLayout ll = (LinearLayout) dialog.findViewById(R.id.answerResultLayout);
			Button dialogButton = (Button) dialog.findViewById(R.id.btnNext); 
			TextView ansDialog = (TextView) dialog.findViewById(R.id.answerDialogResult); 
			TextView ansDescDialog = (TextView) dialog.findViewById(R.id.answerDialogDesc); 
			ImageView imgAns = (ImageView) dialog.findViewById(R.id.answerSymbol);
			
			ll.setBackgroundColor(getResources().getColor(R.color.bordeaux));
			dialogButton.setBackgroundResource(R.drawable.next_question_red);
			ansDialog.setText("RESPOSTA ERRADA");
			ansDialog.setTextColor(getResources().getColor(R.color.bordeaux));
			ansDescDialog.setTextColor(getResources().getColor(R.color.bordeaux));
			ansDescDialog.setText("Não rolou não, amiguinho. Tenta de novo, vai que vai.");
			imgAns.setImageResource(R.drawable.wrong);
		}else{
            LinearLayout ll = (LinearLayout) dialog.findViewById(R.id.answerResultLayout);
            Button dialogButton = (Button) dialog.findViewById(R.id.btnNext);
            TextView ansDialog = (TextView) dialog.findViewById(R.id.answerDialogResult);
            TextView ansDescDialog = (TextView) dialog.findViewById(R.id.answerDialogDesc);
            ImageView imgAns = (ImageView) dialog.findViewById(R.id.answerSymbol);

            ll.setBackgroundColor(getResources().getColor(R.color.green));
            dialogButton.setBackgroundResource(R.drawable.next_question);
            ansDialog.setText("RESPOSTA CERTA");
            ansDialog.setTextColor(getResources().getColor(R.color.green));
            ansDescDialog.setTextColor(getResources().getColor(R.color.green));
            ansDescDialog.setText("Boa boy. Botando quente.");
            imgAns.setImageResource(R.drawable.check);
        }
		
		dialog.show();
	}
	
	private Dialog createDialog(){
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_answer);

		Button dialogButton = (Button) dialog.findViewById(R.id.btnNext);

		dialogButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                currentQuestion++;
                updateUI();
				dialog.dismiss();
			}
		});
		
		return dialog;
	}

}
