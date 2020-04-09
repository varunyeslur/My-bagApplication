package com.yeslurbags.varun.mybagapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yeslurbags.varun.mybagapplication.model.Users;

public class LoginActivity extends AppCompatActivity {

    private EditText InputNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private String parentDBname = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = (Button) findViewById(R.id.login_btn);
        InputNumber = (EditText) findViewById(R.id.phonenumber_login);
        InputPassword = (EditText) findViewById(R.id.password_login);
        loadingBar = new ProgressDialog(this);

        LoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
             LoginUser();
            }


        });




    }

    private void LoginUser()
    {

        String phonenumber = InputNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(phonenumber))
    {
        Toast.makeText(this,"Please Enter Phone Number", Toast.LENGTH_SHORT).show();
    }
     else if (TextUtils.isEmpty(password))
    {
        Toast.makeText(this,"Please Enter Password", Toast.LENGTH_SHORT).show();
    }

        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait while we validate your Credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phonenumber, password);
        }


    }

    private void AllowAccessToAccount(final String phonenumber, final String password)

    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if (dataSnapshot.child(parentDBname).child(phonenumber).exists())
                {

                    Users usersData = dataSnapshot.child(parentDBname).child(phonenumber).getValue(Users.class);

                    if(usersData.getPhone().equals(phonenumber))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                            Toast.makeText(LoginActivity.this,"Logged in Successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);

                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,"Invalid Credentials",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }

                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,"This Phone Number "+phonenumber+"is not Registered", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Account with this number"+phonenumber+"doesn't exist",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
