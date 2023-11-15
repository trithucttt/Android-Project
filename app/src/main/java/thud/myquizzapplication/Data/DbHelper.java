package thud.myquizzapplication.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public static  String DBNAME = "Login.db";

    public DbHelper(Context context) {
        super(context,"Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase Mydb) {

        Mydb.execSQL("create table Users(username TEXT primary key , password TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase Mydb, int i, int j) {

        Mydb.execSQL("Drop table if exists Users ");
    }
// insert user
    public  Boolean InsertData(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("username", username);
        content.put("password" ,password);
        long result = MyDB.insert("Users",null,content);
        if (result == -1) return false;
        else
            return true;

    }
// check user exist
    public  Boolean chkUser(String username){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Users where username = ?" , new String[] {username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
// check user login
    public Boolean chkUser(String  username, String pass){
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("select * from Users where username = ? and password = ? ",new String[] {username,pass});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;

    }
}


