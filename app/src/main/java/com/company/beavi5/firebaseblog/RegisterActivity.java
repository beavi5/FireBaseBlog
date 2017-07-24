package com.company.beavi5.firebaseblog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;
    private EditText mNameField, mPasswordField, mEmailField;
    private Button mRegisterBtn;
    private Pattern pattern;
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth= FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mProgress = new ProgressDialog(this);

        mEmailField = (EditText) findViewById(R.id.emailField);
        mNameField = (EditText) findViewById(R.id.nameField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);

        mRegisterBtn = (Button) findViewById(R.id.registerBtn);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });


    }

    private void startRegister() {

        final String name = mNameField.getText().toString().trim();
        String email = mEmailField.getText().toString().trim();

        String password = mPasswordField.getText().toString().trim();
        if (email.length()<5 || password.length()<6) {
            Toast.makeText(RegisterActivity.this, "Error Login/Password", Toast.LENGTH_LONG).show();
            return;
        }

        if (!validate(email)) {  Toast.makeText(RegisterActivity.this, "Invalidate Email", Toast.LENGTH_LONG).show();
            return;}



        if (!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
            mProgress.setMessage("Signing Up...");
            mProgress.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
//                    String user_id = mAuth.getCurrentUser().getUid();
//                    DatabaseReference current_user_db =  mDatabase.child(user_id);
//                    current_user_db.child("Name").setValue(name);
//                    current_user_db.child("image").setValue("default");
                    mProgress.dismiss();
//                    Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
//                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                  startActivity(mainIntent);

                    Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
                    setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setupIntent);


                }





            }
        });




        }



    }
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";



    public boolean validate(final String hex) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(hex);

        return matcher.matches();
    }
}
