package thud.myquizzapplication.Display.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.TimeAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import thud.myquizzapplication.Data.Question;
import thud.myquizzapplication.Data.QuizzDBHelper;
import thud.myquizzapplication.R;

public class QuizzHome extends AppCompatActivity {

    public  static final String EXTRA_SCORE = "extraScore";
        private TextView txtQuestion;
        private TextView txtScore;
        private TextView txtQCount;
        private TextView txtTime;
        private RadioGroup rdgOption;
        private RadioButton rdbOption1;
        private RadioButton rdbOption2;
        private RadioButton rdbOption3;
        private Button btnConfirm;
        private ColorStateList textcolorDefault;
        private List<Question> quesList;
        private int questionCounter;
        private int questionTotal;
        private Question currentQuestion;
        private int score;
        private boolean answered;
        private  long Timeback;
        private static  final long COUNTDOWN_IN_TIME = 30000;
        private ColorStateList txtCOlorTime;
        private long Timeleft;
        private CountDownTimer cdTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quizz_home);

        txtQuestion = findViewById(R.id.txt_question);
        txtScore = findViewById(R.id.txt_score);
        txtQCount = findViewById(R.id.txt_questioncount);
        txtTime = findViewById(R.id.txt_time);
        rdgOption = findViewById(R.id.rdp_option);
        rdbOption1 = findViewById(R.id.rdb_option1);
        rdbOption2 = findViewById(R.id.rdb_option2);
        rdbOption3 = findViewById(R.id.rdb_option3);
        btnConfirm = findViewById(R.id.btn_confirm);

        textcolorDefault = rdbOption1.getTextColors();
        txtCOlorTime = txtTime.getTextColors();

        QuizzDBHelper dbhelp = new QuizzDBHelper(this);
        quesList = dbhelp.getAllQuestion();
        questionTotal = quesList.size();
//        questionTotal = 25;
        Collections.shuffle(quesList);
        showNextQuestion();
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!answered){
                    if(rdbOption1.isChecked() || rdbOption2.isChecked() || rdbOption3.isChecked()){
                        checkAnswer();
                    }else {
                        Toast.makeText(QuizzHome.this, "Please select answer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });
    }



    private void showSolution() {

        rdbOption1.setTextColor(Color.RED);
        rdbOption2.setTextColor(Color.RED);
        rdbOption3.setTextColor(Color.RED);

        switch (currentQuestion.getAnswerNb()){
            case 1:
                rdbOption1.setTextColor(Color.GREEN);
                txtQuestion.setText("Answer 1 is Correct");
                break;
            case 2:
                rdbOption2.setTextColor(Color.GREEN);
                txtQuestion.setText("Answer 2 is Correct");
                break;
            case 3:
                rdbOption3.setTextColor(Color.GREEN);
                txtQuestion.setText("Answer 3 is Correct");
                break;
        }
        if(questionCounter < questionTotal){
            btnConfirm.setText("Next");
        }else {
            btnConfirm.setText("Finish");
        }

    }



    private void showNextQuestion() {
        rdbOption1.setTextColor(textcolorDefault);
        rdbOption2.setTextColor(textcolorDefault);
        rdbOption3.setTextColor(textcolorDefault);
        rdgOption.clearCheck();
        if(questionCounter < questionTotal){
            currentQuestion = quesList.get(questionCounter);
            txtQuestion.setText(currentQuestion.getQuestion());
            rdbOption1.setText(currentQuestion.getOption1());
            rdbOption2.setText(currentQuestion.getOption2());
            rdbOption3.setText(currentQuestion.getOption3());

            questionCounter++;
            txtQCount.setText("Question: " + questionCounter + "/" + questionTotal);
            answered = false;
            btnConfirm.setText("Confirm");

            Timeleft = COUNTDOWN_IN_TIME;
            startTimeLeft();
        }else{
            finishQuizz();
        }
    }

    private void startTimeLeft() {
        cdTimer = new CountDownTimer(Timeleft,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Timeleft = millisUntilFinished;
                updateTime();
            }

            @Override
            public void onFinish() {
                Timeleft = 0;
                updateTime();
                checkAnswer();
            }
        }.start();
    }
    private void updateTime() {
        int minute =(int) (Timeleft / 1000) / 60;
        int second = (int) (Timeleft / 1000) % 60;

        String Timeformated = String.format(Locale.getDefault(), "%02d:%02d" , minute,second);


        txtTime.setText(Timeformated);

        if (Timeleft < 10000){
            txtTime.setTextColor(Color.RED);

        }else {
            txtTime.setTextColor(txtCOlorTime);
        }
    }

    private void checkAnswer() {
        answered =true;

        cdTimer.cancel();
        RadioButton rdbSelected = findViewById(rdgOption.getCheckedRadioButtonId());
        int answer_nb = rdgOption.indexOfChild(rdbSelected) + 1;

        if(answer_nb == currentQuestion.getAnswerNb()){
            score++;
            txtScore.setText("Score: " + score);
        }
        showSolution();
    }
    private void finishQuizz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE,score);
        setResult(RESULT_OK,resultIntent);
        int incr = questionTotal - score;
        Intent I = getIntent();
        String username = I.getStringExtra("username");
        if (username == null){
            username = "Guest account";
        }
     new   AlertDialog.Builder(this)
             .setTitle("You success Text")
                     .setMessage("User is: "+ username + "\n" + "Score is: " + score + " out of " + questionTotal +  "\n" + "Question correct is: "+ score +"\n" + "Question incorrect is: " + incr + "\n")
                            .setPositiveButton("OK", (dialog, which) -> finish())
                                        .show();
    }

    @Override
    public void onBackPressed() {
        if (Timeback + 2000 > System.currentTimeMillis()){
            finishQuizz();
        }else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }
        Timeback = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cdTimer != null){
            cdTimer.cancel();
        }
    }
}