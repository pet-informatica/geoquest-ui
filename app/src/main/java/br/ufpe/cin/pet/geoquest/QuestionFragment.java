package br.ufpe.cin.pet.geoquest;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

import com.cloudinary.Cloudinary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.ufpe.cin.pet.geoquest.classes.Category;
import br.ufpe.cin.pet.geoquest.classes.Question;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QuestionFragment extends Fragment {

    List<Question> questions;
	HashMap<Integer, Integer> is_right = new HashMap<Integer, Integer>();
	private ProgressDialog progressDialog;

    private int currentQuestion;
	
	TextView questionExam;
	TextView questionDescription;
	ImageView questionImage;
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
	Category category;
	String cat;
	int lev;

	private final OkHttpClient client = new OkHttpClient();

	public QuestionFragment(Category category, int level) {
		this.category = category;
		this.cat = category.getName();
		this.lev = level;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.fragment_question, container, false);

        getActivity().getActionBar().setTitle("GeoQuest");
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().hide();

		questionExam = (TextView) rootView.findViewById(R.id.questionTitle);
		questionDescription = (TextView) rootView.findViewById(R.id.questionDescription);
		questionImage = (ImageView) rootView.findViewById(R.id.questionImage);
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

		progressDialog = new ProgressDialog(rootView.getContext());
		progressDialog.setMessage("Carregando...");
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(true);
		progressDialog.show();

		currentQuestion = 0;

        requestQuestions(rootView);

		return rootView;
	}


    private void updateUI() {

        Question quest = questions.get(currentQuestion);

        questionExam.setText(quest.getExam());
        questionDescription.setText(quest.getQuestion());

        answer1.setText(quest.getAlternatives().get(0));
        answer2.setText(quest.getAlternatives().get(1));
        answer3.setText(quest.getAlternatives().get(2));
        answer4.setText(quest.getAlternatives().get(3));
        answer5.setText(quest.getAlternatives().get(4));

		updateImage(quest);

		setOnClickListeners(getView(), quest.getCorrectAnswer());
    }

	private void updateImage(Question quest) {

		String str = quest.getImage();

		Cloudinary cloudinary = new Cloudinary("cloudinary://789778297459378:24aizLA7T6j7iUNKTqKDAbR-ZXw@ufpe");
		final String src = cloudinary.url().generate(str);

		new AsyncTask<Void, Void, Void>() {
			Bitmap bm = null;

			@Override
			protected Void doInBackground(Void... params) {
				bm = getBitmapFromURL(src);
				return null;
			}

			@Override
			protected void onPostExecute(Void v) {
				questionImage.setImageBitmap(bm);
			}
		}.execute();

	}

	public static Bitmap getBitmapFromURL(String src) {

		try {
			Log.e("src",src);
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();

			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);

			return myBitmap;

		} catch (IOException e) {
			e.printStackTrace();

			return null;
		}
	}

    private void requestQuestions(final View rootView){
        Log.i("QuestionFragment", "Fetching questions...");

		try {

			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					try {
						questions = new ArrayList<Question>();
						questions = run();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}

				@Override
				protected void onPostExecute(Void v) {
					updateUI();
					progressDialog.hide();
				}

			}.execute();

		} catch (Exception e) {
			Log.i("ERROR", "Não foi possível obter as suas questões");
			e.printStackTrace();
		}
    }

	private List<Question> run() throws Exception {

		//String categoryId = category.getId()+"xxx"+lev;

		String url = "http://www.mocky.io/v2/5876b99e100000a2148b5ced";
		//String backUrl = getResources().getString(R.string.base_url)+"questions/?category="+categoryId;

		Request request = new Request.Builder()
				.url(url)
				.header("TOKEN", Config.key)
				.build();
		Response jResponse = client.newCall(request).execute();
		if (!jResponse.isSuccessful()) throw new IOException("Unexpected code " + jResponse);

		List<Question> quest = new ArrayList<Question>();

		try {
			JSONArray response = new JSONArray(jResponse.body().string());

			for (int i = 0; i < response.length(); i++) {
				JSONObject question = response.getJSONObject(i);

				String title = question.getString("question");
				int id = question.getInt("id");
				String exam = question.getString("exam");
				String correctAnswer = question.getString("correct_answer");
				String image = question.getString("image");

				ArrayList<String> alternatives = new ArrayList<String>();
				alternatives.add( question.getString("option_a"));
				alternatives.add( question.getString("option_b"));
				alternatives.add( question.getString("option_c"));
				alternatives.add( question.getString("option_d"));
				alternatives.add( question.getString("option_e"));


				quest.add(new Question(title, exam, alternatives, correctAnswer, id, image));
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.i("JSONError", "Erro na formatação do response");
		}
		return quest;
	}

	static class ViewHolder {
		protected LinearLayout ll;
		protected Button dialogButton;
		protected TextView ansDialog;
		protected TextView ansDescDialog;
		protected ImageView imgAns;
	}

	private ViewHolder populateViewHolder(ViewHolder vw, Dialog dialog) {
		vw.ll = (LinearLayout) dialog.findViewById(R.id.answerResultLayout);
		vw.dialogButton = (Button) dialog.findViewById(R.id.btnNext);
		vw.ansDialog = (TextView) dialog.findViewById(R.id.answerDialogResult);
		vw.ansDescDialog = (TextView) dialog.findViewById(R.id.answerDialogDesc);
		vw.imgAns = (ImageView) dialog.findViewById(R.id.answerSymbol);

		return vw;
	}
	
	private void setOnClickListeners(View rootView, final String rightAnswer){

		rootView.setClickable(false);
		rootView.setEnabled(false);

		//layout_ans1 = (LinearLayout) rootView.findViewById (R.id.answer1Layout);
		layout_ans1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAnswer("A", rightAnswer);
			}
		});

		//layout_ans2 = (LinearLayout) rootView.findViewById (R.id.answer2Layout);
		layout_ans2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAnswer("B", rightAnswer);
			}
		});

		//layout_ans3 = (LinearLayout) rootView.findViewById (R.id.answer3Layout);
		layout_ans3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAnswer("C", rightAnswer);
			}
		});
		
		//layout_ans4 = (LinearLayout) rootView.findViewById (R.id.answer4Layout);
		layout_ans4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAnswer("D", rightAnswer);
			}
		});

		//layout_ans5 = (LinearLayout) rootView.findViewById (R.id.answer5Layout);
		layout_ans5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAnswer("E", rightAnswer);
			}
		});

		questionImage.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Dialog dialog = new Dialog(getActivity());
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dialog_image);

				ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
				image.setImageDrawable(questionImage.getDrawable());
				dialog.show();
			}
		});
	}

	private void checkAnswer(String ans, String rightAnswer){
		Dialog dialog = createDialog();
		
		if(!ans.equals(rightAnswer)){

			is_right.put(questions.get(currentQuestion).getId(), 0);

			ViewHolder vw = new ViewHolder();
			vw = populateViewHolder(vw, dialog);
			
			vw.ll.setBackgroundColor(getResources().getColor(R.color.bordeaux));
			vw.dialogButton.setBackgroundResource(R.drawable.next_question_red);
			vw.ansDialog.setText("RESPOSTA ERRADA");
			vw.ansDialog.setTextColor(getResources().getColor(R.color.bordeaux));
			vw.ansDescDialog.setTextColor(getResources().getColor(R.color.bordeaux));
			vw.ansDescDialog.setText("Que pena!");
			vw.imgAns.setImageResource(R.drawable.wrong);

		} else {

			is_right.put(questions.get(currentQuestion).getId(), 1);

			ViewHolder vw = new ViewHolder();
			vw = populateViewHolder(vw, dialog);

            vw.ll.setBackgroundColor(getResources().getColor(R.color.green));
			vw.dialogButton.setBackgroundResource(R.drawable.next_question);
			vw.ansDialog.setText("RESPOSTA CERTA");
			vw.ansDialog.setTextColor(getResources().getColor(R.color.green));
			vw.ansDescDialog.setTextColor(getResources().getColor(R.color.green));
			vw.ansDescDialog.setText("Parabéns!");
			vw.imgAns.setImageResource(R.drawable.check);

        }

		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	public static final MediaType JSON
			= MediaType.parse("application/json; charset=utf-8");
	private void updateBack(final int right) {
		//Percorrer o is_right e informar ao back quais as questoes ja foram respondidas e
		//estao indisponiveis. Deixar salvo la o precentual de acerto no ultimo bloco
		try {
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					try {
						//backurl = getResources().getString(R.string.base_url)+acertos/

						Log.d("POST", post("http://www.roundsapp.com/post", "{ 'Questoes' : " + right + "}"));
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}

				@Override
				protected void onPostExecute(Void v) { 	}
			}.execute();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	String post(String url, String json) throws IOException {
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder()
				.url(url)
				.header("TOKEN", Config.key)
				.post(body)
				.build();
		try (Response response = client.newCall(request).execute()) {
			return response.body().toString();
		}
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
				if (currentQuestion >= questions.size()) {
					int right = 0;
					for (int i: is_right.values()) {
						if(i == 1) right++;
					}
					updateBack(right);
					dialog.dismiss();
					FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
					ft.replace(R.id.container, new TransitionFragment(category, lev, 0));
					ft.addToBackStack("transition_fragment");
					ft.commit();
				} else {
					updateUI();
					dialog.dismiss();
				}
			}
		});
		
		return dialog;
	}
}
