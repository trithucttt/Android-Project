package thud.myquizzapplication.Display.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import thud.myquizzapplication.Data.DbHelper;
import thud.myquizzapplication.R;

public class Register extends AppCompatActivity {

    EditText UserName,Pass,RePass;
    Button Register, Signin,Return;
    DbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        UserName = findViewById(R.id.edt_username);
        Pass = findViewById(R.id.edt_password);
        RePass = findViewById(R.id.edt_repass);
        Register = findViewById(R.id.btn_register);
        Signin = findViewById(R.id.btn_signin);
        Return = findViewById(R.id.btn_returnmenuregis);
        db = new DbHelper(this);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = UserName.getText().toString();
                String pass = Pass.getText().toString();
                String repass = RePass.getText().toString();

                if(user.equals("") ) {
                    Toast.makeText(Register.this, "Please enter Username", Toast.LENGTH_SHORT).show();
                    UserName.setError("Username can not be empty");
                }else if ( pass.equals("")){
                    Toast.makeText(Register.this, "Please enter Password", Toast.LENGTH_SHORT).show();
                    UserName.setError("Password can not be empty");
                }
                else if (repass.equals("")){
                    Toast.makeText(Register.this, "Please enter confirm Password", Toast.LENGTH_SHORT).show();
                    UserName.setError("Confirm password can not be empty");
                }
                else {
                    if (pass.equals(repass)){
                        Boolean checkUser = db.chkUser(user);
                        if(checkUser == false){
                            Boolean insert = db.InsertData(user, pass);
                            if(insert == true){
                                Toast.makeText(Register.this, "Register successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent itent = new Intent(getApplicationContext(), Login.class);
                                startActivity(itent);
                            }else {
                                Toast.makeText(Register.this, "Registration fail", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(Register.this, "User already exists please sign in", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(Register.this, "password not matching", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent itent = new Intent(getApplicationContext(),Login.class );
                startActivity(itent);
            }
        });

        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent I = getIntent();
                String username = I.getStringExtra("username");
                if (username == null){
                    Toast.makeText(Register.this, "You are not Logged in, Please Login", Toast.LENGTH_SHORT).show();
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