package thud.myquizzapplication.Display.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import thud.myquizzapplication.Data.DbHelper;
import thud.myquizzapplication.R;

public class Menu extends AppCompatActivity {


    private TextView txtHighScore;
    private int hightScore;
    private static final int REQUEST_CODE_QUIZ = 1;
    public static final String SHARE_PREFS = "sharePrefs";
    public static String KEY_HIGHSCORE = "keyHighscore";
    Button btnStart, btnLogin, btnRegister, btnClose, btnAddQuestion;
    DbHelper dbUser;
    Button btnQuestionList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        btnStart = findViewById(R.id.btn_start);
        btnLogin = findViewById(R.id.btn_logins);
        btnRegister = findViewById(R.id.btn_registers);
        btnClose = findViewById(R.id.btn_close);
        btnAddQuestion = findViewById(R.id.btn_addquestion);
        txtHighScore = findViewById(R.id.txt_highscore);
        loadHighScore();
        Intent I = getIntent();
        String username = I.getStringExtra("username");

        dbUser = new DbHelper(this);


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuizz();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                intent.putExtra("username",username);
                startActivity(intent);
                Toast.makeText(Menu.this, "Welcome to Login page", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

                Intent intent = new Intent(getApplicationContext(), Register.class);
                intent.putExtra("username",username);
                startActivity(intent);
                Toast.makeText(Menu.this, "Welcome to Register page", Toast.LENGTH_SHORT).show();

            }
        });

        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AddQuestion.class);
                // check user
                if(username.equals("admin")){
                    finish();
                    intent.putExtra("username",username);
                    startActivity(intent);
                    Toast.makeText(Menu.this, "Welcome Question Add Question Page", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Menu.this, "You do not have enough permission to use", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        btnQuestionList = findViewById(R.id.btn_questionlist);
        btnQuestionList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), DeleteQuestion.class);
                //check user
                if(username.equals("admin")){
                    finish();
                    intent.putExtra("username",username);
                    startActivity(intent);
                    Toast.makeText(Menu.this, "Welcome Question List Page", Toast.LENGTH_SHORT).show();
                }
                else {
                    btnAddQuestion.setBackgroundColor(Color.LTGRAY);
                    btnQuestionList.setBackgroundColor(Color.LTGRAY);
                    Toast.makeText(Menu.this, "You do not have enough permission to use", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void startQuizz() {

        Intent intent = new Intent(Menu.this, QuizzHome.class);
        Intent I = getIntent();
        String username = I.getStringExtra("username");
        intent.putExtra("username",username);
        startActivityIfNeeded(intent, REQUEST_CODE_QUIZ);
    }

    private void loadHighScore() {
        SharedPreferences prefs = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
        hightScore = prefs.getInt(KEY_HIGHSCORE, 0);
        txtHighScore.setText("HighScore: " + hightScore);
    }

    private void updateHighScore(int highScoreNew) {
        hightScore = highScoreNew;
        txtHighScore.setText("HighScore: " + hightScore);

        SharedPreferences prefs = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor edt = prefs.edit();
        edt.apply();
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_QUIZ) {
            if (resultCode == RESULT_OK) {
                int score = data.getIntExtra(QuizzHome.EXTRA_SCORE, 0);
                if (score > hightScore) {
                    updateHighScore(score);
                }
            }
        }

    }


}








