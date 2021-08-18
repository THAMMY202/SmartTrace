package com.smart.trace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {


    private EditText
            editTextFirstName,
            editTextLastName,
            editTextPhone,
            editTextEmail,
            editTextPassword,
            editTextOrgName,
            editTextOrgPhone,
            editTextOrgEmail;


    private Button buttonSignUp;
    private CheckBox checkBoxUserRole;

    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    FirebaseUser user = null;

    private DatabaseReference mCustomerDatabase;
    private String userID, userRole = "individual";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();
    }

    private void initView() {

        auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        editTextFirstName = findViewById(R.id.firstName);
        editTextLastName = findViewById(R.id.lastName);
        editTextPhone = findViewById(R.id.phone);
        editTextEmail = findViewById(R.id.Email);
        checkBoxUserRole = findViewById(R.id.chkUserRole);

        editTextOrgName = findViewById(R.id.etOrgName);
        editTextOrgPhone = findViewById(R.id.etOrgPhone);
        editTextOrgEmail = findViewById(R.id.etOrgEmail);

        editTextPassword = findViewById(R.id.password);
        buttonSignUp = findViewById(R.id.registerBtn);

        TextView textViewSignInLink = findViewById(R.id.createText);
        textViewSignInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });


        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

        checkBoxUserRole.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    userRole = "Organisation";
                    editTextOrgName.setVisibility(View.VISIBLE);
                    editTextOrgPhone.setVisibility(View.VISIBLE);
                    editTextOrgEmail.setVisibility(View.VISIBLE);
                } else {
                    userRole = "individual";
                    editTextOrgName.setVisibility(View.GONE);
                    editTextOrgPhone.setVisibility(View.GONE);
                    editTextOrgEmail.setVisibility(View.GONE);
                }
            }
        });

    }

    private void createUser() {

        final String email = editTextEmail.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String firstName = editTextFirstName.getText().toString().trim();
        final String lastName = editTextLastName.getText().toString().trim();
        final String OrgName = editTextOrgName.getText().toString().trim();
        final String OrgPhone = editTextOrgPhone.getText().toString().trim();
        final String OrgEmail = editTextOrgEmail.getText().toString().trim();

        if (userRole.equals("Organisation")) {

            if (TextUtils.isEmpty(OrgName)) {
                Toast.makeText(getApplicationContext(), "Store name is required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(OrgPhone)) {
                Toast.makeText(getApplicationContext(), "Store tel number is required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(OrgEmail)) {
                Toast.makeText(getApplicationContext(), "Store email is required", Toast.LENGTH_SHORT).show();
                return;
            }

        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(getApplicationContext(), "Please enter first name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(getApplicationContext(), "Please enter first last", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                        } else {

                            userID = auth.getCurrentUser().getUid();
                            mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

                            Map userInfo = new HashMap();
                            userInfo.put("firstName", firstName);
                            userInfo.put("lastName", lastName);
                            userInfo.put("email", email);
                            userInfo.put("phone", phone);
                            userInfo.put("passWord", password);
                            userInfo.put("type", userRole);
                            userInfo.put("key", userID);
                            userInfo.put("picture", "0");

                            if (userRole.equals("Organisation")) {
                                userInfo.put("organisationName", OrgName);
                                userInfo.put("organisationPhone", OrgPhone);
                                userInfo.put("organisationEmail", OrgEmail);
                            }

                            mCustomerDatabase.updateChildren(userInfo);

                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}