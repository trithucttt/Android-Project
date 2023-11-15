package thud.myquizzapplication.Display.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import thud.myquizzapplication.Data.Question;
import thud.myquizzapplication.Data.QuizzDBHelper;
import thud.myquizzapplication.R;

@SuppressLint("NotConstructor")
public class AddQuestion extends AppCompatActivity {
    Button btnReturn, btnAdd, btnEdit;
    EditText edtQuestionName, edtChoice1, edtChoice2, edtChoice3, edtId;
    RadioGroup rdgChoice;
    TextInputLayout layoutQuesName, layoutChoice1, layoutChoice2, layoutChoice3;
    RadioButton rdb1, rdb2, rdb3;
    String strQuesName, strChoice1, strChoice2, strChoice3;
    int AnsCorrect = 0;
    QuizzDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_question);


        edtQuestionName = findViewById(R.id.edt_questionname);
        edtChoice1 = findViewById(R.id.edt_choice1);
        edtChoice2 = findViewById(R.id.edt_choice2);
        edtChoice3 = findViewById(R.id.edt_choice3);
        rdb1 = findViewById(R.id.rdb_choice1);
        rdb2 = findViewById(R.id.rdb_choice2);
        rdb3 = findViewById(R.id.rdb_choice3);
        btnReturn = findViewById(R.id.btn_returnMenuadd);
        btnAdd = findViewById(R.id.btn_add);
        layoutQuesName = findViewById(R.id.layout_questionname);
        layoutChoice1 = findViewById(R.id.layout_choice1);
        layoutChoice3 = findViewById(R.id.layout_choice3);
        layoutChoice2 = findViewById(R.id.layout_choice2);
        btnEdit = findViewById(R.id.btn_eddit);
        db = new QuizzDBHelper(this);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent I = getIntent();
                String username = I.getStringExtra("username");
                finish();
                Intent it = new Intent(getApplicationContext(), EditQuestion.class);
                it.putExtra("username",username);
                startActivity(it);
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = getIntent();
                String username = I.getStringExtra("username");
                finish();
                Intent itent = new Intent(getApplicationContext(),Menu.class);
                itent.putExtra("username",username);
                startActivity(itent);
            }
        });

    }

    public void AddQuestion(View view) {
        strQuesName = edtQuestionName.getText().toString().trim();
        if ((strQuesName.length()) == 0) {
            layoutQuesName.setError("Question is not filled yet");
            edtQuestionName.requestFocus();
            return;
        } else
            layoutQuesName.setError(null);
        strChoice1 = edtChoice1.getText().toString().trim();
        if ((strChoice1.length()) == 0) {
            layoutChoice1.setError("Choice 1 is not filled yet");
            edtChoice1.requestFocus();
            return;
        } else
            layoutChoice1.setError(null);
        strChoice2 = edtChoice2.getText().toString().trim();
        if (strChoice2.length() == 0) {
            layoutChoice2.setError("Choice 2 is not filled yet");
            edtChoice2.requestFocus();
            return;
        } else
            layoutChoice2.setError(null);
        strChoice3 = edtChoice3.getText().toString().trim();
        if (strChoice1.length() == 0) {
            layoutChoice3.setError("Choice 3 is not filled yet");
            edtChoice3.requestFocus();
            return;
        } else
            layoutChoice3.setError(null);
        if (rdb1.isChecked() || rdb2.isChecked() || rdb3.isChecked()) {
            ChoseAnsCorrect();
            Boolean chkAns = db.chkAnswer(strQuesName, strChoice1, strChoice2, strChoice3);
            if (chkAns == false) {
                Boolean insert = db.InsertQuestion(strQuesName, strChoice1, strChoice2, strChoice3, AnsCorrect);
                if (insert == true) {
                    Toast.makeText(this, "Question Insert successfully", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder brdInform = new AlertDialog.Builder(this);
                    brdInform.setTitle("Question Information");
                    brdInform.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(AddQuestion.this, "Accept", Toast.LENGTH_SHORT).show();
                        }
                    });
                    brdInform.setNegativeButton("Return", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent I = getIntent();
                            String username = I.getStringExtra("username");
                            Intent i = new Intent(getApplicationContext(), Menu.class);
                            i.putExtra("username",username);
                            startActivity(i);
                            Toast.makeText(AddQuestion.this, "Return Success", Toast.LENGTH_SHORT).show();
                        }
                    });
                    String strQuestionInfo;
                    strQuestionInfo = "Question Name :" + strQuesName + "\n";
                    strQuestionInfo += "Choice 1: " + strChoice1 + "\n";
                    strQuestionInfo += "Choice 2: " + strChoice2 + "\n";
                    strQuestionInfo += "Choice 3: " + strChoice3 + "\n";
                    strQuestionInfo += "Number Option Correct:" + AnsCorrect + "\n";
                    brdInform.setMessage(strQuestionInfo);
                    brdInform.create().show();
                } else {
                    Toast.makeText(this, "Question Insert failed", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Question and answer already exist", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "You need choice answer correct", Toast.LENGTH_SHORT).show();
        }

    }

    private void ChoseAnsCorrect() {

        if (rdb1.isChecked()) {
            AnsCorrect = 1;
        } else if (rdb2.isChecked()) {
            AnsCorrect = 2;
        } else
            AnsCorrect = 3;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}