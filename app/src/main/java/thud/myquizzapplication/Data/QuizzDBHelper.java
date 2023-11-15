package thud.myquizzapplication.Data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.lights.LightsManager;

import thud.myquizzapplication.Data.QuizzContract.*;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class QuizzDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyQuizz.db";
    private static final int DATABASE_VERSION = 3;
    private SQLiteDatabase db;
    public QuizzDBHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        final  String SQL_CREATE_QUESTION_TABLE = "CREATE TABLE " +  QuestionTable.TABLE_NAME + " ( " +
                QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionTable.COLUMN_QUESTION + " TEXT, " +
                QuestionTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionTable.COLUMN_ANSWER_NB + " INTEGER " + ")";

        db.execSQL(SQL_CREATE_QUESTION_TABLE);
        fillQuestionTable();
    }

    private void fillQuestionTable() {
        Question q1 = new Question("Which of the following methods is called in an Activity when another Activity is called?" ,"onStop" ,"onPause()" , "onCreate()" ,2);
        addQuestion(q1);
        Question q2 = new Question("Which attribute is required to declare when using Layout?  " ,"Layout_with (1)"," Layout_height (2)","All (1), (2) correct",3);
        addQuestion(q2);
        Question q3 = new Question("What function does AndroidManifest have in Android Studio?" ,"As a file, set permissions, Activity, Service,..." ,"Is the directory containing Activity, Service,..." , "As default program in Android Studio" ,1);
        addQuestion(q3);
        Question q4 = new Question("What function does java folder have on Android Studio window?" ,"Save files containing interface design Java code" ,"Save all Activity classes that handle user business" , "Save Java source code for Java application programming" ,2);
        addQuestion(q4);
        Question q5 = new Question("Which component to pass data between activities in Android?" ," Fragment" ,"Broadcast receiver" , "Intent" ,3);
        addQuestion(q5);
//        Question q6 = new Question("Layout or design of android application is saved in file" ," .dex" ," .xml" , " .java" ,2);
//        addQuestion(q6);
//        Question q7 = new Question("One type of presentation layout that allows all elements to be arranged in order is: " ," RelativeLayout" ," ConstraintLayout" , " LinearLayout" ,3);
//        addQuestion(q7);
//        Question q8 = new Question("The component that manages the look and feel on the screen in Android is called:" ,"layout" ," intent" , "fragment" ,1);
//        addQuestion(q8);
//        Question q9 = new Question("How many orientations does Android support?" ,"2" ,"4" , "6" ,2);
//        addQuestion(q9);
//        Question q10 = new Question("What function does AVD Manager have?" ,"Virtual machine management emulator" ,"Management of AVD" , "Emulator virtual machine management" ,1);
//        addQuestion(q10);

    }

    // insert data with keyboard
    public Boolean InsertQuestion(String questionName,String Choice1, String Choice2,String Choice3,int numbercorrect){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(QuestionTable.COLUMN_QUESTION,questionName);
        content.put(QuestionTable.COLUMN_OPTION1,Choice1);
        content.put(QuestionTable.COLUMN_OPTION2,Choice2);
        content.put(QuestionTable.COLUMN_OPTION3,Choice3);
        content.put(QuestionTable.COLUMN_ANSWER_NB, numbercorrect);
        long result = MyDB.insert(QuestionTable.TABLE_NAME,null,content);
        if(result == -1) return false;
        else
            return true;
    }
    //check answer
    public  Boolean chkAnswer(String questionName,String Choice1, String Choice2,String Choice3){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from "+ QuestionTable.TABLE_NAME + " where " + QuestionTable.COLUMN_QUESTION + " =?  and "
                + QuestionTable.COLUMN_OPTION1 + " =? and " + QuestionTable.COLUMN_OPTION2 + " =? and "
                + QuestionTable.COLUMN_OPTION3 + " =? ", new String[] {questionName,Choice1,Choice2,Choice3});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    //delete Question
    public  int deleteQuestion(int id){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return  MyDB.delete(QuestionTable.TABLE_NAME,QuestionTable._ID + " = " + id,null);
    }
// get question by id
public Question getQuestionById(int id) {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.query(QuestionTable.TABLE_NAME, new String[] {
                    QuestionTable.COLUMN_QUESTION, QuestionTable.COLUMN_OPTION1, QuestionTable.COLUMN_OPTION2, QuestionTable.COLUMN_OPTION3, QuestionTable.COLUMN_ANSWER_NB }, QuestionTable._ID + "=?",
            new String[] { String.valueOf(id) }, null, null, null, null);
    if (cursor != null && cursor.getCount() > 0) {
        cursor.moveToFirst();
        Question question = new Question((cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
        return question;
    } else {
        return null;
    }
}


    public Boolean updateQuestion(int id , String questionName,String Choice1, String Choice2,String Choice3,int NbCorrect){
            SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(QuestionTable.COLUMN_QUESTION,questionName);
        value.put(QuestionTable.COLUMN_OPTION1,Choice1);
        value.put(QuestionTable.COLUMN_OPTION2,Choice2);
        value.put(QuestionTable.COLUMN_OPTION3,Choice3);
        value.put(QuestionTable.COLUMN_ANSWER_NB,NbCorrect);
        long  result =   db.update(QuestionTable.TABLE_NAME,value,QuestionTable._ID + " = " + id , null);
        if (result == -1) return false;
        else
            return true;
        }




    //insert data from available
    private void addQuestion(Question question){
        ContentValues value = new ContentValues();
        value.put(QuestionTable.COLUMN_QUESTION, question.getQuestion());
        value.put(QuestionTable.COLUMN_OPTION1,question.getOption1());
        value.put(QuestionTable.COLUMN_OPTION2,question.getOption2());
        value.put(QuestionTable.COLUMN_OPTION3,question.getOption3());
        value.put(QuestionTable.COLUMN_ANSWER_NB,question.getAnswerNb());
        db.insert(QuestionTable.TABLE_NAME,null,value);
    }
    @SuppressLint("Range")
    public List<Question> getAllQuestion(){
        List<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + QuestionTable.TABLE_NAME , null);
        if(cursor.moveToFirst()){
            do{
                Question question = new Question();
                question.setQuestion(cursor.getString(cursor.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setId(cursor.getInt(cursor.getColumnIndex(QuestionTable._ID)));
                question.setOption1(cursor.getString(cursor.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(cursor.getString(cursor.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(cursor.getString(cursor.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setAnswerNb(cursor.getInt(cursor.getColumnIndex(QuestionTable.COLUMN_ANSWER_NB)));
                questionList.add(question);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return questionList;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);
        onCreate(db);
    }


}
