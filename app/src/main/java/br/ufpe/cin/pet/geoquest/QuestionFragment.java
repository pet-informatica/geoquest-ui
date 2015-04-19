package br.ufpe.cin.pet.geoquest;

import java.util.Vector;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.ufpe.cin.pet.geoquest.classes.Question;

public class QuestionFragment extends Fragment{
	
	TextView questionTitle;
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

		getActivity().getActionBar().setTitle("Geoquest");
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		
		questionTitle = (TextView) rootView.findViewById(R.id.questionTitle);
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
		
		Question quest = new Question();
		quest.title = "Enem 2009".toUpperCase();
		
		quest.description = "Suspendisse ultricies mi in dignissim consectetur. Duis non turpis neque. Fusce id quam semper massa molestie cursus vel non purus. Etiam aliquet urna placerat magna venenatis bibendum. Ut nec risus gravida libero porta iaculis sed eu orci. Ut feugiat non elit et cursus. Aliquam rutrum eros et commodo vehicula. Maecenas vel tincidunt ligula, id sagittis quam. Nam ac mauris in nulla pretium pellentesque. Nullam semper, ante vitae molestie venenatis, ligula ligula tincidunt turpis, id viverra tortor felis sit amet orci. Aenean luctus finibus metus id tempor. Duis viverra lectus eget ipsum sodales elementum.";
		
		Vector<String> answers = new Vector<String>();
		answers.add("Lorem ipsum dolor sit amet.");
		answers.add("Nunc facilisis turpis enim, eget porta justo.");
		answers.add("fosa faoishfa fwifjwelg oieghw");
		answers.add("23pr9j esoighs tp3i4ntl34 pofjqe");
		answers.add("23nr fefpnwe� gwgkwengkwe opwejpwengn w wieehgwen");		
		quest.alternatives = answers;
		
		quest.rightAnswer = 4;
		
		questionTitle.setText(quest.title);
		questionDescription.setText(quest.description);
		answer1.setText(quest.alternatives.get(0));
		answer2.setText(quest.alternatives.get(1));
		answer3.setText(quest.alternatives.get(2));
		answer4.setText(quest.alternatives.get(3));
		answer5.setText(quest.alternatives.get(4));
		
		setOnClickListeners(rootView, quest.rightAnswer);
		
		return rootView;
		
	}	
	
	private void setOnClickListeners(View rootView, final int rightAnswer){
		layout_ans1 = (LinearLayout) rootView.findViewById (R.id.answer1Layout);
		layout_ans1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAnswer(1, rightAnswer);
			}
		});

		layout_ans2 = (LinearLayout) rootView.findViewById (R.id.answer2Layout);
		layout_ans2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAnswer(2, rightAnswer);
			}
		});

		layout_ans3 = (LinearLayout) rootView.findViewById (R.id.answer3Layout);
		layout_ans3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAnswer(3, rightAnswer);
			}
		});
		
		layout_ans4 = (LinearLayout) rootView.findViewById (R.id.answer4Layout);
		layout_ans4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAnswer(4, rightAnswer);
			}
		});

		layout_ans5 = (LinearLayout) rootView.findViewById (R.id.answer5Layout);
		layout_ans5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAnswer(5, rightAnswer);
			}
		});
	}
	
	private void checkAnswer(int ans, int rightAnswer){
		Dialog dialog = createDialog();
		
		if(ans != rightAnswer){
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
			ansDescDialog.setText("N�o rolou n�o, amiguinho. Tenta de novo, vai que n�.");
			imgAns.setImageResource(R.drawable.wrong);
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
				dialog.dismiss();
			}
		});
		
		return dialog;
	}

}
