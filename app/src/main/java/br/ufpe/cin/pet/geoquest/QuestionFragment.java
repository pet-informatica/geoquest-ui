package br.ufpe.cin.pet.geoquest;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cloudinary.Cloudinary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.ufpe.cin.pet.geoquest.Utils.BitmapFromURL;
import br.ufpe.cin.pet.geoquest.Utils.Cloud;
import br.ufpe.cin.pet.geoquest.classes.Alternative;
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
	private AdapterAlternatives adapter;

    private int currentQuestion;
	
	TextView questionExam;
	TextView questionDescription;
	ImageView questionImage;
	ListView questionAlternatives;
	TextView questionSequence;
	TextView timer;
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

		final View rootView = inflater.inflate(R.layout.fragment_questionup, container, false);

		progressDialog = new ProgressDialog(rootView.getContext());
		progressDialog.setMessage("Carregando...");
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(true);
		progressDialog.show();

		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().hide();

		questionExam = (TextView) rootView.findViewById(R.id.question_exam);
		questionDescription = (TextView) rootView.findViewById(R.id.question_description);
		questionImage = (ImageView) rootView.findViewById(R.id.question_image);
		questionAlternatives = (ListView) rootView.findViewById(R.id.listViewQuestion);
		questionSequence = (TextView) rootView.findViewById(R.id.question_number);
		timer = (TextView) rootView.findViewById(R.id.timer);

		questionDescription.setMovementMethod(new ScrollingMovementMethod());

		currentQuestion = 0;

        requestQuestions(rootView);

		return rootView;
	}


    private void updateUI() {

        Question quest = questions.get(currentQuestion);
		String problem = "Questão ";
		int seq = currentQuestion+1;
		String examFrom = "FONTE: ";

		questionExam.setText(examFrom+quest.getExam());
        questionDescription.setText(quest.getQuestion());
		questionSequence.setText(problem+seq);

		adapter = new AdapterAlternatives(getActivity().getApplicationContext(), quest.getAlternatives());
		questionAlternatives.setAdapter(adapter);

		updateImage(quest);

		setTimer();

		setOnClickListeners(getView(), quest.getCorrectAnswer());
    }

	private void updateImage(Question quest) {

		String str = quest.getImage();

		final String src = Cloud.cloudinary.url().generate(str);

		new AsyncTask<Void, Void, Void>() {
			Bitmap bm = null;

			@Override
			protected Void doInBackground(Void... params) {
				bm = new BitmapFromURL(src).getBitmapFromURL();
				return null;
			}

			@Override
			protected void onPostExecute(Void v) {
				questionImage.setImageBitmap(bm);
			}
		}.execute();

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

	private void setTimer() {

		new CountDownTimer(180000, 1000) {

			public void onTick(long millis) {
				long seconds = millis / 1000;
				long minutes = seconds/60;
				seconds = seconds - (minutes*60);
				String leadingZero = "";
				if (seconds < 10) leadingZero = "0";
				timer.setText("0"+minutes+":"+leadingZero+seconds);
			}

			public void onFinish() {
				timer.setText("00:00");

				Dialog dialog = createDialog();

				is_right.put(questions.get(currentQuestion).getId(), 0);

				ViewHolder vw = new ViewHolder();
				vw = populateViewHolder(vw, dialog);

				vw.ll.setBackgroundColor(getResources().getColor(R.color.bordeaux));
				vw.dialogButton.setBackgroundResource(R.drawable.next_question_red);
				vw.ansDialog.setText("TEMPO EXCEDIDO");
				vw.ansDialog.setTextColor(getResources().getColor(R.color.bordeaux));
				vw.ansDescDialog.setTextColor(getResources().getColor(R.color.bordeaux));
				vw.ansDescDialog.setText("Que pena!");
				vw.imgAns.setImageResource(R.drawable.wrong);

				dialog.setCancelable(false);
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
			}

		}.start();

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

				ArrayList<Alternative> alternatives = new ArrayList<Alternative>();
				alternatives.add(new Alternative(question.getString("option_a"), "A"));
				alternatives.add(new Alternative(question.getString("option_b"), "B"));
				alternatives.add(new Alternative(question.getString("option_c"), "C"));
				alternatives.add(new Alternative(question.getString("option_d"), "D"));
				alternatives.add(new Alternative(question.getString("option_e"), "E"));

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

		questionAlternatives.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				switch (pos) {
					case 0:
						checkAnswer("A", rightAnswer);
						break;
					case 1:
						checkAnswer("B", rightAnswer);
						break;
					case 2:
						checkAnswer("C", rightAnswer);
						break;
					case 3:
						checkAnswer("D", rightAnswer);
						break;
					case 4:
						checkAnswer("E", rightAnswer);
						break;
				}
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
						//backurl = getResources().getString(R.string.base_url)+"result/"

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
