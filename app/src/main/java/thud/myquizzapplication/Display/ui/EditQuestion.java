package thud.myquizzapplication.Display.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class EditQuestion extends AppCompatActivity {


    Button btnReturn, btnAdd, btnEdit,btnShow;
    EditText edtQuestionName, edtChoice1, edtChoice2, edtChoice3, edtId;
    RadioGroup rdgChoice;
    TextInputLayout layoutQuesName, layoutChoice1, layoutChoice2, layoutChoice3,layoutId;
    RadioButton rdb1, rdb2, rdb3;
    String strQuesName, strChoice1, strChoice2, strChoice3;
    int AnsCorrect = 0;
    QuizzDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_question);

        edtQuestionName = findViewById(R.id.edt_questionnameedit);
        edtChoice1 = findViewById(R.id.edt_choice1edit);
        edtChoice2 = findViewById(R.id.edt_choice2edit);
        edtChoice3 = findViewById(R.id.edt_choice3);
        rdb1 = findViewById(R.id.rdb_choice1edit);
        rdb2 = findViewById(R.id.rdb_choice2edit);
        rdb3 = findViewById(R.id.rdb_choice3edit);
        btnReturn = findViewById(R.id.btn_returnMenuedit);
        btnAdd = findViewById(R.id.btn_confirm);
        layoutQuesName = findViewById(R.id.layout_questionnameedit);
        layoutChoice1 = findViewById(R.id.layout_choice1edit);
        layoutChoice2 = findViewById(R.id.layout_choice2edit);
        layoutChoice3 = findViewById(R.id.layout_choice3edit);
        layoutId = findViewById(R.id.layout_Id);
        edtId = findViewById(R.id.edt_idedit);
        btnShow = findViewById(R.id.btn_show);
        db = new QuizzDBHelper(this);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strID = edtId.getText().toString();

                if (strID.length() == 0) {
                    layoutId.setError("ID  is not filled yet");
                    edtId.requestFocus();
                } else {
                    layoutId.setError(null);
                    int idQuestion = Integer.parseInt(strID);
                    edtId.setEnabled(false);
                    Question q = db.getQuestionById(idQuestion);
                    if (q != null) {
                        edtQuestionName.setText(q.getQuestion());
                        edtChoice1.setText(q.getOption1());
                        edtChoice2.setText(q.getOption2());
                        edtChoice3.setText(q.getOption3());
                        switch (q.getAnswerNb()) {
                            case 1:
                                rdb1.setChecked(true);
                                break;
                            case 2:
                                rdb2.setChecked(true);
                                break;
                            case 3:
                                rdb3.setChecked(true);
                                break;
                        }
                    } else {
                        Toast.makeText(EditQuestion.this, "ID does not exist", Toast.LENGTH_SHORT).show();
                        edtId.setEnabled(true);
                        edtId.clearFocus();
                        edtId.requestFocus();
                    }

                }
            }

        });




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
    }


// btn confirm update
    public void Confirm(View view) {
        String strID = edtId.getText().toString();
        int idQuestion = Integer.parseInt(strID);


        strQuesName = edtQuestionName.getText().toString().trim();
        if (strQuesName.length() == 0) {
            layoutQuesName.setError("Question is not filled yet");
            edtQuestionName.requestFocus();
            return;
        } else
            layoutQuesName.setError(null);
        strChoice1 = edtChoice1.getText().toString().trim();
        if (strChoice1.length() == 0) {
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
        if (strChoice3.length() == 0) {
            layoutChoice3.setError("Choice 3 is not filled yet");
            edtChoice3.requestFocus();
            return;
        } else
            layoutChoice3.setError(null);
        if (rdb1.isChecked() || rdb2.isChecked() || rdb3.isChecked()) {
            ChoseAnsCorrect();
            Boolean chkAns = db.chkAnswer(strQuesName, strChoice1, strChoice2, strChoice3);
            if (chkAns == false) {
                Boolean update = db.updateQuestion(idQuestion,strQuesName, strChoice1, strChoice2, strChoice3, AnsCorrect);
                if (update == true) {
                    Toast.makeText(this, "Question Update successfully", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder brdInform = new AlertDialog.Builder(this);
                    brdInform.setTitle("Question Information");
                    brdInform.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(EditQuestion.this, "Question edit successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                    edtId.setEnabled(true);
                    edtId.clearFocus();
                    edtQuestionName.clearFocus();
                    edtChoice1.clearFocus();
                    edtChoice2.clearFocus();
                    edtChoice3.clearFocus();
                    edtId.requestFocus();
                    brdInform.setNegativeButton("Return", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent I = getIntent();
                            String username = I.getStringExtra("username");
                            Intent i = new Intent(getApplicationContext(),Menu.class);
                            i.putExtra("username",username);
                            startActivity(i);
                            Toast.makeText(EditQuestion.this, "Return Success", Toast.LENGTH_SHORT).show();
                        }
                    });
                    String strQuestionInfo;
                    strQuestionInfo = "Question Name : " + strQuesName + "\n";
                    strQuestionInfo += "Choice 1: " + strChoice1 + "\n";
                    strQuestionInfo += "Choice 2: " + strChoice2 + "\n";
                    strQuestionInfo += "Choice 3: " + strChoice3 + "\n";
                    strQuestionInfo += " Number Choice Correct:" + AnsCorrect + "\n";
                    brdInform.setMessage(strQuestionInfo);
                    brdInform.create().show();
                } else {
                    Toast.makeText(this, "Question Update failed", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Question and answer already exist", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "You need choice answer correct", Toast.LENGTH_SHORT).show();
        }

    }

// chose answer with radiobutton
    private void ChoseAnsCorrect() {

        if (rdb1.isChecked()) {
            AnsCorrect = 1;
        } else if (rdb2.isChecked()) {
            AnsCorrect = 2;
        } else
            AnsCorrect = 3;
    }


// display question by id
//    public void displayQuestionById(int id) {
//
//
//
//            Question q = db.getQuestionById(id);
//            if (q != null) {
//                edtQuestionName.setText(q.getQuestion());
//                edtChoice1.setText(q.getOption1());
//                edtChoice2.setText(q.getOption2());
//                edtChoice3.setText(q.getOption3());
//                switch (q.getAnswerNb()) {
//                    case 1:
//                        rdb1.setChecked(true);
//                        break;
//                    case 2:
//                        rdb2.setChecked(true);
//                        break;
//                    case 3:
//                        rdb3.setChecked(true);
//                        break;
//                }
//            } else {
//                Toast.makeText(this, "ID does not exist", Toast.LENGTH_SHORT).show();
//                edtId.setEnabled(true);
//                edtId.clearFocus();
//                edtId.requestFocus();
//            }
//
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}


