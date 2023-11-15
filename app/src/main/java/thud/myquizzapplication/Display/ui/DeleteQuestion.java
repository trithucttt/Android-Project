package thud.myquizzapplication.Display.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import thud.myquizzapplication.Data.Question;
import thud.myquizzapplication.Data.QuizzDBHelper;
import thud.myquizzapplication.R;

public class DeleteQuestion extends AppCompatActivity {

    QuizzDBHelper db;
    Question q;
    TextView ViewQuestion;
    EditText edtIdDelete;
    Button btnDelete,btnRenderAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_question);
        edtIdDelete = findViewById(R.id.edt_choseid);
        btnDelete = findViewById(R.id.btn_delete);
        btnRenderAdd = findViewById(R.id.btn_renderadd);

        Button btnReturn = findViewById(R.id.btn_returnMenu2);
        db = new QuizzDBHelper(this);
        ViewQuestion();

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = getIntent();
                String username = I.getStringExtra("username");
                finish();
                Intent i = new Intent(getApplicationContext(),Menu.class);
                i.putExtra("username",username);
                startActivity(i);
            }
        });

        btnRenderAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(getApplicationContext(),AddQuestion.class);
                startActivity(i);
            }
        });


        }
// show all question with listView
    private void ViewQuestion() {
        List<Question> quesList = db.getAllQuestion();
        String strQuestion = "\n\n\n";
        ViewQuestion = findViewById(R.id.txt_delete);
        int n = quesList.size();
        if (n > 0){
            for (int i =0 ; i<n;i++){
                q = quesList.get(i);
                strQuestion += "\n\n\n\n Question ID: "+ q.getId();
                strQuestion +=  "\n\n Question Name: " + q.getQuestion();
                strQuestion += "\n\n Option 1: " + q.getOption1();
                strQuestion += "\n\n Option 2: " + q.getOption2();
                strQuestion += "\n\n Option 3: " + q.getOption3();
                strQuestion += "\n\n Number Answer  Correct: " + q.getAnswerNb();
            }
            ViewQuestion.setText(strQuestion);
    }
        else
            ViewQuestion.setText("No Question in Database");
    }

// btn delete onclick
    public void DeleteQuestion(View view){
        String idStr = edtIdDelete.getText().toString();
    if(idStr.length() != 0){
        int id = Integer.parseInt(idStr);
        int rowsDeleted = db.deleteQuestion(id);
        if (rowsDeleted > 0) {
            Toast.makeText(this, "Delete success", Toast.LENGTH_SHORT).show();
            edtIdDelete.clearFocus();
        } else {
            Toast.makeText(this, "ID is incorrect or has been deleted", Toast.LENGTH_SHORT).show();
        }
    }else {
        Toast.makeText(this, "You have not entered the ID you want to Delete", Toast.LENGTH_SHORT).show();
    }
    edtIdDelete.requestFocus();
    ViewQuestion();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}