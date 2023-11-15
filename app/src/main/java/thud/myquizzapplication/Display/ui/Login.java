package thud.myquizzapplication.Display.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import thud.myquizzapplication.Data.DbHelper;
import thud.myquizzapplication.R;

public class Login extends AppCompatActivity {
    EditText UserName,Pass;
    Button  Login, btnRegister,btnReturn;

   public static final String SHARE_USERNAME = "username";
    DbHelper db;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UserName = findViewById(R.id.edt_usernames);
        Pass = findViewById(R.id.edt_pass);
        Login = findViewById(R.id.btn_login);
        btnReturn = findViewById(R.id.btn_return);

        db = new DbHelper(this);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = UserName.getText().toString();
                String pass = Pass.getText().toString();

                if(user.equals("") ) {
                    Toast.makeText(Login.this, "Please enter Username", Toast.LENGTH_SHORT).show();
                    UserName.setError("Username can not be empty");
                }else if ( pass.equals("")){
                        Toast.makeText(Login.this, "Please enter Password", Toast.LENGTH_SHORT).show();
                        UserName.setError("Password can not be empty");
                    }
                else {
                    Boolean checkuser = db.chkUser(user,pass);
                    if (checkuser == true){
                        Toast.makeText(Login.this, "Sign successfully", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent inten = new Intent(getApplicationContext(), Menu.class);
                        inten.putExtra(SHARE_USERNAME,user);
                        startActivity(inten);
                        Toast.makeText(Login.this, "Welcome to  "+ user +" Menu Page", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(Login.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        btnRegister = findViewById(R.id.btn_gotoregister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(getApplicationContext(),Register.class);
                startActivity(i);
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent I = getIntent();
                String username = I.getStringExtra("username");
                if (username == null){
                    Toast.makeText(Login.this, "You are not logged in, Please Login", Toast.LENGTH_SHORT).show();
                }else
                {
                    finish();
                    Intent itent = new Intent(getApplicationContext(),Menu.class);
                    itent.putExtra("username",username);
                    startActivity(itent);
                }

            }
        });



    }
}