//package thud.myquizzapplication;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.AlertDialog;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//
//    TextView txt_totalQuestion, txt_question;
//    Button btn_A,btn_B,btn_C,btn_D,btn_submit;
//
//    int score = 0;
//    int totalQuestions = QuizAnswer.question.length;
//    int currentQuesttionIndex = 0;
//    String selectionAns = "";
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        txt_totalQuestion = findViewById(R.id.total_question);
//        txt_question = findViewById(R.id.question);
//        btn_A = findViewById(R.id.ans_A);
//        btn_B = findViewById(R.id.ans_B);
//        btn_C = findViewById(R.id.ans_C);
//        btn_D = findViewById(R.id.ans_D);
//        btn_submit = findViewById(R.id.btn_submit);
//
//
//        btn_A.setOnClickListener(this);
//        btn_B.setOnClickListener(this);
//        btn_C.setOnClickListener(this);
//        btn_D.setOnClickListener(this);
//        btn_submit.setOnClickListener(this);
//
//        txt_totalQuestion.setText("Total Question :" + totalQuestions);
//
//        loadNewQueation();
//
//    }
//
//    private void loadNewQueation() {
//
//        if (currentQuesttionIndex == totalQuestions){
//            finisQuizz();
//            return;
//        }
//        txt_question.setText(QuizAnswer.question[currentQuesttionIndex]);
//        btn_A.setText(QuizAnswer.choice[currentQuesttionIndex][0]);
//        btn_B.setText(QuizAnswer.choice[currentQuesttionIndex][1]);
//        btn_C.setText(QuizAnswer.choice[currentQuesttionIndex][2]);
//        btn_D.setText(QuizAnswer.choice[currentQuesttionIndex][3]);
//
//    }
//
//    private void finisQuizz() {
//        String passStatus = "";
//        if(score > totalQuestions*0.60){
//            passStatus = "Pass";
//        }else {
//            passStatus = "Fail";
//        }
//        new  AlertDialog.Builder(this)
//                .setTitle(passStatus)
//                .setMessage("Score is " + score +" out of" + totalQuestions)
//                .setPositiveButton("Restart" , ((dialogInterface, i) -> restartQuizz() ))
//                .setCancelable(false)
//                .show();
//    }
//
//    private void restartQuizz() {
//        score = 0;
//        currentQuesttionIndex = 0;
//        loadNewQueation();
//    }
//
//    @Override
//    public void onClick(View view) {
//
//        btn_A.setBackgroundColor(Color.BLUE);
//        btn_B.setBackgroundColor(Color.BLUE);
//        btn_C.setBackgroundColor(Color.BLUE);
//        btn_D.setBackgroundColor(Color.BLUE);
//            Button btn_click = (Button) view;
//            if(btn_click.getId() == R.id.btn_submit){
//                if(selectionAns.equals(QuizAnswer.correctAnswer[currentQuesttionIndex])){
//                    score++;
//                }
//                currentQuesttionIndex++;
//                loadNewQueation();
//            }else {
//                selectionAns = btn_click.getText().toString();
//                btn_click.setBackgroundColor(Color.GREEN);
//            }
//    }
//}